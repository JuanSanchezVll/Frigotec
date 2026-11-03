package com.frigo.frigotec.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Arquivo enviado pelo cliente. Pertence a um chamado (fotos e vídeo do defeito) ou a uma unidade de equipamento
 * (nota fiscal e comprovantes para a garantia), nunca ao catálogo. O conteúdo fica em disco; aqui só os metadados.
 */
@Entity
public class Anexo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome_original", nullable = false)
    private String nomeOriginal;

    @Column(name = "tipo_conteudo", nullable = false)
    private String tipoConteudo;

    @Column(nullable = false)
    private long tamanho;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoriaAnexo categoria;

    /** Caminho relativo ao diretório de uploads; o nome em disco é gerado pelo servidor (nunca o do cliente). */
    @Column(name = "caminho", nullable = false)
    private String caminho;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm;

    @ManyToOne
    @JoinColumn(name = "chamado_id")
    private Chamado chamado;

    @ManyToOne
    @JoinColumn(name = "equipamento_usuario_id")
    private EquipamentoUsuario equipamentoUsuario;

    @ManyToOne(optional = false)
    @JoinColumn(name = "enviado_por_id", nullable = false)
    private Usuario enviadoPor;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNomeOriginal() { return nomeOriginal; }
    public void setNomeOriginal(String nomeOriginal) { this.nomeOriginal = nomeOriginal; }
    public String getTipoConteudo() { return tipoConteudo; }
    public void setTipoConteudo(String tipoConteudo) { this.tipoConteudo = tipoConteudo; }
    public long getTamanho() { return tamanho; }
    public void setTamanho(long tamanho) { this.tamanho = tamanho; }
    public CategoriaAnexo getCategoria() { return categoria; }
    public void setCategoria(CategoriaAnexo categoria) { this.categoria = categoria; }
    public String getCaminho() { return caminho; }
    public void setCaminho(String caminho) { this.caminho = caminho; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }
    public Chamado getChamado() { return chamado; }
    public void setChamado(Chamado chamado) { this.chamado = chamado; }
    public EquipamentoUsuario getEquipamentoUsuario() { return equipamentoUsuario; }
    public void setEquipamentoUsuario(EquipamentoUsuario equipamentoUsuario) { this.equipamentoUsuario = equipamentoUsuario; }
    public Usuario getEnviadoPor() { return enviadoPor; }
    public void setEnviadoPor(Usuario enviadoPor) { this.enviadoPor = enviadoPor; }
}
