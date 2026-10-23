package com.frigo.frigotec.entity;

import jakarta.persistence.*;
import org.hibernate.Length;
import org.hibernate.annotations.UuidGenerator;

import javax.annotation.processing.Generated;
import java.util.UUID;

@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "UUID")
    private UUID id;
    @Column(name = "nomeCompleto", nullable = false)
    private String nomeCompleto;
    @Column(name = "email", unique = true, nullable = false)
    private String email;
    @Column(name = "cpf", unique = true)
    private String cpf;
    @Column(name = "cnpj", unique = true)
    private String cnpj;
    @Column(name = "razaoSocial")
    private String razaoSocial;
    @Column(name = "inscricaoEstadual")
    private String inscricaoEstadual;
    @Column(name = "senha", nullable = false)
    private String senha;
    @Column(name = "telefone", length = 10)
    private String telefone;
    @Column(name = "celular",  length = 13)
    private String celular;

    @ManyToOne
    @JoinColumn(name = "endereco_id",  nullable = false)
    private Endereco endereco;

    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getInscricaoEstadual() {
        return inscricaoEstadual;
    }

    public void setInscricaoEstadual(String inscricaoEstadual) {
        this.inscricaoEstadual = inscricaoEstadual;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }
}
