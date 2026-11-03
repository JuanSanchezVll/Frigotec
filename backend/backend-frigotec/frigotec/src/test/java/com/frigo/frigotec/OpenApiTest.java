package com.frigo.frigotec;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** Documentação da API: o Swagger e a exportação OpenAPI (JSON e YAML) são públicos e trazem o título e o esquema JWT. */
@SpringBootTest(properties = {"spring.datasource.url=jdbc:h2:mem:frigotec_openapi_teste"})
@AutoConfigureMockMvc
class OpenApiTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void exportacaoYamlEPublicaETemTituloEEsquemaJwt() throws Exception {
        mvc.perform(get("/v3/api-docs.yaml"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("openapi: 3.")))
                .andExpect(content().string(containsString("Portal Frigotec")))
                .andExpect(content().string(containsString("bearerAuth")))
                .andExpect(content().string(containsString("/equipamentos/{id}")));
    }

    @Test
    void exportacaoJsonEPublica() throws Exception {
        mvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"openapi\"")));
    }
}
