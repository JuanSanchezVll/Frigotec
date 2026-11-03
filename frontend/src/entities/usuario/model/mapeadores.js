// Segmento model (FSD) da entidade usuario: converte o formulário (como a tela guarda) no JSON que o backend espera.
// Se o backend mudar o formato, só este arquivo muda.
export const soNumeros = (texto = "") => texto.replace(/\D/g, "")

const montarEndereco = (v) => ({
    cep: soNumeros(v.cep),
    estado: v.estado.toUpperCase(),
    cidade: v.cidade,
    rua: v.rua,
    numero: v.numero,
})

// UsuarioCadastroDto (cliente pessoa física ou jurídica)
export function montarCadastroCliente(v, pessoaJuridica) {
    const base = {
        nomeCompleto: v.nome,
        email: v.email,
        senha: v.senha,
        celular: soNumeros(v.celular),
        telefone: soNumeros(v.telefone) || undefined,
        endereco: montarEndereco(v),
    }
    if (!pessoaJuridica) return { ...base, cpf: soNumeros(v.cpf) }
    const empresa = { razaoSocial: v.razaoSocial, inscricaoEstadual: v.inscricaoEstadual, cnpj: soNumeros(v.cnpj) }
    return { ...base, cnpj: empresa.cnpj, razaoSocial: empresa.razaoSocial, inscricaoEstadual: empresa.inscricaoEstadual, empresa }
}

// ColaboradorCadastroDto (técnico da Frigotec: só CPF)
export function montarCadastroColaborador(v, perfil) {
    return {
        nomeCompleto: v.nome,
        email: v.email,
        senha: v.senha,
        cpf: soNumeros(v.cpf),
        celular: soNumeros(v.celular),
        telefone: soNumeros(v.telefone) || undefined,
        endereco: montarEndereco(v),
        perfil,
    }
}
