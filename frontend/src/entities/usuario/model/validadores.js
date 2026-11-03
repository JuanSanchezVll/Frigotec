// Segmento model da entidade usuario: regras de negócio da senha (SDD RN-USR-05). O backend valida de novo: nunca confie só no front.
export const SENHA_MINIMA = 6;

export function validarSenha(senha, confirma) {
    if (senha.length < SENHA_MINIMA) return `A senha precisa ter no mínimo ${SENHA_MINIMA} caracteres.`;
    if (senha !== confirma) return 'As senhas não conferem.';
    return null;
}
