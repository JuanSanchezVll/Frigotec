package com.frigo.frigotec.mapper;

import com.frigo.frigotec.dto.UsuarioCadastroDto;
import com.frigo.frigotec.entity.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    private final EnderecoMapper enderecoMapper;
    private final EmpresaMapper empresaMapper;

    // Injeção de dependência dos mappers menores
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
        entity.setCpf(dto.getCpf());
        entity.setTelefone(dto.getTelefone());
        entity.setCelular(dto.getCelular());

        // Delega para os mappers específicos
        entity.setEndereco(enderecoMapper.toEntity(dto.getEndereco()));
        entity.setEmpresa(empresaMapper.toEntity(dto.getEmpresa()));

        return entity;
    }

    public UsuarioCadastroDto toDto(Usuario entity) {
        if (entity == null) {
            return null;
        }

        UsuarioCadastroDto dto = new UsuarioCadastroDto();
        dto.setNomeCompleto(entity.getNomeCompleto());
        dto.setEmail(entity.getEmail());
        dto.setCpf(entity.getCpf());
        dto.setTelefone(entity.getTelefone());
        dto.setCelular(entity.getCelular());

        dto.setEndereco(enderecoMapper.toDto(entity.getEndereco()));
        dto.setEmpresa(empresaMapper.toDto(entity.getEmpresa()));

        return dto;
    }
}