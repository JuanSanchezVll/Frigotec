package com.frigo.frigotec.service;

import com.frigo.frigotec.entity.Empresa;
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
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {
private final UsuarioRepository repository;
private final EnderecoRepository enderecoRepository;
private final EmpresaRepository empresaRepository;
private final UsuarioMapper usuarioMapper;

public UsuarioService(UsuarioRepository repository,
                      EnderecoRepository enderecoRepository,
                      EmpresaRepository empresaRepository,
                      UsuarioMapper usuarioMapper) {
    this.repository = repository;
    this.enderecoRepository = enderecoRepository;
    this.empresaRepository = empresaRepository;
    this.usuarioMapper = usuarioMapper;
}

public UsuarioDto cadastrar(UsuarioCadastroDto novoUsuario) {

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

    public UsuarioDto autenticar(UsuarioLoginDto dadosUsuario) {
        Usuario usuario = repository.findByEmail(dadosUsuario.getEmail())
                .orElseThrow(() -> new EmailNaoEncontradoException("E-mail não encontrado no sistema!"));

        return usuarioMapper.toDto(usuario);
    }
}