package com.frigo.frigotec.mapper;

import com.frigo.frigotec.dto.*;
import com.frigo.frigotec.entity.*;

public class OrcamentoMapper {
    public static OrcamentoDto toResponse(Orcamento orcamento, Chamado chamado, Usuario usuario,
                                          Endereco endereco, Empresa empresa) {
        if (orcamento == null) {
            return null;
        }

        OrcamentoDto dto = new OrcamentoDto();

        dto.setValor(orcamento.getValor());

        ChamadoDto novoChamado = new ChamadoDto();
        novoChamado.setDataPedido(chamado.getDataPedido());
        novoChamado.setDescricao(chamado.getDescricao());
        novoChamado.setProblema(chamado.getProblema());

        UsuarioDto usuarioDto = new UsuarioDto();
        usuarioDto.setNomeCompleto(usuario.getNomeCompleto());
        usuarioDto.setEmail(usuario.getEmail());
        usuarioDto.setCpf(usuario.getCpf());
        usuarioDto.setTelefone(usuario.getTelefone());
        usuarioDto.setCelular(usuario.getCelular());

        EnderecoDto enderecoDto = new EnderecoDto();
        enderecoDto.setId(endereco.getId());
        enderecoDto.setEstado(endereco.getEstado());
        enderecoDto.setCidade(endereco.getCidade());
       enderecoDto.setRua(endereco.getRua());
       enderecoDto.setNumero(endereco.getNumero());
       enderecoDto.setCep(endereco.getCep());

        usuarioDto.setEndereco(enderecoDto);

        EmpresaDto empresaDto = new EmpresaDto();

        empresaDto.setRazaoSocial(empresa.getRazaoSocial());
        empresaDto.setInscricaoEstadual(empresa.getInscricaoEstadual());
        empresaDto.setCnpj(empresa.getCnpj());

        usuarioDto.setEmpresa(empresaDto);

        novoChamado.setUsuario(usuarioDto);

        novoChamado.setEquipamentoUsuario(chamado.getEquipamentoUsuario());

        dto.setChamado(novoChamado);
        dto.setAceito(orcamento.getAceito());

        return dto;
    }

    public static Orcamento toEntity(OrcamentoCadastroDto orcamentoDto, Chamado chamado) {
        if (orcamentoDto == null) {
            return null;
        }

        Orcamento orcamento = new Orcamento();
        orcamento.setValor(orcamentoDto.getValor());

        Chamado novoChamado = new Chamado();
        novoChamado.setDataPedido(chamado.getDataPedido());
        novoChamado.setDescricao(chamado.getDescricao());
        novoChamado.setProblema(chamado.getProblema());
        novoChamado.setUsuario(chamado.getUsuario());
        novoChamado.setEquipamentoUsuario(chamado.getEquipamentoUsuario());

        orcamento.setChamado(novoChamado);
        orcamento.setAceito(orcamentoDto.getAceito());

        return orcamento;

    }
}
