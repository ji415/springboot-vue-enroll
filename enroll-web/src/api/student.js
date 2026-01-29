import http from "./http";

// 家长端
export const parentSubmit = (data) => http.post("/api/parent/submit", data);
export const parentProgress = (idCard) => http.get("/api/parent/progress", { params: { idCard } });

// 管理端
export const adminPage = (params) => http.get("/api/admin/students", { params });
export const adminDetail = (id) => http.get(`/api/admin/students/${id}`);
export const adminAudit = (id, status) => http.put(`/api/admin/students/${id}/audit`, null, { params: { status } });
export const adminDelete = (id) => http.delete(`/api/admin/students/${id}`);
export const exportApproved = () => http.get("/api/admin/students/export-approved", { responseType: "blob" });

// 新增：全局统计
export const adminStatistics = () => http.get("/api/admin/students/statistics");
