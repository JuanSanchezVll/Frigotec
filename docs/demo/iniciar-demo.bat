@echo off
rem Sobe o Portal Frigotec para a apresentacao: back-end (8080), front-end (5173) e, se houver Python, o servidor de e-mail de teste.
rem Basta dar dois cliques. Para encerrar, feche as janelas que se abrirem.
cd /d "%~dp0..\.."

start "Back-end Frigotec (8080)" cmd /k "cd backend\backend-frigotec\frigotec && mvnw.cmd spring-boot:run"
start "Front-end Frigotec (5173)" cmd /k "cd frontend && npm run dev"
where python >nul 2>nul && start "E-mails de teste (1025)" cmd /k "python docs\demo\smtp_sink.py docs\demo\emails.log"

echo Aguardando o back-end subir (cerca de 40 segundos)...
timeout /t 40 /nobreak >nul

start "" http://localhost:5173
start "" http://localhost:8080/swagger-ui.html
start "" http://localhost:8080/h2-console
echo Pronto. Roteiro: docs\roteiro-de-apresentacao.md  |  Consultas do H2: docs\demo\consultas-h2.sql
