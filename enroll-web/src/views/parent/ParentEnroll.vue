<template>
  <el-card>
<!--    <h2>Enrollment Form (Parent)</h2>-->

    <el-form :model="form" :rules="rules" ref="formRef" label-width="120px">
      <el-form-item label="Student Name" prop="name">
        <el-input v-model="form.name" />
      </el-form-item>

      <el-form-item label="ID Card" prop="idCard">
        <el-input v-model="form.idCard" />
      </el-form-item>

      <el-form-item label="Parent Phone" prop="parentPhone">
        <el-input v-model="form.parentPhone" />
      </el-form-item>

      <el-form-item label="Address" prop="address">
        <el-input v-model="form.address" />
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="onSubmit">Submit</el-button>
        <el-button @click="onProgress">Check Status</el-button>
      </el-form-item>
    </el-form>

    <el-divider />

    <div v-if="progress">
      <el-tag v-if="progress.status === 0" type="warning">Pending Review</el-tag>
      <el-tag v-else-if="progress.status === 1" type="success">Approved</el-tag>
      <el-tag v-else type="danger">Rejected</el-tag>

      <p style="margin-top: 10px;">
        <span v-if="progress.status === 0">Your application is under review...</span>
        <span v-else-if="progress.status === 1">Enrollment success.</span>
        <span v-else>Rejected. You can modify info and submit again.</span>
      </p>
    </div>
  </el-card>
</template>

<script setup>
import { ref } from "vue";
import { ElMessage } from "element-plus";
import { parentSubmit, parentProgress } from "@/api/student";

const formRef = ref();
const form = ref({
  name: "",
  idCard: "",
  parentPhone: "",
  address: "",
});

const rules = {
  name: [{ required: true, message: "Required", trigger: "blur" }],
  idCard: [
    { required: true, message: "Required", trigger: "blur" },
    { pattern: /^[1-9]\d{5}(18|19|20)\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\d|3[01])\d{3}[0-9Xx]$/, message: "Invalid ID card", trigger: "blur" },
  ],
  parentPhone: [
    { required: true, message: "Required", trigger: "blur" },
    { pattern: /^1\d{10}$/, message: "Invalid phone", trigger: "blur" },
  ],
  address: [{ required: true, message: "Required", trigger: "blur" }],
};

const progress = ref(null);

const onSubmit = async () => {
  await formRef.value.validate();
  try {
    await parentSubmit(form.value);
    ElMessage.success("Submitted");
    await onProgress();
  } catch (e) {
    ElMessage.error(e?.response?.data?.msg || "Submit failed");
  }
};

const onProgress = async () => {
  if (!form.value.idCard) return ElMessage.warning("Please input ID card");
  try {
    const res = await parentProgress(form.value.idCard);
    progress.value = res.data.data;
  } catch (e) {
    ElMessage.error(e?.response?.data?.msg || "Query failed");
  }
};
</script>
