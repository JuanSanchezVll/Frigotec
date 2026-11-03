// Converte o erro do axios em uma mensagem para o usuário.
// O backend devolve JSON { status, mensagem, campos: [{ campo, mensagem }] } (GlobalExceptionHandler).
export function mensagemDeErro(erro, padrao = "Algo deu errado. Tente de novo.") {
    if (!erro.response) {
        return "Não foi possível falar com o servidor. Confira se o backend está rodando na porta 8080."
    }
    const { mensagem, campos } = erro.response.data ?? {}
    const detalhes = (campos ?? []).map((c) => c.mensagem).join(" ")
    if (mensagem) return detalhes ? `${mensagem} ${detalhes}` : mensagem
    return `${padrao} (erro ${erro.response.status})`
}
