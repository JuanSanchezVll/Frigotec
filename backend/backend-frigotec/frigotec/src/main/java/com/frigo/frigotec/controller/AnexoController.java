package com.frigo.frigotec.controller;

import com.frigo.frigotec.config.AutorAutenticado;
import com.frigo.frigotec.dto.AnexoDto;
import com.frigo.frigotec.entity.Anexo;
import com.frigo.frigotec.service.AnexoService;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.List;

/** Envio e consulta de arquivos: fotos e vídeo do chamado, documentos do equipamento (SDD RN-CHM-03 e RN-EUS-05). */
@RestController
public class AnexoController {

    private final AnexoService service;

    public AnexoController(AnexoService service) {
        this.service = service;
    }

    /** CLIENTE dono do chamado (SegurancaConfig): multipart com o campo "arquivos" (JPG, PNG ou MP4). */
    @PostMapping(value = "/chamados/{id}/anexos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<AnexoDto>> anexarAoChamado(@PathVariable Long id,
                                                          @RequestParam("arquivos") List<MultipartFile> arquivos,
                                                          @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.status(201).body(service.anexarAoChamado(id, arquivos, AutorAutenticado.de(jwt)));
    }

    @GetMapping("/chamados/{id}/anexos")
    public ResponseEntity<List<AnexoDto>> listarDoChamado(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(service.listarDoChamado(id, AutorAutenticado.de(jwt)));
    }

    /** CLIENTE dono da unidade (marca própria): multipart com o campo "arquivos" (PDF, JPG ou PNG). */
    @PostMapping(value = "/equipamentos/{id}/documentos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<AnexoDto>> anexarAoEquipamento(@PathVariable Long id,
                                                              @RequestParam("arquivos") List<MultipartFile> arquivos,
                                                              @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.status(201).body(service.anexarAoEquipamento(id, arquivos, AutorAutenticado.de(jwt)));
    }

    @GetMapping("/equipamentos/{id}/documentos")
    public ResponseEntity<List<AnexoDto>> listarDoEquipamento(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(service.listarDoEquipamento(id, AutorAutenticado.de(jwt)));
    }

    /** Baixa o arquivo: o dono ou a equipe Frigotec (outro cliente recebe 404). */
    @GetMapping("/anexos/{id}/arquivo")
    public ResponseEntity<Resource> baixar(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        Anexo anexo = service.buscarParaDownload(id, AutorAutenticado.de(jwt));
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(anexo.getTipoConteudo()))
                .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.inline().filename(anexo.getNomeOriginal(), StandardCharsets.UTF_8).build().toString())
                .header("X-Content-Type-Options", "nosniff")
                .body(service.conteudo(anexo));
    }
}
