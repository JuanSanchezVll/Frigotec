package com.frigo.frigotec;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** CRUD do equipamento do cliente (relacionamento Usuário → Equipamento → Garantia): criar, ler, atualizar e excluir. */
@SpringBootTest(properties = {"spring.datasource.url=jdbc:h2:mem:frigotec_crud_teste",
        "spring.sql.init.data-locations=classpath:data.sql,classpath:data-demo.sql"})
@AutoConfigureMockMvc
class EquipamentoCrudTest {

    private static final String JOAO = "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa";

    @Autowired
    private MockMvc mvc;

    private String token(String email) throws Exception {
        String corpo = mvc.perform(post("/usuarios/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"" + email + "\",\"senha\":\"senha123\"}"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        Matcher m = Pattern.compile("\"token\":\"([^\"]+)\"").matcher(corpo);
        assertTrue(m.find());
        return "Bearer " + m.group(1);
    }

    private String listaDoJoao(String bearer) throws Exception {
        return mvc.perform(get("/usuarios/" + JOAO + "/equipamentos").header("Authorization", bearer))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
    }

    /** Pega um campo numérico do equipamento do cliente com o modelo informado. */
    private long campo(String lista, String modelo, String campo) {
        List<Number> valores = JsonPath.read(lista, "$[?(@.modelo=='" + modelo + "')]." + campo);
        assertFalse(valores.isEmpty(), "equipamento " + modelo + " (" + campo + ") não veio na lista: " + lista);
        return valores.get(0).longValue();
    }

    private long cadastrarUnidadeNova(String bearer, String serie) throws Exception {
        long modeloId = campo(listaDoJoao(bearer), "Forno Turbo ITW-4", "equipamentoId");
        String corpo = mvc.perform(post("/equipamentos/" + JOAO).header("Authorization", bearer)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"equipamentoId\":" + modeloId + ",\"numeroSerie\":\"" + serie + "\"}"))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
        return ((Number) JsonPath.read(corpo, "$.equipamentoUsuarioId")).longValue();
    }

    @Test
    void clienteCriaAtualizaEExcluiOProprioEquipamento() throws Exception {
        String joao = token("mendesgusta58@gmail.com");
        long id = cadastrarUnidadeNova(joao, "CRUD-TESTE-1");

        mvc.perform(patch("/equipamentos/" + id).header("Authorization", joao)
                        .contentType(MediaType.APPLICATION_JSON).content("{\"numeroSerie\":\"CRUD-TESTE-2\"}"))
                .andExpect(status().isOk()).andExpect(content().string(org.hamcrest.Matchers.containsString("CRUD-TESTE-2")));

        mvc.perform(delete("/equipamentos/" + id).header("Authorization", joao)).andExpect(status().isNoContent());
        assertFalse(listaDoJoao(joao).contains("CRUD-TESTE-2"), "a unidade excluída não pode continuar na lista");
        mvc.perform(delete("/equipamentos/" + id).header("Authorization", joao)).andExpect(status().isNotFound());
    }

    @Test
    void equipamentoComChamadoNaoPodeSerExcluido() throws Exception {
        String joao = token("mendesgusta58@gmail.com");
        long unidadeComChamado = campo(listaDoJoao(joao), "Forno Turbo ITW-4", "equipamentoUsuarioId");
        mvc.perform(delete("/equipamentos/" + unidadeComChamado).header("Authorization", joao)).andExpect(status().isConflict());
    }

    @Test
    void serieRepetidaNoMesmoModeloRetorna409() throws Exception {
        String joao = token("mendesgusta58@gmail.com");
        long id = cadastrarUnidadeNova(joao, "CRUD-TESTE-3");
        mvc.perform(patch("/equipamentos/" + id).header("Authorization", joao)
                        .contentType(MediaType.APPLICATION_JSON).content("{\"numeroSerie\":\"ITW-0001\"}"))
                .andExpect(status().isConflict());
    }

    @Test
    void notaFiscalSoExisteParaMarcaPropria() throws Exception {
        String joao = token("mendesgusta58@gmail.com");
        long id = cadastrarUnidadeNova(joao, "CRUD-TESTE-4");
        mvc.perform(patch("/equipamentos/" + id).header("Authorization", joao)
                        .contentType(MediaType.APPLICATION_JSON).content("{\"notaFiscal\":\"NF-123\"}"))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void outroClienteRecebe404EQuemNaoEClienteRecebe403() throws Exception {
        String joao = token("mendesgusta58@gmail.com");
        long id = cadastrarUnidadeNova(joao, "CRUD-TESTE-5");
        String maria = token("guimesquita.0612@gmail.com");
        mvc.perform(delete("/equipamentos/" + id).header("Authorization", maria)).andExpect(status().isNotFound());
        mvc.perform(patch("/equipamentos/" + id).header("Authorization", maria)
                        .contentType(MediaType.APPLICATION_JSON).content("{\"numeroSerie\":\"X\"}"))
                .andExpect(status().isNotFound());
        mvc.perform(delete("/equipamentos/" + id).header("Authorization", token("davi@frigotec.com"))).andExpect(status().isForbidden());
        mvc.perform(delete("/equipamentos/" + id)).andExpect(status().isUnauthorized());
    }
}
