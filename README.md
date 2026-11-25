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

Para dar entrada no estoque:

```sh
curl -i -X PUT http://localhost:8080/loja/produto/1/entrada -H "Content-Type: application/json" -d '{"quantidade":10}'
```

O retorno é `200` com `codigo` e o `estoque` já somado. Quantidade ausente, não inteira ou menor que 1 retorna `400`. Produto inexistente retorna `404`.

Para dar saída no estoque:

```sh
curl -i -X PUT http://localhost:8080/loja/produto/1/saida -H "Content-Type: application/json" -d '{"quantidade":4}'
```

O retorno é `200` com `codigo` e o `estoque` já descontado. Quantidade ausente, não inteira, menor que 1 ou maior que o estoque atual retorna `400`. Produto inexistente retorna `404`. Em qualquer erro, o estoque fica como estava.

Para registrar uma venda:

```sh
curl -i -X POST http://localhost:8080/loja/venda -H "Content-Type: application/json" -d '{"itens":[{"codigo":1,"quantidade":2}]}'
```

O retorno é `201` com o `total` da venda e, para cada item, `codigo`, `quantidade`, `subtotal` (`preco × quantidade`) e o `estoque` depois da saída. Lista de itens ausente ou vazia, item com campo inválido ou estoque insuficiente retorna `400`. Produto inexistente retorna `404`. A venda é tudo ou nada: se um item der erro, nenhum estoque muda.

## Front-end (console)

Com a API em execução, em outro terminal:

```sh
java frontend/ConsoleApp.java
```

O menu oferece as cinco operações e mostra o código HTTP e a resposta da API. Para outra URL: `java frontend/ConsoleApp.java http://host:porta`. O console só consome a API REST; as regras ficam na camada de serviço.

## Testar

```sh
mvn verify
```

- `ProdutoServiceTest`: testes unitários da camada de serviço, chamando as cinco operações direto, sem a API.
- `ProdutoControllerTest` e `VendaControllerTest`: testes da API REST, cobrindo sucesso, códigos `200`, `201`, `400`, `404` e `409`, estoque após cada operação e atomicidade da venda.

## Documentação da API

Com a aplicação em execução, acesse http://localhost:8080/swagger-ui.html para consultar e testar os endpoints. A descrição OpenAPI em JSON está em http://localhost:8080/v3/api-docs. Erros retornam um objeto com `status` e `mensagem`.
