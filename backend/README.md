# Backend

API do Portal Frigotec em Java 21 + Spring Boot 4, dentro de `backend-frigotec/frigotec`.

- **Segurança:** login com token JWT e perfis CLIENTE, TECNICO e ADMIN (`config/SegurancaConfig`).
- **Camadas:** `controller` → `service` → `repository`, com `dto`, `mapper` e `entity`.
- **Banco:** H2 em memória. `data.sql` traz o admin inicial e o catálogo; `data-demo.sql` traz os dados de exemplo.
- **Erros:** respostas padronizadas (400, 401, 403, 404, 409, 422) em `exception/GlobalExceptionHandler`.
- **E-mail:** envio por eventos (observer) ao salvar orçamento.
- **Testes:** `./mvnw test`.

Configuração por variáveis de ambiente (`JWT_SECRET`, `SEED_DATA`, `SEED_MODE`, `MAIL_*`); nunca versionar senhas.

Como rodar e usuários de exemplo: veja o README da raiz.
