import random, json, urllib.request, urllib.error, datetime, uuid, struct, zlib, io

B = "http://localhost:5173/api"


def req(method, path, token=None, body=None, files=None):
    data, headers = None, {}
    if token:
        headers["Authorization"] = "Bearer " + token
    if files:
        bd = "----b" + uuid.uuid4().hex
        buf = io.BytesIO()
        for nome, conteudo, tipo in files:
            buf.write(("--%s\r\nContent-Disposition: form-data; name=\"arquivos\"; filename=\"%s\"\r\nContent-Type: %s\r\n\r\n" % (bd, nome, tipo)).encode())
            buf.write(conteudo)
            buf.write(b"\r\n")
        buf.write(("--%s--\r\n" % bd).encode())
        data = buf.getvalue()
        headers["Content-Type"] = "multipart/form-data; boundary=" + bd
    elif body is not None:
        data = json.dumps(body).encode()
        headers["Content-Type"] = "application/json"
    r = urllib.request.Request(B + path, data=data, headers=headers, method=method)
    try:
        with urllib.request.urlopen(r) as resp:
            txt = resp.read().decode() or "null"
            return resp.status, json.loads(txt) if txt.strip().startswith(("{", "[", "n")) else txt
    except urllib.error.HTTPError as e:
        txt = e.read().decode()
        try:
            return e.code, json.loads(txt)
        except Exception:
            return e.code, txt


def png():
    def ch(t, d):
        c = struct.pack(">I", len(d)) + t + d
        return c + struct.pack(">I", zlib.crc32(t + d) & 0xFFFFFFFF)
    return b"\x89PNG\r\n\x1a\n" + ch(b"IHDR", struct.pack(">IIBBBBB", 1, 1, 8, 2, 0, 0, 0)) + ch(b"IDAT", zlib.compress(b"\x00\xff\x00\x00")) + ch(b"IEND", b"")


def passo(nome, esperado, res):
    ok = res[0] in esperado
    print(("OK  " if ok else "ERRO"), nome, "->", res[0], "" if ok else str(res[1])[:200])
    return res[1]


def cpf():
    d=[random.randint(0,9) for _ in range(9)]
    for n in (9,10):
        r=(sum(d[i]*(n+1-i) for i in range(n))*10)%11%10; d.append(r)
    return "".join(map(str,d))


SUF = str(random.randint(1000, 9999))
CPF_C, CPF_T = cpf(), cpf()
end = {"estado": "SP", "cidade": "Sao Paulo", "rua": "Avenida Paulista", "numero": "1578", "cep": "01310100"}
print("== 1. Cadastro de cliente pessoa fisica ==")
passo("cadastrar cliente", (201,), req("POST", "/usuarios", body={"nomeCompleto": "Carlos Eduardo Lima", "email": "carlos%s@exemplo.com" % SUF, "senha": "teste123", "cpf": CPF_C, "telefone": "1133334444", "celular": "11988776655", "endereco": end}))
print("== 2. Login do cliente ==")
r = passo("login cliente", (200,), req("POST", "/usuarios/login", body={"email": "carlos%s@exemplo.com" % SUF, "senha": "teste123"}))
tc, cid = r["token"], r["usuario"]["id"]
print("== 3. Admin cria o tecnico ==")
ta = passo("login admin", (200,), req("POST", "/usuarios/login", body={"email": "admin@frigotec.com", "senha": "senha123"}))["token"]
passo("cadastrar tecnico", (201,), req("POST", "/usuarios/colaboradores", ta, {"nomeCompleto": "Rafael Tecnico Teste", "email": "rafael%s@frigotec.com" % SUF, "senha": "teste123", "cpf": CPF_T, "celular": "11977665544", "perfil": "TECNICO", "endereco": end}))
tt = passo("login tecnico", (200,), req("POST", "/usuarios/login", body={"email": "rafael%s@frigotec.com" % SUF, "senha": "teste123"}))["token"]
print("== 4. Equipamento FrigoPro com garantia e nota fiscal ==")
fabs = req("GET", "/fabricantes")[1]
fp = [f for f in fabs if f.get("propria")][0]
modelos = req("GET", "/fabricantes/%s/equipamentos" % fp["id"], tc)[1]
eq = passo("cadastrar equipamento", (201,), req("POST", "/equipamentos/" + cid, tc, {"equipamentoId": modelos[0]["id"], "numeroSerie": "TESTE-0001", "tipoGarantia": "Garantia comum", "dataCompra": "2026-10-01"}))
uid = eq["equipamentoUsuarioId"]
passo("upload da nota fiscal", (201,), req("POST", "/equipamentos/%s/documentos" % uid, tc, files=[("nota.png", png(), "image/png")]))
print("== 5. CRUD ==")
passo("ler (GET)", (200,), req("GET", "/usuarios/%s/equipamentos" % cid, tc))
passo("atualizar (PATCH)", (200,), req("PATCH", "/equipamentos/%s" % uid, tc, {"numeroSerie": "TESTE-0002"}))
passo("excluir (DELETE) outro equipamento", (204,), req("DELETE", "/equipamentos/%s" % passo("criar equipamento descartavel", (201,), req("POST", "/equipamentos/" + cid, tc, {"equipamentoId": modelos[0]["id"], "numeroSerie": "DESCARTE-1", "tipoGarantia": "Garantia comum", "dataCompra": "2026-10-02"}))["equipamentoUsuarioId"], tc))
print("== 6. Chamado com foto ==")
ch = passo("abrir chamado", (201,), req("POST", "/chamados", tc, {"equipamentoUsuarioId": uid, "problema": "Temperatura irregular", "descricao": "A chapa esquenta so de um lado"}))
cd, prot = ch["id"], ch["protocolo"]
print("   protocolo:", prot)
passo("anexar foto", (201,), req("POST", "/chamados/%s/anexos" % cd, tc, files=[("defeito.png", png(), "image/png")]))
print("== 7. Excluir equipamento com chamado deve ser recusado ==")
passo("delete com chamado -> 409", (409,), req("DELETE", "/equipamentos/%s" % uid, tc))
print("== 8. Tecnico atende ==")
passo("dashboard do tecnico", (200,), req("GET", "/dashboard/tecnico", tt))
passo("atribuir a mim", (200, 201), req("POST", "/chamados/%s/atribuicao" % cd, tt, {}))
passo("contato do tecnico", (200,), req("PATCH", "/chamados/%s/status?novoStatus=CONTATO_DO_TECNICO" % cd, tt))
passo("cliente aprova o servico", (200,), req("PATCH", "/chamados/%s/status?novoStatus=APROVACAO_DO_SERVICO" % cd, tc))
amanha = (datetime.datetime.now() + datetime.timedelta(days=2)).strftime("%Y-%m-%dT10:00:00")
passo("agendar visita", (200,), req("PATCH", "/chamados/%s/visita" % cd, tt, {"visitaAgendadaEm": amanha}))
passo("encerrar chamado", (200, 201), req("POST", "/chamados/%s/encerramento" % cd, tt, {"motivo": "Atendimento concluido", "descricao": "Termostato ajustado"}))
print("== 9. Cliente ve o status final ==")
fim = req("GET", "/chamados/%s" % cd, tc)[1]
print("   status atual:", fim["statusAtual"])
