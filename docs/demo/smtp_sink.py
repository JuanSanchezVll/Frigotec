"""Servidor SMTP de demonstracao: aceita qualquer e-mail na porta 1025 e grava as mensagens no arquivo informado. Uso: python docs/demo/smtp_sink.py docs/demo/emails.log"""
import asyncio
import datetime
import sys

LOG = sys.argv[1]


async def handle(reader, writer):
    writer.write(b"220 sink ESMTP pronto\r\n")
    await writer.drain()
    em_dados, buf = False, []
    while True:
        linha = await reader.readline()
        if not linha:
            break
        if em_dados:
            if linha == b".\r\n":
                em_dados = False
                with open(LOG, "ab") as f:
                    f.write(b"=== MENSAGEM " + datetime.datetime.now().isoformat().encode() + b"\n" + b"".join(buf) + b"\n")
                buf = []
                writer.write(b"250 OK mensagem recebida\r\n")
            else:
                buf.append(linha)
        else:
            cmd = linha.decode(errors="ignore").strip().upper()
            if cmd.startswith(("EHLO", "HELO")):
                writer.write(b"250-sink\r\n250 8BITMIME\r\n")
            elif cmd.startswith("DATA"):
                writer.write(b"354 envie os dados, termine com <CRLF>.<CRLF>\r\n")
                em_dados = True
            elif cmd.startswith("QUIT"):
                writer.write(b"221 tchau\r\n")
                await writer.drain()
                break
            else:
                writer.write(b"250 OK\r\n")
        await writer.drain()
    writer.close()


async def main():
    srv = await asyncio.start_server(handle, None, 1025)
    async with srv:
        await srv.serve_forever()


asyncio.run(main())
