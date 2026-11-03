package com.frigo.frigotec.dto;

import com.frigo.frigotec.entity.Perfil;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.br.CPF;

/**
 * Cadastro de colaborador da Frigotec (TECNICO ou ADMIN). SDD RN-USR-03: não passa pelo autocadastro do portal.
 * O CPF é obrigatório; CNPJ e empresa não se aplicam.
 */
public class ColaboradorCadastroDto {

    @NotBlank(message = "O nome completo é obrigatório")
    private String nomeCompleto;

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "Formato de e-mail inválido")
    private String email;

    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
    private String senha;

    @NotBlank(message = "O CPF é obrigatório")
    @CPF(message = "CPF inválido")
    private String cpf;

    @Size(max = 10, message = "O telefone deve ter no máximo 10 caracteres")
    private String telefone;

    @NotBlank(message = "O celular é obrigatório")
    @Pattern(regexp = "\\d+", message = "O celular deve conter apenas números")
    @Size(max = 13, message = "O celular deve ter no máximo 13 caracteres")
    private String celular;

    @NotNull(message = "O endereço é obrigatório")
    @Valid
    private EnderecoCadastroDto endereco;

    @NotNull(message = "O perfil é obrigatório (TECNICO ou ADMIN)")
    private Perfil perfil;

    public String getNomeCompleto() { return nomeCompleto; }
    public void setNomeCompleto(String nomeCompleto) { this.nomeCompleto = nomeCompleto; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public String getCelular() { return celular; }
    public void setCelular(String celular) { this.celular = celular; }
    public EnderecoCadastroDto getEndereco() { return endereco; }
    public void setEndereco(EnderecoCadastroDto endereco) { this.endereco = endereco; }
    public Perfil getPerfil() { return perfil; }
    public void setPerfil(Perfil perfil) { this.perfil = perfil; }
}
