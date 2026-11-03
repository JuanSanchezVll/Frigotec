package com.frigo.frigotec;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** Upload de arquivos (SDD RN-CHM-03 e RN-EUS-05): tipo conferido pelo conteúdo, limites, dono e equipe. */
@SpringBootTest(properties = {"spring.datasource.url=jdbc:h2:mem:frigotec_anexo_teste",
        "spring.sql.init.data-locations=classpath:data.sql,classpath:data-demo.sql",
        "app.upload.dir=${java.io.tmpdir}/frigotec-teste-uploads"})
@AutoConfigureMockMvc
class AnexoTest {

    private static final String JOAO = "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa";

    @Autowired
    private MockMvc mvc;

    private static byte[] jpeg() {
        byte[] b = new byte[64];
        b[0] = (byte) 0xFF; b[1] = (byte) 0xD8; b[2] = (byte) 0xFF; b[3] = (byte) 0xE0;
        return b;
    }

    private static byte[] pdf() {
        return "%PDF-1.4\n%conteudo de teste\n".getBytes(StandardCharsets.US_ASCII);
    }

    private static byte[] mp4() {
        byte[] b = new byte[64];
        b[3] = 0x18; b[4] = 'f'; b[5] = 't'; b[6] = 'y'; b[7] = 'p'; b[8] = 'm'; b[9] = 'p'; b[10] = '4'; b[11] = '2';
        return b;
    }

    private static MockMultipartFile arq(String nome, String tipo, byte[] bytes) {
        return new MockMultipartFile("arquivos", nome, tipo, bytes);
    }

    private String token(String email) throws Exception {
        String corpo = mvc.perform(post("/usuarios/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"" + email + "\",\"senha\":\"senha123\"}"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        return "Bearer " + JsonPath.read(corpo, "$.token");
    }

    private long unidade(String bearer, String modelo) throws Exception {
        String lista = mvc.perform(get("/usuarios/" + JOAO + "/equipamentos").header("Authorization", bearer))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        List<Number> ids = JsonPath.read(lista, "$[?(@.modelo=='" + modelo + "')].equipamentoUsuarioId");
        return ids.get(0).longValue();
    }

    @Test
    void clienteAnexaFotoVideoEBaixaOArquivo() throws Exception {
        String joao = token("mendesgusta58@gmail.com");
        byte[] foto = jpeg();
        String resposta = mvc.perform(multipart("/chamados/1001/anexos").file(arq("defeito.jpg", "image/jpeg", foto))
                        .file(arq("video.mp4", "video/mp4", mp4())).header("Authorization", joao))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].categoria").value("FOTO"))
                .andExpect(jsonPath("$[1].categoria").value("VIDEO"))
                .andReturn().getResponse().getContentAsString();
        int id = JsonPath.read(resposta, "$[0].id");

        mvc.perform(get("/chamados/1001/anexos").header("Authorization", joao))
                .andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(2));
        byte[] baixado = mvc.perform(get("/anexos/" + id + "/arquivo").header("Authorization", joao))
                .andExpect(status().isOk()).andExpect(content().contentType("image/jpeg"))
                .andReturn().getResponse().getContentAsByteArray();
        assertArrayEquals(foto, baixado);

        // a equipe vê; outro cliente não
        mvc.perform(get("/chamados/1001/anexos").header("Authorization", token("davi@frigotec.com"))).andExpect(status().isOk());
        String maria = token("guimesquita.0612@gmail.com");
        mvc.perform(get("/chamados/1001/anexos").header("Authorization", maria)).andExpect(status().isNotFound());
        mvc.perform(get("/anexos/" + id + "/arquivo").header("Authorization", maria)).andExpect(status().isNotFound());
        mvc.perform(get("/anexos/" + id + "/arquivo")).andExpect(status().isUnauthorized());
    }

