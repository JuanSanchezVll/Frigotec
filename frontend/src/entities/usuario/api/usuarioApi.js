import { http } from "../../../shared/api"

// Segmento api (FSD) da entidade usuario: só fala com o backend (axios) e devolve os dados da resposta.
// Cada função corresponde a um endpoint do UsuarioController (Spring).

// POST /usuarios/login  -> { token, tipo, expiraEm, perfil, usuario } (a senha nunca volta)
export function login(email, senha) {
    return http.post("/usuarios/login", { email, senha }).then((resposta) => resposta.data)
}

// POST /usuarios  -> autocadastro: o backend sempre grava o perfil CLIENTE
export function cadastrarUsuario(dados) {
    return http.post("/usuarios", dados).then((resposta) => resposta.data)
}

// As rotas abaixo só aceitam um ADMIN: o token JWT (Authorization: Bearer) é anexado pelo axios em shared/api/http.js.

// POST /usuarios/colaboradores  -> só o ADMIN cria TECNICO ou ADMIN (SDD RN-USR-03)
export function cadastrarColaborador(dados) {
    return http.post("/usuarios/colaboradores", dados).then((resposta) => resposta.data)
}

// GET /usuarios/colaboradores  -> só o ADMIN: lista técnicos e administradores
export function listarColaboradores() {
    return http.get("/usuarios/colaboradores").then((resposta) => resposta.data)
}
