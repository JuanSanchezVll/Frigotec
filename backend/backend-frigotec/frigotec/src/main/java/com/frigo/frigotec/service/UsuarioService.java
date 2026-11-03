package com.frigo.frigotec.service;

import com.frigo.frigotec.dto.ColaboradorCadastroDto;
import com.frigo.frigotec.dto.LoginRespostaDto;
import com.frigo.frigotec.entity.Empresa;
import com.frigo.frigotec.entity.Perfil;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.List;
import com.frigo.frigotec.entity.Endereco;
import com.frigo.frigotec.exception.CnpjOuCpfTemQueEstarPreenchidoException;
import com.frigo.frigotec.dto.UsuarioCadastroDto;
import com.frigo.frigotec.dto.UsuarioDto;
import com.frigo.frigotec.dto.UsuarioLoginDto;
import com.frigo.frigotec.entity.Usuario;
import com.frigo.frigotec.exception.*;
import com.frigo.frigotec.mapper.UsuarioMapper;
import com.frigo.frigotec.repository.EmpresaRepository;
import com.frigo.frigotec.repository.EnderecoRepository;
import com.frigo.frigotec.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {
private final UsuarioRepository repository;
private final EnderecoRepository enderecoRepository;
private final EmpresaRepository empresaRepository;
private final UsuarioMapper usuarioMapper;
private final PasswordEncoder passwordEncoder;
private final TokenService tokenService;

public UsuarioService(UsuarioRepository repository,
                      EnderecoRepository enderecoRepository,
                      EmpresaRepository empresaRepository,
                      UsuarioMapper usuarioMapper,
                      PasswordEncoder passwordEncoder,
                      TokenService tokenService) {
    this.repository = repository;
    this.enderecoRepository = enderecoRepository;
    this.empresaRepository = empresaRepository;
    this.usuarioMapper = usuarioMapper;
    this.passwordEncoder = passwordEncoder;
    this.tokenService = tokenService;
}

/** Autocadastro do portal: sempre cria CLIENTE (RN-USR-02). */
@Transactional
public UsuarioDto cadastrar(UsuarioCadastroDto novoUsuario) {
    return salvar(novoUsuario, Perfil.CLIENTE);
}

/**
 * Cadastro de TECNICO ou ADMIN (RN-USR-03). Só um ADMIN chega aqui: a rota exige ROLE_ADMIN (SegurancaConfig).
 */
@Transactional
public UsuarioDto cadastrarColaborador(ColaboradorCadastroDto dto) {
    if (dto.getPerfil() == Perfil.CLIENTE) {
        throw new RegraDeNegocioException("Colaborador deve ter perfil TECNICO ou ADMIN.");
    }
    UsuarioCadastroDto base = new UsuarioCadastroDto();
    base.setNomeCompleto(dto.getNomeCompleto());
    base.setEmail(dto.getEmail());
    base.setSenha(dto.getSenha());
    base.setCpf(dto.getCpf());
    base.setTelefone(dto.getTelefone());
    base.setCelular(dto.getCelular());
    base.setEndereco(dto.getEndereco());
    return salvar(base, dto.getPerfil());
}

/** Lista técnicos e administradores para a tela do administrador. Só ADMIN. */
public List<UsuarioDto> listarColaboradores() {
    return repository.findByPerfilInOrderByNomeCompletoAsc(List.of(Perfil.TECNICO, Perfil.ADMIN))
            .stream()
            .map(usuarioMapper::toDto)
            .toList();
}

private UsuarioDto salvar(UsuarioCadastroDto novoUsuario, Perfil perfil) {

    boolean temCpf = novoUsuario.getCpf() != null && !novoUsuario.getCpf().isBlank();
    boolean temCnpj = novoUsuario.getCnpj() != null && !novoUsuario.getCnpj().isBlank();


    if (temCpf && temCnpj) {
        throw new CpfECnpjNaoPodemExistirEmConjuntoException("O cadastro não pode conter CPF e CNPJ simultaneamente. " +
                "Escolha apenas um tipo de cadastro.");
    }
    if (!temCpf && !temCnpj) {
        throw new CnpjOuCpfTemQueEstarPreenchidoException("É obrigatório informar o CPF (Pessoa Física) ou " +
                "o CNPJ (Pessoa Jurídica).");
    }


    if (repository.existsByEmail(novoUsuario.getEmail())) {
        throw new EmailJaCadastradoException("E-mail já cadastrado no sistema.");
    }


    if (temCpf) {
        if (repository.existsByCpf(novoUsuario.getCpf())) {
            throw new CpfJaCadastradoException("CPF já cadastrado no sistema.");
        }
        novoUsuario.setCnpj(null);
        novoUsuario.setRazaoSocial(null);
        novoUsuario.setInscricaoEstadual(null);
        novoUsuario.setEmpresa(null);
    }


    if (temCnpj) {
        if (repository.existsByCnpj(novoUsuario.getCnpj())) {
            throw new CnpjJaExisteException("Cnpj já cadastrado no sistema.");
        }
    }

    Usuario usuarioEntity = usuarioMapper.toEntity(novoUsuario);
    usuarioEntity.setPerfil(perfil);
    usuarioEntity.setSenha(passwordEncoder.encode(novoUsuario.getSenha()));


    if (usuarioEntity.getEndereco() != null) {
        Endereco enderecoSalvo = enderecoRepository.save(usuarioEntity.getEndereco());
        usuarioEntity.setEndereco(enderecoSalvo);
    }


    if (usuarioEntity.getEmpresa() != null) {
        Empresa empresaSalva = empresaRepository.save(usuarioEntity.getEmpresa());
        usuarioEntity.setEmpresa(empresaSalva);
    }

    Usuario usuarioSalvo = repository.save(usuarioEntity);

    return usuarioMapper.toDto(usuarioSalvo);
}

    /** Confere e-mail e senha (RN-USR-05) e devolve o token JWT com o perfil (SDD seção 8). */
    public LoginRespostaDto autenticar(UsuarioLoginDto dadosUsuario) {
        // a mesma resposta (401) para e-mail inexistente e senha errada
        Usuario usuario = repository.findByEmail(dadosUsuario.getEmail())
                .orElseThrow(() -> new CredenciaisInvalidasException("E-mail ou senha inválidos."));

        if (!passwordEncoder.matches(dadosUsuario.getSenha(), usuario.getSenha())) {
            throw new CredenciaisInvalidasException("E-mail ou senha inválidos.");
        }

        TokenService.Token token = tokenService.gerar(usuario.getId(), usuario.getPerfil());
        return new LoginRespostaDto(token.valor(), "Bearer", token.expiraEmSegundos(), usuario.getPerfil(), usuarioMapper.toDto(usuario));
    }
}
