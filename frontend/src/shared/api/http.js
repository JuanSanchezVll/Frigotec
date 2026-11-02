import axios from "axios"

// Cliente HTTP único do projeto. Em desenvolvimento aponta para o json-server (npm run api).
export const http = axios.create({
    baseURL: import.meta.env.VITE_API_URL || "http://localhost:8080",
})
