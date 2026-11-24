package br.edu.gcm;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class ProdutoControllerTest {
    @Autowired MockMvc mvc;

    @Test
    void cadastraEConsultaEstoque() throws Exception {
        mvc.perform(post("/loja/produto").contentType(MediaType.APPLICATION_JSON)
                .content("{\"codigo\":101,\"preco\":12.50}"))
                .andExpect(status().isCreated()).andExpect(content().json("{\"codigo\":101,\"preco\":12.50,\"estoque\":0}", true));
        mvc.perform(get("/loja/produto/101/estoque"))
                .andExpect(status().isOk()).andExpect(content().json("{\"codigo\":101,\"estoque\":0}", true));
    }

    @Test
    void rejeitaCodigoDuplicado() throws Exception {
        String produto = "{\"codigo\":102,\"preco\":10}";
        mvc.perform(post("/loja/produto").contentType(MediaType.APPLICATION_JSON).content(produto)).andExpect(status().isCreated());
        mvc.perform(post("/loja/produto").contentType(MediaType.APPLICATION_JSON).content(produto)).andExpect(status().isConflict());
    }

    @Test
    void rejeitaCamposAusentesOuInvalidos() throws Exception {
        for (String body : new String[]{"{}", "{\"codigo\":1}", "{\"preco\":1}", "{\"codigo\":null,\"preco\":1}",
                "{\"codigo\":\"1\",\"preco\":1}", "{\"codigo\":1.5,\"preco\":1}",
                "{\"codigo\":2147483648,\"preco\":1}", "{\"codigo\":1,\"preco\":\"1\"}",
                "{\"codigo\":1,\"preco\":null}", "[]", "{"}) {
            mvc.perform(post("/loja/produto").contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isBadRequest());
        }
        mvc.perform(post("/loja/produto").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    @Test
    void produtoInexistenteRetorna404() throws Exception {
        mvc.perform(get("/loja/produto/999999/estoque")).andExpect(status().isNotFound());
    }
    @Test
    void retornaErroEstruturadoParaValidacaoEConflito() throws Exception {
        mvc.perform(post("/loja/produto").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.mensagem").isNotEmpty());
        String produto = "{\"codigo\":103,\"preco\":10}";
        mvc.perform(post("/loja/produto").contentType(MediaType.APPLICATION_JSON).content(produto)).andExpect(status().isCreated());
        mvc.perform(post("/loja/produto").contentType(MediaType.APPLICATION_JSON).content(produto))
                .andExpect(status().isConflict()).andExpect(jsonPath("$.status").value(409));
        mvc.perform(get("/loja/produto/999998/estoque"))
                .andExpect(status().isNotFound()).andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void documentaAsOperacoesNoOpenApi() throws Exception {
        mvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paths['/loja/produto'].post.responses['201']").exists())
                .andExpect(jsonPath("$.paths['/loja/produto'].post.responses['400']").exists())
                .andExpect(jsonPath("$.paths['/loja/produto'].post.responses['409']").exists())
                .andExpect(jsonPath("$.paths['/loja/produto/{codigo}/estoque'].get.responses['404']").exists());
        mvc.perform(get("/swagger-ui/index.html")).andExpect(status().isOk());
    }
    @Test
    void textosDaApiPreservamAcentuacao() throws Exception {
        mvc.perform(post("/loja/produto").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(jsonPath("$.mensagem").value("Requisi\u00e7\u00e3o inv\u00e1lida: informe os campos obrigat\u00f3rios com tipos v\u00e1lidos."));
        mvc.perform(get("/v3/api-docs"))
                .andExpect(jsonPath("$.components.schemas.ProdutoCadastroDTO.properties.codigo.description")
                        .value("C\u00f3digo inteiro do produto"));
    }
}
