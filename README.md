# Projeto-GCM

API Java da disciplina de Gerenciamento de Configuração e Mudanças. O armazenamento é em memória e os produtos são perdidos ao encerrar a aplicação.

## Requisitos

- JDK 21
- Maven 3.9 ou superior

## Executar

```sh
mvn spring-boot:run
```

A API fica disponível em `http://localhost:8080`.

Para cadastrar um produto:

```sh
curl -i -X POST http://localhost:8080/loja/produto -H "Content-Type: application/json" -d '{"codigo":1,"preco":12.50}'
```

O retorno é `201` com `codigo`, `preco` e `estoque` inicial igual a zero. Campos obrigatórios ausentes ou com tipo inválido retornam `400`; código já cadastrado retorna `409`.

Para consultar o estoque:

```sh
curl -i http://localhost:8080/loja/produto/1/estoque
```

O retorno é `200` com `codigo` e `estoque`, ou `404` se o produto não existir.

## Testar

```sh
mvn verify
```

## Documentação da API

Com a aplicação em execução, acesse http://localhost:8080/swagger-ui.html para consultar e testar os endpoints. A descrição OpenAPI em JSON está em http://localhost:8080/v3/api-docs. Erros retornam um objeto com `status` e `mensagem`.
