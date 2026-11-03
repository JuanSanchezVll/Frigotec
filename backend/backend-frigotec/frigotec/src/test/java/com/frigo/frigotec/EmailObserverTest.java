package com.frigo.frigotec;

import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.after;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** Padrão Observer: o ChamadoService publica o evento e o ouvinte envia o e-mail ao cliente (JavaMail simulado). */
@SpringBootTest(properties = {"spring.datasource.url=jdbc:h2:mem:frigotec_email_teste",
        "spring.sql.init.data-locations=classpath:data.sql,classpath:data-demo.sql"})
@AutoConfigureMockMvc
class EmailObserverTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private JavaMailSender mailSender;

    @BeforeEach
    void prepararEmailSimulado() {
        when(mailSender.createMimeMessage()).thenAnswer(invocacao -> new MimeMessage((Session) null));
    }

    private String token(String email) throws Exception {
        String corpo = mvc.perform(post("/usuarios/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"" + email + "\",\"senha\":\"senha123\"}"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        Matcher m = Pattern.compile("\"token\":\"([^\"]+)\"").matcher(corpo);
        assertTrue(m.find());
        return "Bearer " + m.group(1);
    }

    @Test
    void statusInvalidoNaoEnviaEmailEStatusValidoEnviaAoCliente() throws Exception {
        String davi = token("davi@frigotec.com");

        // chamado 1002 (da Maria) está em APROVADO_PELO_TECNICO: pular para APROVACAO_DO_SERVICO é recusado e nada é enviado
        mvc.perform(patch("/chamados/1002/status").param("novoStatus", "APROVACAO_DO_SERVICO").header("Authorization", davi))
                .andExpect(status().isConflict());
        verify(mailSender, after(800).never()).send(any(MimeMessage.class));

        // o próximo status da sequência é aceito, o evento é publicado e o ouvinte envia o e-mail depois do commit
        mvc.perform(patch("/chamados/1002/status").param("novoStatus", "CONTATO_DO_TECNICO").header("Authorization", davi))
                .andExpect(status().isOk());
        ArgumentCaptor<MimeMessage> enviado = ArgumentCaptor.forClass(MimeMessage.class);
        verify(mailSender, timeout(5000)).send(enviado.capture());
        assertEquals("Chamado PR00001002: Contato do técnico", enviado.getValue().getSubject());
        assertEquals("guimesquita.0612@gmail.com", enviado.getValue().getAllRecipients()[0].toString());
    }
}
