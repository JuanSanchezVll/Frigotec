package com.frigo.frigotec.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CPF;

public class UsuarioCadastroDto {

    @NotBlank(message = "O nome completo é obrigatório")
    private String nomeCompleto;

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "Formato de e-mail inválido")
    private String email;

    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
    private String senha;

    @CPF(message = "CPF inválido")
    private String cpf;

    @CNPJ(message = "CNPJ inválido")
    private String cnpj;

    private String razaoSocial;

    private String inscricaoEstadual;

    @Size(max = 10, message = "O telefone deve ter no máximo 10 caracteres")
    private String telefone;

    @NotBlank(message = "O celular é obrigatório")
    @Pattern(regexp = "\\d+", message = "O celular deve conter apenas números")
    @Size(max = 13, message = "O celular deve ter no máximo 13 caracteres")
    private String celular;

    @Valid
    private EnderecoCadastroDto endereco;

    @Valid
    private EmpresaCadastroDto empresa;


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

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
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

    public EnderecoCadastroDto getEndereco() {
        return endereco;
    }

    public void setEndereco(EnderecoCadastroDto endereco) {
        this.endereco = endereco;
    }

    public EmpresaCadastroDto getEmpresa() {
        return empresa;
    }

    public void setEmpresa(EmpresaCadastroDto empresa) {
        this.empresa = empresa;
    }
}