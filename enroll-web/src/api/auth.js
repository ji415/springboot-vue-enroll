import http from "./http";
export const loginApi = (data) => http.post("/api/login", data);
export const logoutApi = () => http.post("/api/logout");
