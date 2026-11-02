# Backend

API do Portal Frigotec em Java + Spring Boot, dentro de `backend-frigotec/frigotec`.

- Banco H2 em memória, populado por `src/main/resources/data.sql`.
- Controllers: Usuário, Equipamento, Fabricante, Orçamento e Chamado.
- Envio de e-mail por eventos (observer) ao salvar orçamento e alterar status do chamado.
- Configuração de e-mail por variáveis de ambiente; nunca versionar senhas.

Como rodar: veja o README da raiz.
