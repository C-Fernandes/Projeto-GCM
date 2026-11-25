package br.edu.gcm;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@AutoConfigureMockMvc
class VendaControllerTest {
    @Autowired MockMvc mvc;

    @Test
    void vendeCalculaSubtotaisETotalEDaSaidaNoEstoque() throws Exception {
        cadastrarComEstoque(401, "59.90", 15);
        cadastrarComEstoque(402, "69.90", 8);
        vender("{\"itens\":[{\"codigo\":401,\"quantidade\":2},{\"codigo\":402,\"quantidade\":1}]}")
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"total\":189.70,\"itens\":["
                        + "{\"codigo\":401,\"quantidade\":2,\"subtotal\":119.80,\"estoque\":13},"
                        + "{\"codigo\":402,\"quantidade\":1,\"subtotal\":69.90,\"estoque\":7}]}", true));
        assertEstoque(401, 13);
        assertEstoque(402, 7);
    }

    @Test
    void vendaComProdutoInexistenteRetorna404SemAlterarEstoque() throws Exception {
        cadastrarComEstoque(403, "10", 5);
        vender("{\"itens\":[{\"codigo\":403,\"quantidade\":2},{\"codigo\":999995,\"quantidade\":1}]}")
                .andExpect(status().isNotFound()).andExpect(jsonPath("$.status").value(404));
        assertEstoque(403, 5);
    }

    @Test
    void vendaComEstoqueInsuficienteRetorna400SemAlterarNenhumEstoque() throws Exception {
        cadastrarComEstoque(404, "10", 5);
        cadastrarComEstoque(405, "10", 1);
        vender("{\"itens\":[{\"codigo\":404,\"quantidade\":2},{\"codigo\":405,\"quantidade\":2}]}")
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.status").value(400));
        assertEstoque(404, 5);
        assertEstoque(405, 1);
    }

    @Test
    void vendaSomaItensRepetidosDoMesmoProduto() throws Exception {
        cadastrarComEstoque(406, "2.50", 5);
        vender("{\"itens\":[{\"codigo\":406,\"quantidade\":3},{\"codigo\":406,\"quantidade\":3}]}")
                .andExpect(status().isBadRequest());
        assertEstoque(406, 5);
        vender("{\"itens\":[{\"codigo\":406,\"quantidade\":2},{\"codigo\":406,\"quantidade\":3}]}")
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"total\":12.50,\"itens\":["
                        + "{\"codigo\":406,\"quantidade\":2,\"subtotal\":5.00,\"estoque\":3},"
                        + "{\"codigo\":406,\"quantidade\":3,\"subtotal\":7.50,\"estoque\":0}]}", true));
        assertEstoque(406, 0);
    }

    @Test
    void vendaRejeitaListaAusenteVaziaOuItemInvalidoSemAlterarEstoque() throws Exception {
        cadastrarComEstoque(407, "10", 5);
        for (String body : new String[]{"{}", "{\"itens\":null}", "{\"itens\":[]}", "{\"itens\":\"407\"}", "{\"itens\":[null]}",
                "{\"itens\":[{}]}", "{\"itens\":[{\"codigo\":407}]}", "{\"itens\":[{\"quantidade\":1}]}",
                "{\"itens\":[{\"codigo\":\"407\",\"quantidade\":1}]}", "{\"itens\":[{\"codigo\":407,\"quantidade\":\"1\"}]}",
                "{\"itens\":[{\"codigo\":407,\"quantidade\":1.5}]}", "{\"itens\":[{\"codigo\":407,\"quantidade\":0}]}",
                "{\"itens\":[{\"codigo\":407,\"quantidade\":-1}]}", "{\"itens\":[{\"codigo\":407,\"quantidade\":1},{\"codigo\":407}]}",
                "[]", "{"}) {
            vender(body).andExpect(status().isBadRequest()).andExpect(jsonPath("$.status").value(400));
        }
        mvc.perform(post("/loja/venda").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
        assertEstoque(407, 5);
    }

    @Test
    void documentaVendaNoOpenApi() throws Exception {
        mvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paths['/loja/venda'].post.responses['201']").exists())
                .andExpect(jsonPath("$.paths['/loja/venda'].post.responses['400']").exists())
                .andExpect(jsonPath("$.paths['/loja/venda'].post.responses['404']").exists());
    }

    private void cadastrarComEstoque(int codigo, String preco, int estoque) throws Exception {
        mvc.perform(post("/loja/produto").contentType(MediaType.APPLICATION_JSON)
                .content("{\"codigo\":" + codigo + ",\"preco\":" + preco + "}")).andExpect(status().isCreated());
        mvc.perform(put("/loja/produto/" + codigo + "/entrada").contentType(MediaType.APPLICATION_JSON)
                .content("{\"quantidade\":" + estoque + "}")).andExpect(status().isOk());
    }

    private ResultActions vender(String body) throws Exception {
        return mvc.perform(post("/loja/venda").contentType(MediaType.APPLICATION_JSON).content(body));
    }

    private void assertEstoque(int codigo, int estoque) throws Exception {
        mvc.perform(get("/loja/produto/" + codigo + "/estoque"))
                .andExpect(status().isOk()).andExpect(content().json("{\"codigo\":" + codigo + ",\"estoque\":" + estoque + "}", true));
    }
}
