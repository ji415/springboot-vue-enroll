<template>
  <el-container style="min-height: 100vh">
    <el-header style="display:flex; align-items:center; gap:12px;">
      <div style="font-weight: 700;">招生报名系统</div>

      <el-menu mode="horizontal" :default-active="activePath" router style="flex:1;">
        <el-menu-item index="/parent">家长端</el-menu-item>
        <el-menu-item index="/admin">管理端</el-menu-item>
      </el-menu>
    </el-header>

    <el-main>
      <!-- 关键：路由页面渲染位置 -->
      <router-view />
    </el-main>
  </el-container>
</template>

<script setup>
import { computed } from "vue";
import { useRoute } from "vue-router";

const route = useRoute();
const activePath = computed(() => route.path);
</script>

<style scoped>
/* 可选：让页面别贴边 */
.el-main {
  padding: 16px;
}
</style>
