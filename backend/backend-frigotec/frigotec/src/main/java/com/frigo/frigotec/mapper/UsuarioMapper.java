package com.frigo.frigotec.mapper;

import com.frigo.frigotec.dto.UsuarioCadastroDto;
import com.frigo.frigotec.dto.UsuarioDto;
import com.frigo.frigotec.entity.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    private final EnderecoMapper enderecoMapper;
    private final EmpresaMapper empresaMapper;

    public UsuarioMapper(EnderecoMapper enderecoMapper, EmpresaMapper empresaMapper) {
        this.enderecoMapper = enderecoMapper;
        this.empresaMapper = empresaMapper;
    }


    public Usuario toEntity(UsuarioCadastroDto dto) {
        if (dto == null) {
            return null;
        }

        Usuario entity = new Usuario();
        entity.setNomeCompleto(dto.getNomeCompleto());
        entity.setEmail(dto.getEmail());
        entity.setSenha(dto.getSenha());
        entity.setCpf(dto.getCpf());
        entity.setCnpj(dto.getCnpj());
        entity.setRazaoSocial(dto.getRazaoSocial());
        entity.setInscricaoEstadual(dto.getInscricaoEstadual());
        entity.setTelefone(dto.getTelefone());
        entity.setCelular(dto.getCelular());


        if (dto.getEndereco() != null) {
            entity.setEndereco(
                    enderecoMapper.toEntity(
                            enderecoMapper.toResponseCadastro(dto.getEndereco())
                    )
            );
        }


        if (dto.getEmpresa() != null) {
            entity.setEmpresa(
                    empresaMapper.toEntity(
                            empresaMapper.toResponseCadastro(dto.getEmpresa())
                    )
            );
        }

        return entity;
    }


    public Usuario toEntity(UsuarioDto dto) {
        if (dto == null) {
            return null;
        }

        Usuario entity = new Usuario();
        entity.setNomeCompleto(dto.getNomeCompleto());
        entity.setEmail(dto.getEmail());
        entity.setCpf(dto.getCpf());
        entity.setCnpj(dto.getCnpj());
        entity.setRazaoSocial(dto.getRazaoSocial());
        entity.setInscricaoEstadual(dto.getInscricaoEstadual());
        entity.setTelefone(dto.getTelefone());
        entity.setCelular(dto.getCelular());


        entity.setEndereco(enderecoMapper.toEntity(dto.getEndereco()));
        entity.setEmpresa(empresaMapper.toEntity(dto.getEmpresa()));

        return entity;
    }


    public UsuarioDto toDto(Usuario entity) {
        if (entity == null) {
            return null;
        }

        UsuarioDto dto = new UsuarioDto();
        dto.setNomeCompleto(entity.getNomeCompleto());
        dto.setEmail(entity.getEmail());
        dto.setCpf(entity.getCpf());
        dto.setCnpj(entity.getCnpj());
        dto.setRazaoSocial(entity.getRazaoSocial());
        dto.setInscricaoEstadual(entity.getInscricaoEstadual());
        dto.setTelefone(entity.getTelefone());
        dto.setCelular(entity.getCelular());


        dto.setEndereco(enderecoMapper.toDto(entity.getEndereco()));
        dto.setEmpresa(empresaMapper.toDto(entity.getEmpresa()));

        return dto;
    }
}