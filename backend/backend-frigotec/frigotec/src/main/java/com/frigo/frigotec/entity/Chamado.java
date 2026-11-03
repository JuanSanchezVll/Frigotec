package com.frigo.frigotec.entity;

import com.frigo.frigotec.util.StatusChamadoEnum;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Chamado {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /** Número exibido ao cliente: PR + 8 dígitos, gerado a partir do id (SDD RN-CHM-04). */
    @Column(name = "protocolo", unique = true)
    private String protocolo;

    @Column(name = "dataPedido")
    private LocalDate dataPedido;

    @Column(name = "descricao")
    private String descricao;

    /** Categoria do problema (SDD RN-CHM-02). */
    @Column(name = "problema")
    private String problema;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    /** Técnico responsável; nulo enquanto o chamado está na fila (SDD RN-CHM-12). */
    @ManyToOne
    @JoinColumn(name = "tecnico_id")
    private Usuario tecnico;

    @ManyToOne
    @JoinColumn(name = "equipamento_usuario_id", nullable = false)
    private EquipamentoUsuario equipamentoUsuario;

    @Column(name = "visita_agendada_em")
    private LocalDateTime visitaAgendadaEm;

    /** Histórico em ordem de inserção: o status atual é o último registro (SDD RN-CHM-16). */
    @OneToMany(mappedBy = "chamado", cascade = CascadeType.ALL)
    @OrderBy("id ASC")
    private List<Historico> historicos = new ArrayList<>();

    @OneToMany(mappedBy = "chamado")
    @OrderBy("id ASC")
    private List<Encerramento> encerramentos = new ArrayList<>();

    public List<Historico> getHistoricos() { return historicos; }
    public List<Encerramento> getEncerramentos() { return encerramentos; }

    public void adicionarHistorico(Historico historico) {
        historico.setChamado(this);
        this.historicos.add(historico);
    }

    public StatusChamadoEnum statusAtual() {
        return historicos.isEmpty() ? StatusChamadoEnum.ENVIADO : historicos.get(historicos.size() - 1).getStatus();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getProtocolo() { return protocolo; }
    public void setProtocolo(String protocolo) { this.protocolo = protocolo; }
    public LocalDate getDataPedido() { return dataPedido; }
    public void setDataPedido(LocalDate dataPedido) { this.dataPedido = dataPedido; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public String getProblema() { return problema; }
    public void setProblema(String problema) { this.problema = problema; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public Usuario getTecnico() { return tecnico; }
    public void setTecnico(Usuario tecnico) { this.tecnico = tecnico; }
    public EquipamentoUsuario getEquipamentoUsuario() { return equipamentoUsuario; }
    public void setEquipamentoUsuario(EquipamentoUsuario equipamentoUsuario) { this.equipamentoUsuario = equipamentoUsuario; }
    public LocalDateTime getVisitaAgendadaEm() { return visitaAgendadaEm; }
    public void setVisitaAgendadaEm(LocalDateTime visitaAgendadaEm) { this.visitaAgendadaEm = visitaAgendadaEm; }
}
