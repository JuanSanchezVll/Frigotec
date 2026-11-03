// Token JWT da sessão e o que fazer quando o servidor diz "não autenticado" (401).
// Fica em shared para o axios usar sem importar nada de camadas acima (regra do FSD).
let token = null
let aoNaoAutenticado = () => {}

export const definirToken = (novo) => { token = novo }
export const lerToken = () => token

// A camada de cima registra aqui o que fazer no 401 (ex.: encerrar a sessão e voltar ao login).
export const definirAoNaoAutenticado = (funcao) => { aoNaoAutenticado = funcao }
export const avisarNaoAutenticado = () => aoNaoAutenticado()
