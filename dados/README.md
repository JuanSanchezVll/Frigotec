# Catálogo de equipamentos e marcas

Transcrição da relação que a Frigotec enviou: **20 tipos de equipamento**,
**29 marcas** e **64 combinações** entre eles.

| Arquivo | Uso |
|---|---|
| `catalogo-equipamentos.json` | Estruturado, com o vínculo tipo↔marca e o dado identificador de cada tipo. É o que vai virar seed do banco. |
| `catalogo-equipamentos.csv` | Mesma informação em formato plano, uma linha por par tipo×marca. Serve para conferência. |

## O campo que importa

Cada tipo de equipamento define **qual dado a Frigotec precisa** para identificá-lo:

- **Etiqueta / nº de série** — 15 tipos
- **Modelo** — 4 tipos (cutter, fogão de indução, conservador de frituras, máquina de café)
- **Marca** — 1 tipo (refresqueira)

É isso que torna o formulário de abertura de chamado dinâmico.

## Importante

Esta lista **vai mudar**. A Frigotec disse isso por escrito. Por isso o catálogo
precisa ser dado administrável no sistema, não constante em código.

A transcrição foi feita a partir de um PDF e ainda não foi conferida com o cliente —
vale validar a grafia das marcas antes de usar como seed definitivo.
