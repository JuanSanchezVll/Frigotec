package com.frigo.frigotec;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** Login, cadastro e permissões por perfil, usando o seed (data.sql) e o token JWT real. */
@SpringBootTest(properties = {"spring.datasource.url=jdbc:h2:mem:frigotec_fluxo_teste",
        "spring.sql.init.data-locations=classpath:data.sql,classpath:data-demo.sql"})
@AutoConfigureMockMvc
class FluxoPrincipalTest {

    private static final String ENDERECO = "\"endereco\":{\"cep\":\"01001000\",\"rua\":\"Praça da Sé\",\"numero\":\"1\",\"cidade\":\"São Paulo\",\"estado\":\"SP\"}";

    @Autowired
    private MockMvc mvc;

    private String login(String email, String senha, int statusEsperado) throws Exception {
        String corpo = mvc.perform(post("/usuarios/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"" + email + "\",\"senha\":\"" + senha + "\"}"))
                .andExpect(status().is(statusEsperado))
                .andReturn().getResponse().getContentAsString();
        Matcher m = Pattern.compile("\"token\":\"([^\"]+)\"").matcher(corpo);
        return m.find() ? m.group(1) : null;
    }

    private String token(String email) throws Exception {
        String token = login(email, "senha123", 200);
        assertTrue(token != null && token.split("\\.").length == 3, "o login deve devolver um JWT com 3 partes");
        return token;
    }

    @Test
    void asTresPersonasFazemLoginEORespostaTrazOPerfil() throws Exception {
        mvc.perform(post("/usuarios/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"mendesgusta58@gmail.com\",\"senha\":\"senha123\"}"))
                .andExpect(status().isOk()).andExpect(content().string(org.hamcrest.Matchers.containsString("\"perfil\":\"CLIENTE\"")));
        token("davi@frigotec.com");
        token("admin@frigotec.com");
    }

    @Test
    void senhaErradaRetorna401() throws Exception {
        login("mendesgusta58@gmail.com", "errada", 401);
    }

    @Test
    void rotaProtegidaSemTokenRetorna401() throws Exception {
        mvc.perform(get("/chamados/1001")).andExpect(status().isUnauthorized());
    }

    @Test
    void cadastroDeClienteSalvaEPermiteLogin() throws Exception {
        mvc.perform(post("/usuarios").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nomeCompleto\":\"Cliente Teste\",\"email\":\"novo.cliente@exemplo.com\",\"senha\":\"senha123\",\"cpf\":\"45317828791\","
                                + "\"telefone\":\"1133334444\",\"celular\":\"11999998888\"," + ENDERECO + "}"))
                .andExpect(status().isCreated()).andExpect(content().string(org.hamcrest.Matchers.containsString("\"perfil\":\"CLIENTE\"")));
        token("novo.cliente@exemplo.com");
    }

    @Test
    void somenteAdminCadastraColaborador() throws Exception {
        String corpo = "{\"nomeCompleto\":\"Tecnico Teste\",\"email\":\"novo.tecnico@frigotec.com\",\"senha\":\"senha123\",\"cpf\":\"86288366757\","
                + "\"telefone\":\"1133334444\",\"celular\":\"11999997777\",\"perfil\":\"TECNICO\"," + ENDERECO + "}";
        mvc.perform(post("/usuarios/colaboradores").header("Authorization", "Bearer " + token("davi@frigotec.com"))
                .contentType(MediaType.APPLICATION_JSON).content(corpo)).andExpect(status().isForbidden());
        mvc.perform(post("/usuarios/colaboradores").header("Authorization", "Bearer " + token("admin@frigotec.com"))
                .contentType(MediaType.APPLICATION_JSON).content(corpo)).andExpect(status().isCreated());
        token("novo.tecnico@frigotec.com");
    }

    @Test
    void clienteNaoListaTodosOsChamados() throws Exception {
        mvc.perform(get("/chamados").header("Authorization", "Bearer " + token("mendesgusta58@gmail.com")))
                .andExpect(status().isForbidden());
    }

    @Test
    void clienteNaoVeChamadoDeOutroCliente() throws Exception {
        // chamado 1002 é da Maria; o João recebe 404 (não revela que existe)
        mvc.perform(get("/chamados/1002").header("Authorization", "Bearer " + token("mendesgusta58@gmail.com")))
                .andExpect(status().isNotFound());
    }

    @Test
    void transicaoForaDeOrdemRetorna409() throws Exception {
        // chamado 1002 está em APROVADO_PELO_TECNICO: pular para APROVACAO_DO_SERVICO não é permitido
        mvc.perform(patch("/chamados/1002/status").param("novoStatus", "APROVACAO_DO_SERVICO")
                        .header("Authorization", "Bearer " + token("davi@frigotec.com")))
                .andExpect(status().isConflict());
    }

    @Test
    void orcamentoPendenteImpedeNovoOrcamentoEOEncerramento() throws Exception {
        String davi = "Bearer " + token("davi@frigotec.com");
        // chamado 1003 está em VISITA_AGENDADA com visita já realizada (seed)
        mvc.perform(post("/orcamentos").header("Authorization", davi).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"chamadoId\":1003,\"valor\":200.00,\"descricao\":\"Resistência\"}"))
                .andExpect(status().isCreated());
        mvc.perform(post("/orcamentos").header("Authorization", davi).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"chamadoId\":1003,\"valor\":10.00,\"descricao\":\"Outro\"}"))
                .andExpect(status().isConflict());
        mvc.perform(post("/chamados/1003/encerramento").header("Authorization", davi).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"motivo\":\"Reparo\",\"descricao\":\"Feito\"}"))
                .andExpect(status().isConflict());
    }
}
