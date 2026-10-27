
package com.frigo.frigotec.service;

import com.frigo.frigotec.entity.MensagemEmail;
import com.frigo.frigotec.entity.StatusEnvio;
import com.frigo.frigotec.repository.MensagemEmailRepository;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.Locale;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;
    private final MensagemEmailRepository repository;
    private final String from;
    private final String siteUrl;

    public EmailService(JavaMailSender mailSender,
                        MensagemEmailRepository repository,
                        @Value("${app.mail.from}") String from,
                        @Value("${app.site-url}") String siteUrl) {
        this.mailSender = mailSender;
        this.repository = repository;
        this.from = from;
        this.siteUrl = siteUrl;
    }

    @Async
    public void enviarOrcamento(Long mensagemId, BigDecimal valor) {
        MensagemEmail mensagem = repository.findComDetalhesById(mensagemId).orElse(null);
        if (mensagem == null) {
            log.error("MensagemEmail {} não encontrada; e-mail não enviado", mensagemId);
            return;
        }

        String valorFormatado = NumberFormat.getCurrencyInstance(Locale.of("pt", "BR")).format(valor);
        String link = siteUrl + "/chamados/" + mensagem.getChamado().getId();
        String html = """
                <p>Olá, %s!</p>
                <p>O orçamento do conserto do seu equipamento ficou em <strong>%s</strong>.</p>
                <p><a href="%s">Acesse o portal para ver os detalhes e aprovar ou recusar.</a></p>
                """.formatted(
                HtmlUtils.htmlEscape(mensagem.getUsuario().getNomeCompleto()),
                valorFormatado,
                link);

        try {
            MimeMessage mime = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mime, true, "UTF-8");
            helper.setFrom(from);
            helper.setTo(mensagem.getUsuario().getEmail());
            helper.setSubject(mensagem.getAssunto());
            helper.setText(html, true);
            mailSender.send(mime);
            mensagem.setStatus(StatusEnvio.ENVIADO);
        } catch (Exception ex) {
            log.error("Falha ao enviar e-mail da mensagem {}", mensagemId, ex);
            mensagem.setStatus(StatusEnvio.ERRO);
        }
        mensagem.setDataEnvio(LocalDateTime.now());
        repository.save(mensagem);
    }
}