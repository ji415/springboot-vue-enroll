import axios from "axios";

const http = axios.create({
    baseURL: "http://localhost:8080",
    timeout: 10000,
});

// 请求拦截：自动带 token
http.interceptors.request.use((config) => {
    const token = localStorage.getItem("token");
    if (token) config.headers.Authorization = token; // Sa-Token token-name 配 Authorization
    return config;
});

// 响应拦截：未登录自动跳登录页
http.interceptors.response.use(
    (res) => res,
    (err) => {
        if (err?.response?.status === 401) {
            localStorage.removeItem("token");
            window.location.href = "/login";
        }
        return Promise.reject(err);
    }
);

export default http;
