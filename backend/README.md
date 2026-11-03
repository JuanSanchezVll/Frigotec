# Backend

API do Portal Frigotec em Java 21 + Spring Boot 4, dentro de `backend-frigotec/frigotec`.

- **Segurança:** login com token JWT e perfis CLIENTE, TECNICO e ADMIN (`config/SegurancaConfig`).
- **Camadas:** `controller` → `service` → `repository`, com `dto`, `mapper` e `entity`.
- **Banco:** H2 em memória. `data.sql` traz o admin inicial e o catálogo; `data-demo.sql` traz os dados de exemplo.
- **Erros:** respostas padronizadas (400, 401, 403, 404, 409, 422) em `exception/GlobalExceptionHandler`.
- **E-mail:** envio assíncrono por eventos (observer, após o commit) ao salvar orçamento e ao mudar o status do chamado.
- **API:** Swagger UI em `/swagger-ui.html`; YAML OpenAPI público em `/v3/api-docs.yaml` (cópia em `docs/openapi.yaml`). O botão Authorize aceita o token JWT.
- **Upload:** fotos/vídeo do chamado e documentos da unidade em disco (`UPLOAD_DIR`, padrão `./uploads`); limites em `UPLOAD_MAX_BYTES`, `UPLOAD_MAX_FOTOS`, `UPLOAD_MAX_VIDEOS` e `UPLOAD_MAX_DOCUMENTOS`.
- **Banco persistente:** `SPRING_PROFILES_ACTIVE=mysql` (variáveis `DB_URL`, `DB_USER`, `DB_PASSWORD`); MySQL local com `docker compose up -d` na raiz.
- **CI:** `.github/workflows/ci.yml` roda `./mvnw test`, `npm run lint` e `npm run build` em todo push e PR.
- **Testes:** `./mvnw test` (25 testes de integração).

Configuração por variáveis de ambiente (`JWT_SECRET`, `SEED_DATA`, `SEED_MODE`, `MAIL_*`); nunca versionar senhas.

Como rodar e usuários de exemplo: veja o README da raiz.
