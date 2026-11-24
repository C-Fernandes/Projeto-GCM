# Projeto-GCM

API Java da disciplina de Gerenciamento de Configura??o e Mudan?as. O armazenamento ? em mem?ria e os produtos s?o perdidos ao encerrar a aplica??o.

## Requisitos

- JDK 21
- Maven 3.9 ou superior

## Executar

```sh
mvn spring-boot:run
```

A API fica dispon?vel em `http://localhost:8080`.

Para cadastrar um produto:

```sh
curl -i -X POST http://localhost:8080/loja/produto -H "Content-Type: application/json" -d '{"codigo":1,"preco":12.50}'
```

O retorno ? `201` com `codigo`, `preco` e `estoque` inicial igual a zero. Campos obrigat?rios ausentes ou com tipo inv?lido retornam `400`; c?digo j? cadastrado retorna `409`.

Para consultar o estoque:

```sh
curl -i http://localhost:8080/loja/produto/1/estoque
```

O retorno ? `200` com `codigo` e `estoque`, ou `404` se o produto n?o existir.

## Testar

```sh
mvn verify
```

## Documenta??o da API

Com a aplica??o em execu??o, acesse http://localhost:8080/swagger-ui.html para consultar e testar os endpoints. A descri??o OpenAPI em JSON est? em http://localhost:8080/v3/api-docs. Erros retornam um objeto com `status` e `mensagem`.
