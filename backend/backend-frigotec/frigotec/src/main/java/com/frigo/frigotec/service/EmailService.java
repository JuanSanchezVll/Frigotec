
package com.frigo.frigotec.service;

import com.frigo.frigotec.entity.Chamado;
import com.frigo.frigotec.entity.MensagemEmail;
import com.frigo.frigotec.repository.ChamadoRepository;
import com.frigo.frigotec.util.StatusChamadoEnum;
import com.frigo.frigotec.util.StatusEnvio;
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
    private final ChamadoRepository chamadoRepository;
    private final String from;
    private final String siteUrl;

    public EmailService(JavaMailSender mailSender,
                        MensagemEmailRepository repository,
                        ChamadoRepository chamadoRepository,
                        @Value("${app.mail.from}") String from,
                        @Value("${app.site-url}") String siteUrl) {
        this.mailSender = mailSender;
        this.repository = repository;
        this.chamadoRepository = chamadoRepository;
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
        String html = """
                <p>Olá, %s!</p>
                <p>O orçamento do conserto do seu equipamento ficou em <strong>%s</strong>.</p>
                <p><a href="%s">Acesse o portal para ver os detalhes e aprovar ou recusar.</a></p>
                """.formatted(
                HtmlUtils.htmlEscape(mensagem.getUsuario().getNomeCompleto()),
                valorFormatado,
                linkDoChamado(mensagem.getChamado()));
        enviar(mensagem, html);
    }

    /**
     * SDD RN-NOT-01 a 03: aviso ao cliente a cada mudança de status do chamado (Observer: o ChamadoService só publica
     * o evento). Usa o nome e a descrição do status e o protocolo. Falha de envio não desfaz nada: fica ERRO em MensagemEmail.
     */
    @Async
    public void enviarMudancaStatus(Long chamadoId, StatusChamadoEnum novoStatus) {
        Chamado chamado = chamadoRepository.findById(chamadoId).orElse(null);
        if (chamado == null || chamado.getUsuario() == null || chamado.getUsuario().getEmail() == null) {
            log.warn("Chamado {} sem cliente com e-mail; aviso de status não enviado", chamadoId);
            return;
        }
        MensagemEmail mensagem = new MensagemEmail();
        mensagem.setChamado(chamado);
        mensagem.setUsuario(chamado.getUsuario());
        mensagem.setAssunto("Chamado " + chamado.getProtocolo() + ": " + novoStatus.getNome());
        mensagem.setDescricao("Mudança de status: " + novoStatus.getNome());
        mensagem.setStatus(StatusEnvio.PENDENTE);
        mensagem = repository.save(mensagem);

        String html = """
                <p>Olá, %s!</p>
                <p>O seu chamado <strong>%s</strong> mudou para <strong>%s</strong>.</p>
                <p>%s</p>
                <p><a href="%s">Acompanhe o atendimento no portal.</a></p>
                """.formatted(
                HtmlUtils.htmlEscape(chamado.getUsuario().getNomeCompleto()),
                HtmlUtils.htmlEscape(chamado.getProtocolo()),
                HtmlUtils.htmlEscape(novoStatus.getNome()),
                HtmlUtils.htmlEscape(novoStatus.getDescricao()),
                linkDoChamado(chamado));
        enviar(mensagem, html);
    }

    /** A tela do portal abre o chamado pelo protocolo (PR00000000), não pelo id numérico. */
    private String linkDoChamado(Chamado chamado) {
        return siteUrl + "/chamados/" + chamado.getProtocolo();
    }

    private void enviar(MensagemEmail mensagem, String html) {
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
            log.error("Falha ao enviar e-mail da mensagem {}", mensagem.getId(), ex);
            mensagem.setStatus(StatusEnvio.ERRO);
        }
        mensagem.setDataEnvio(LocalDateTime.now());
        repository.save(mensagem);
    }
}
