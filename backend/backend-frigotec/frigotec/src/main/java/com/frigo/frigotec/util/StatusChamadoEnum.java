package com.frigo.frigotec.util;

public enum StatusChamadoEnum {

    ENVIADO("Enviado", "Chamado aberto pelo cliente e enviado para análise."),
    APROVADO_PELO_TECNICO("Aprovado pelo técnico", "O técnico analisou o chamado e o aprovou para atendimento."),
    CONTATO_DO_TECNICO("Contato do técnico", "O técnico entrou em contato com o cliente para alinhar detalhes do atendimento."),
    APROVACAO_DO_SERVICO("Aprovação do serviço", "O cliente avalia e aprova o serviço (escopo e condições) proposto."),
    VISITA_AGENDADA("Visita agendada", "A visita técnica foi marcada com data e horário definidos."),
    FINALIZADO("Finalizado", "O atendimento foi concluído e o chamado encerrado.");

    private final String nome;
    private final String descricao;

    StatusChamadoEnum(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }
}
