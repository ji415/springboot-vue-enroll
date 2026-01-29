<script setup>
import { ref, onMounted, onBeforeUnmount, nextTick } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import * as echarts from "echarts";
import {
  adminPage,
  adminDetail,
  adminAudit,
  adminDelete,
  exportApproved,
  adminStatistics,
} from "@/api/student";

const keyword = ref("");
const page = ref(1);
const size = ref(10);
const total = ref(0);
const rows = ref([]);

const detailVisible = ref(false);
const detail = ref(null);

// ===== 饼图相关（全局统计）=====
const chartRef = ref(null);
let chartIns = null;

const stats = ref({ PENDING: 0, APPROVED: 0, REJECTED: 0 });

const resizeChart = () => chartIns?.resize();

const renderChart = () => {
  if (!chartRef.value) return;

  if (!chartIns) {
    chartIns = echarts.init(chartRef.value);
    window.addEventListener("resize", resizeChart);
  }

  const { PENDING, APPROVED, REJECTED } = stats.value;

  chartIns.setOption({
    tooltip: { trigger: "item" },
    legend: { bottom: 0 },
    series: [
      {
        type: "pie",
        radius: ["35%", "70%"],
        label: { formatter: "{b}: {c} ({d}%)" },
        data: [
          { value: PENDING, name: "待审核" },
          { value: APPROVED, name: "通过" },
          { value: REJECTED, name: "驳回" },
        ],
      },
    ],
  });
};

// ===== status 显示工具（兼容没有 statusLabelCn 的情况）=====
const statusText = (row) => {
  if (row?.statusLabelCn) return row.statusLabelCn;
  const s = row?.status;
  if (s === "PENDING") return "Pending";
  if (s === "APPROVED") return "Approved";
  if (s === "REJECTED") return "Rejected";
  return String(s ?? "");
};

const statusTagType = (s) => {
  if (s === "PENDING") return "warning";
  if (s === "APPROVED") return "success";
  return "danger";
};

// 列表
const load = async () => {
  try {
    const res = await adminPage({
      page: page.value,
      size: size.value,
      keyword: keyword.value,
    });
    const p = res.data.data;
    rows.value = p.records || [];
    total.value = p.total || 0;
  } catch (e) {
    ElMessage.error(e?.response?.data?.msg || "Load failed");
  }
};

// 全局统计（调用后端接口）
const loadStatistics = async () => {
  try {
    const res = await adminStatistics();
    // 期望返回：{ PENDING: x, APPROVED: y, REJECTED: z }
    const data = res.data.data || {};
    stats.value = {
      PENDING: data.PENDING ?? 0,
      APPROVED: data.APPROVED ?? 0,
      REJECTED: data.REJECTED ?? 0,
    };
    await nextTick();
    renderChart();
  } catch (e) {
    ElMessage.error(e?.response?.data?.msg || "统计加载失败");
  }
};

const openDetail = async (id) => {
  try {
    const res = await adminDetail(id);
    detail.value = res.data.data;
    detailVisible.value = true;
  } catch (e) {
    ElMessage.error(e?.response?.data?.msg || "Detail failed");
  }
};

// 审核：后端仍收数字 1/2（APPROVED/REJECTED），所以这里不要传字符串
const audit = async (id, status) => {
  try {
    await adminAudit(id, status);
    ElMessage.success("Updated");
    await load();
    await loadStatistics();
  } catch (e) {
    ElMessage.error(e?.response?.data?.msg || "Audit failed");
  }
};

const remove = async (id) => {
  await ElMessageBox.confirm("Confirm delete?", "Warning", { type: "warning" });
  try {
    await adminDelete(id);
    ElMessage.success("Deleted");
    await load();
    await loadStatistics();
  } catch (e) {
    ElMessage.error(e?.response?.data?.msg || "Delete failed");
  }
};

// Excel导出
const onExport = async () => {
  try {
    const res = await exportApproved();
    const blob = new Blob([res.data], {
      type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
    });

    const url = window.URL.createObjectURL(blob);
    const a = document.createElement("a");
    a.href = url;
    a.download = "通过审核学生名单.xlsx";
    document.body.appendChild(a);
    a.click();
    a.remove();
    window.URL.revokeObjectURL(url);
  } catch (e) {
    ElMessage.error("导出失败");
  }
};

onMounted(() => {
  load();
  loadStatistics();
});

onBeforeUnmount(() => {
  window.removeEventListener("resize", resizeChart);
  chartIns?.dispose();
  chartIns = null;
});
</script>

<template>
  <el-card>
    <!-- 搜索框 -->
    <div style="display: flex; gap: 10px; margin-bottom: 12px">
      <el-input
          v-model="keyword"
          placeholder="Search by name or phone"
          style="width: 300px"
      />
      <el-button type="primary" @click="load">Search</el-button>
      <el-button type="success" @click="onExport">导出通过名单</el-button>
    </div>

    <!-- 饼图 -->
    <el-card style="margin: 12px 0">
      <div
          style="
          display: flex;
          align-items: center;
          justify-content: space-between;
          margin-bottom: 8px;
        "
      >
        <div style="font-weight: 600">报名状态占比</div>
        <div style="color: #666; font-size: 12px">统计基于全量数据</div>
      </div>
      <div ref="chartRef" style="width: 100%; height: 280px"></div>
    </el-card>

    <!-- 表格 -->
    <el-table :data="rows" stripe>
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="name" label="Name" />
      <el-table-column prop="parentPhone" label="Phone" />
      <el-table-column prop="idCard" label="ID Card" />
      <el-table-column prop="createTime" label="Create Time" />

      <el-table-column label="Status" width="120">
        <template #default="{ row }">
          <el-tag :type="statusTagType(row.status)">
            {{ statusText(row) }}
          </el-tag>
        </template>
      </el-table-column>

      <el-table-column label="Actions" width="320">
        <template #default="{ row }">
          <el-button size="small" @click="openDetail(row.id)">Detail</el-button>

          <!-- 只有 PENDING 才允许审核 -->
          <el-button
              size="small"
              type="success"
              :disabled="row.status !== 'PENDING'"
              @click="audit(row.id, 1)"
          >
            Approve
          </el-button>

          <el-button
              size="small"
              type="warning"
              :disabled="row.status !== 'PENDING'"
              @click="audit(row.id, 2)"
          >
            Reject
          </el-button>

          <el-button size="small" type="danger" @click="remove(row.id)"
          >Delete</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <div style="margin-top: 12px; display: flex; justify-content: flex-end">
      <el-pagination
          layout="prev, pager, next, sizes, total"
          :total="total"
          v-model:current-page="page"
          v-model:page-size="size"
          @change="load"
          @size-change="load"
      />
    </div>

    <el-dialog v-model="detailVisible" title="Student Detail" width="600px">
      <el-descriptions v-if="detail" :column="1" border>
        <el-descriptions-item label="Name">{{ detail.name }}</el-descriptions-item>
        <el-descriptions-item label="ID Card">{{ detail.idCard }}</el-descriptions-item>
        <el-descriptions-item label="Phone">{{ detail.parentPhone }}</el-descriptions-item>
        <el-descriptions-item label="Address">{{ detail.address }}</el-descriptions-item>
        <el-descriptions-item label="Status">
          {{ detail.statusLabelCn || detail.status }}
        </el-descriptions-item>
        <el-descriptions-item label="Create Time">{{ detail.createTime }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </el-card>
</template>
