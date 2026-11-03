package com.frigo.frigotec.service;

import com.frigo.frigotec.exception.RegraDeNegocioException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * Grava e lê os arquivos em disco. O nome em disco é gerado aqui (UUID + extensão do tipo verificado), então o nome que
 * o cliente envia nunca vira caminho: não há como escapar da pasta de uploads.
 */
@Service
public class ArmazenamentoService {

    private final Path raiz;

    public ArmazenamentoService(@Value("${app.upload.dir:./uploads}") String diretorio) {
        this.raiz = Paths.get(diretorio).toAbsolutePath().normalize();
    }

    /** Confere os primeiros bytes do arquivo: o tipo informado precisa bater com o conteúdo (JPG, PNG, PDF ou MP4). */
    public static boolean assinaturaConfere(String tipo, byte[] b) {
        if (b == null || b.length < 12) return false;
        return switch (tipo) {
            case "image/jpeg" -> (b[0] & 0xFF) == 0xFF && (b[1] & 0xFF) == 0xD8 && (b[2] & 0xFF) == 0xFF;
            case "image/png" -> (b[0] & 0xFF) == 0x89 && b[1] == 'P' && b[2] == 'N' && b[3] == 'G';
            case "application/pdf" -> b[0] == '%' && b[1] == 'P' && b[2] == 'D' && b[3] == 'F';
            case "video/mp4" -> b[4] == 'f' && b[5] == 't' && b[6] == 'y' && b[7] == 'p';
            default -> false;
        };
    }

    public static String extensao(String tipo) {
        return switch (tipo) {
            case "image/jpeg" -> "jpg";
            case "image/png" -> "png";
            case "application/pdf" -> "pdf";
            case "video/mp4" -> "mp4";
            default -> "bin";
        };
    }

    public byte[] cabecalho(MultipartFile arquivo) {
        try (InputStream in = arquivo.getInputStream()) {
            return in.readNBytes(16);
        } catch (IOException e) {
            throw new RegraDeNegocioException("Não foi possível ler o arquivo enviado.");
        }
    }

    /** Devolve o caminho relativo gravado (ex.: chamados/12/uuid.jpg). */
    public String gravar(String pasta, MultipartFile arquivo, String tipo) {
        try {
            Path destinoPasta = raiz.resolve(pasta).normalize();
            if (!destinoPasta.startsWith(raiz)) throw new RegraDeNegocioException("Destino inválido.");
            Files.createDirectories(destinoPasta);
            String nome = UUID.randomUUID() + "." + extensao(tipo);
            try (InputStream in = arquivo.getInputStream()) {
                Files.copy(in, destinoPasta.resolve(nome));
            }
            return pasta + "/" + nome;
        } catch (IOException e) {
            throw new IllegalStateException("Falha ao gravar o arquivo.", e);
        }
    }

    public void apagar(String caminho) {
        try {
            Path p = raiz.resolve(caminho).normalize();
            if (p.startsWith(raiz)) Files.deleteIfExists(p);
        } catch (IOException ignorado) {
            // arquivo órfão não impede a regra de negócio
        }
    }

    public Resource ler(String caminho) {
        Path p = raiz.resolve(caminho).normalize();
        if (!p.startsWith(raiz) || !Files.exists(p)) return null;
        return new FileSystemResource(p);
    }
}
