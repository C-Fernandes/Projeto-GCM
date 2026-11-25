# Notas de versão

## rc-1.1 — Versão 1 (candidata para testes)

Baseline da versão 1, considerada finalizada e pronta para ser testada (não para produção). Estabilização é feita no branch `release/1.0`: apenas correções de defeitos entram aqui, cada uma com sua Issue. Novas funcionalidades continuam em `feature/*` a partir de `develop`.

### Funcionalidades

| Operação | Endpoint | Issue | PR |
|---|---|---|---|
| Configuração e modelagem inicial | — | #1 | #9 |
| Cadastrar Produto | `POST /loja/produto` | #2 | #9 |
| Consultar Estoque | `GET /loja/produto/{codigo}/estoque` | #3 | #9 |
| Entrada de Estoque | `PUT /loja/produto/{codigo}/entrada` | #4 | #10 |
| Saída de Estoque | `PUT /loja/produto/{codigo}/saida` | #5 | #11 |
| Venda | `POST /loja/venda` | #6 | #12 |
| Testes e validação | — | #7 | #14 |
| Front-end Console | `frontend/ConsoleApp.java` | #13 | #15 |

### Arquitetura

- Camada de serviço (`ProdutoService`): todas as regras de negócio, testável sem a API.
- API REST (`controller`): só repassa as requisições para a camada de serviço.
- Front-end de console: consome a API REST.

### Testes

`mvn verify`: 41 testes, 0 falhas (17 unitários da camada de serviço, 24 da API).
