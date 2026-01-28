import { createRouter, createWebHistory } from "vue-router";
import ParentEnroll from "@/views/parent/ParentEnroll.vue";
import AdminStudents from "@/views/admin/AdminStudents.vue";

const routes = [
    { path: "/", redirect: "/parent" },
    { path: "/parent", component: ParentEnroll },
    { path: "/admin", component: AdminStudents },
];

export default createRouter({
    history: createWebHistory(),
    routes,
});