    @Test
    void tipoPrecisaBaterComOConteudoETerLimites() throws Exception {
        String joao = token("mendesgusta58@gmail.com");
        // texto se passando por JPG
        mvc.perform(multipart("/chamados/1005/anexos").file(arq("falso.jpg", "image/jpeg", "isto nao e uma imagem".getBytes(StandardCharsets.UTF_8)))
                        .header("Authorization", joao))
                .andExpect(status().isUnprocessableEntity());
        // PDF não é aceito como anexo de chamado
        mvc.perform(multipart("/chamados/1005/anexos").file(arq("nota.pdf", "application/pdf", pdf())).header("Authorization", joao))
                .andExpect(status().isUnprocessableEntity());
        // limite de 5 fotos
        for (int i = 0; i < 5; i++) {
            mvc.perform(multipart("/chamados/1005/anexos").file(arq("f" + i + ".jpg", "image/jpeg", jpeg())).header("Authorization", joao))
                    .andExpect(status().isCreated());
        }
        mvc.perform(multipart("/chamados/1005/anexos").file(arq("f6.jpg", "image/jpeg", jpeg())).header("Authorization", joao))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void chamadoFinalizadoERecusadoETecnicoNaoEnvia() throws Exception {
        String joao = token("mendesgusta58@gmail.com");
        mvc.perform(multipart("/chamados/1004/anexos").file(arq("a.jpg", "image/jpeg", jpeg())).header("Authorization", joao))
                .andExpect(status().isConflict());
        mvc.perform(multipart("/chamados/1001/anexos").file(arq("a.jpg", "image/jpeg", jpeg())).header("Authorization", token("davi@frigotec.com")))
                .andExpect(status().isForbidden());
    }

    @Test
    void documentoSoParaMarcaPropriaEFicaComAUnidade() throws Exception {
        String joao = token("mendesgusta58@gmail.com");
        long frigoPro = unidade(joao, "Chapa Bifteira FP-600");
        long outraMarca = unidade(joao, "Forno Turbo ITW-4");

        mvc.perform(multipart("/equipamentos/" + frigoPro + "/documentos").file(arq("nota.pdf", "application/pdf", pdf())).header("Authorization", joao))
                .andExpect(status().isCreated()).andExpect(jsonPath("$[0].categoria").value("DOCUMENTO"));
        mvc.perform(get("/equipamentos/" + frigoPro + "/documentos").header("Authorization", joao))
                .andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(1));
        mvc.perform(multipart("/equipamentos/" + outraMarca + "/documentos").file(arq("nota.pdf", "application/pdf", pdf())).header("Authorization", joao))
                .andExpect(status().isUnprocessableEntity());
        // vídeo não é documento
        mvc.perform(multipart("/equipamentos/" + frigoPro + "/documentos").file(arq("v.mp4", "video/mp4", mp4())).header("Authorization", joao))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void excluirAUnidadeApagaOsDocumentos() throws Exception {
        String joao = token("mendesgusta58@gmail.com");
        String lista = mvc.perform(get("/usuarios/" + JOAO + "/equipamentos").header("Authorization", joao)).andReturn().getResponse().getContentAsString();
        List<Number> modelos = JsonPath.read(lista, "$[?(@.modelo=='Chapa Bifteira FP-600')].equipamentoId");
        String criada = mvc.perform(post("/equipamentos/" + JOAO).header("Authorization", joao).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"equipamentoId\":" + modelos.get(0) + ",\"numeroSerie\":\"ANEXO-TESTE-1\",\"tipoGarantia\":\"Garantia comum\",\"dataCompra\":\"2026-10-01\"}"))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
        long id = ((Number) JsonPath.read(criada, "$.equipamentoUsuarioId")).longValue();
        String doc = mvc.perform(multipart("/equipamentos/" + id + "/documentos").file(arq("nota.pdf", "application/pdf", pdf())).header("Authorization", joao))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
        int anexoId = JsonPath.read(doc, "$[0].id");

        mvc.perform(delete("/equipamentos/" + id).header("Authorization", joao)).andExpect(status().isNoContent());
        mvc.perform(get("/anexos/" + anexoId + "/arquivo").header("Authorization", joao)).andExpect(status().isNotFound());
        assertTrue(listaVazia(joao, id), "a unidade excluída não pode continuar com documentos");
    }

    private boolean listaVazia(String bearer, long unidadeId) throws Exception {
        return mvc.perform(get("/equipamentos/" + unidadeId + "/documentos").header("Authorization", bearer))
                .andReturn().getResponse().getStatus() == 404;
    }
}
