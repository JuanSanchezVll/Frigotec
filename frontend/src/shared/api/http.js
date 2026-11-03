import axios from "axios"
import { avisarNaoAutenticado, lerToken } from "./token"

// Cliente HTTP único do projeto.
// Em desenvolvimento, "/api" é repassado pelo Vite para o backend Spring (http://localhost:8080).
export const http = axios.create({
    baseURL: import.meta.env.VITE_API_URL || "/api",
})

// Toda requisição leva o token JWT (se houver): Authorization: Bearer <token>.
http.interceptors.request.use((config) => {
    const token = lerToken()
    if (token) config.headers.Authorization = `Bearer ${token}`
    return config
})

// Token vencido, adulterado ou ausente: o backend responde 401 e a sessão é encerrada.
// O login em si também responde 401 (senha errada), por isso fica de fora.
http.interceptors.response.use(
    (resposta) => resposta,
    (erro) => {
        const url = erro.config?.url ?? ""
        if (erro.response?.status === 401 && !url.includes("/usuarios/login")) avisarNaoAutenticado()
        return Promise.reject(erro)
    },
)
