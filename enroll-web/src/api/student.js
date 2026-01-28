import axios from "axios";

const http = axios.create({
    baseURL: "http://localhost:8080",
    timeout: 10000,
});

export const parentSubmit = (data) => http.post("/api/parent/submit", data);
export const parentProgress = (idCard) => http.get("/api/parent/progress", { params: { idCard } });

export const adminPage = (params) => http.get("/api/admin/students", { params });
export const adminDetail = (id) => http.get(`/api/admin/students/${id}`);
export const adminAudit = (id, status) => http.put(`/api/admin/students/${id}/audit`, null, { params: { status } });
export const adminDelete = (id) => http.delete(`/api/admin/students/${id}`);

export const exportApproved = () =>
    http.get("/api/admin/students/export-approved", { responseType: "blob" });

