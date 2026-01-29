<template>
  <el-card style="max-width:420px;margin:80px auto;">
    <h3>管理端登录</h3>
    <el-form :model="form" label-width="90px">
      <el-form-item label="用户名">
        <el-input v-model="form.username" />
      </el-form-item>
      <el-form-item label="密码">
        <el-input v-model="form.password" type="password" show-password />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onLogin">登录</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script setup>
import { reactive } from "vue";
import { ElMessage } from "element-plus";
import { useRouter } from "vue-router";
import { loginApi } from "@/api/auth";

const router = useRouter();
const form = reactive({ username: "admin", password: "123456" });

const onLogin = async () => {
  try {
    const res = await loginApi(form);
    const token = res.data.data.token;
    localStorage.setItem("token", token);
    ElMessage.success("登录成功");
    router.push("/admin");
  } catch (e) {
    ElMessage.error(e?.response?.data?.msg || "登录失败");
  }
};
</script>
