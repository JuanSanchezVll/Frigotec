// Sessão do usuário logado, guardada no navegador para sobreviver ao F5.
// Guarda o usuário devolvido pelo login junto com o token JWT (token) e o instante em que ele vence (expiraEmMs).
const CHAVE = "frigotec.usuario"

export function lerSessao() {
    try {
        const sessao = JSON.parse(localStorage.getItem(CHAVE))
        // token já vencido: a sessão não vale mais
        if (sessao?.expiraEmMs && sessao.expiraEmMs < Date.now()) {
            localStorage.removeItem(CHAVE)
            return null
        }
        return sessao
    } catch {
        return null
    }
}

export function gravarSessao(usuario) {
    localStorage.setItem(CHAVE, JSON.stringify(usuario))
}

export function limparSessao() {
    localStorage.removeItem(CHAVE)
}
