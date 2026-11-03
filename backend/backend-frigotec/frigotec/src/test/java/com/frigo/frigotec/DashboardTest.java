package com.frigo.frigotec;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** Painel do técnico: os KPIs vêm do banco (GET /dashboard/tecnico), só para TECNICO e ADMIN. */
@SpringBootTest(properties = {"spring.datasource.url=jdbc:h2:mem:frigotec_dashboard_teste",
        "spring.sql.init.data-locations=classpath:data.sql,classpath:data-demo.sql"})
@AutoConfigureMockMvc
class DashboardTest {

    @Autowired
    private MockMvc mvc;

    private String token(String email) throws Exception {
        String corpo = mvc.perform(post("/usuarios/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"" + email + "\",\"senha\":\"senha123\"}"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        return "Bearer " + JsonPath.read(corpo, "$.token");
    }

    @Test
    void tecnicoVeOsKpisDaSuaCarteiraEAFilaGeral() throws Exception {
        mvc.perform(get("/dashboard/tecnico").header("Authorization", token("davi@frigotec.com")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.chamadosNovos", greaterThanOrEqualTo(1)))
                .andExpect(jsonPath("$.meusChamados", greaterThanOrEqualTo(2)))
                .andExpect(jsonPath("$.visitasDestaSemana").isNumber())
                .andExpect(jsonPath("$.orcamentosAguardandoResposta").isNumber())
                .andExpect(jsonPath("$.proximasVisitas").isArray());
    }

    @Test
    void clienteRecebe403EAnonimoRecebe401() throws Exception {
        mvc.perform(get("/dashboard/tecnico").header("Authorization", token("mendesgusta58@gmail.com")))
                .andExpect(status().isForbidden());
        mvc.perform(get("/dashboard/tecnico")).andExpect(status().isUnauthorized());
    }
}
