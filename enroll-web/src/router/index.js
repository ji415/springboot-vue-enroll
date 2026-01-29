import { createRouter, createWebHistory } from "vue-router";
import ParentEnroll from "@/views/parent/ParentEnroll.vue";
import AdminStudents from "@/views/admin/AdminStudents.vue";
import Login from "@/views/Login.vue";

const router = createRouter({
    history: createWebHistory(),
    routes: [
        { path: "/", redirect: "/parent" },
        { path: "/parent", component: ParentEnroll },
        { path: "/login", component: Login },
        { path: "/admin", component: AdminStudents },
    ],
});

// 路由守卫：未登录禁止进管理端
router.beforeEach((to) => {
    if (to.path.startsWith("/admin")) {
        const token = localStorage.getItem("token");
        if (!token) return "/login";
    }
});

export default router;
