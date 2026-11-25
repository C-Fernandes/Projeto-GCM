import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Front-end de console da loja. Apenas consome a API REST; não contém regras de negócio.
 *
 * Executar (com a API rodando): java frontend/ConsoleApp.java [urlBase]
 * urlBase padrão: http://localhost:8080
 */
public class ConsoleApp {
    private static final HttpClient CLIENTE = HttpClient.newHttpClient();
    private static String base = "http://localhost:8080";
    private static Scanner entrada;

    public static void main(String[] args) {
        if (args.length > 0) base = args[0].replaceAll("/+$", "");
        entrada = new Scanner(System.in);
        while (true) {
            System.out.println();
            System.out.println("=== Loja (" + base + ") ===");
            System.out.println("1 - Cadastrar produto");
            System.out.println("2 - Consultar estoque");
            System.out.println("3 - Entrada de estoque");
            System.out.println("4 - Saída de estoque");
            System.out.println("5 - Venda");
            System.out.println("0 - Sair");
            String opcao = ler("Opção: ");
            if (opcao == null || opcao.equals("0")) return;
            try {
                switch (opcao) {
                    case "1" -> cadastrar();
                    case "2" -> consultar();
                    case "3" -> movimentar("entrada");
                    case "4" -> movimentar("saida");
                    case "5" -> vender();
                    default -> System.out.println("Opção inválida.");
                }
            } catch (IOException e) {
                System.out.println("Não foi possível conectar à API em " + base + ": " + e.getMessage());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    private static void cadastrar() throws IOException, InterruptedException {
        String codigo = ler("Código: ");
        String preco = ler("Preço (ex. 59.90): ");
        enviar("POST", "/loja/produto", "{\"codigo\":" + json(codigo) + ",\"preco\":" + json(preco) + "}");
    }

    private static void consultar() throws IOException, InterruptedException {
        String codigo = ler("Código: ");
        enviar("GET", "/loja/produto/" + caminho(codigo) + "/estoque", null);
    }

    private static void movimentar(String tipo) throws IOException, InterruptedException {
        String codigo = ler("Código: ");
        String quantidade = ler("Quantidade: ");
        enviar("PUT", "/loja/produto/" + caminho(codigo) + "/" + tipo, "{\"quantidade\":" + json(quantidade) + "}");
    }

    private static void vender() throws IOException, InterruptedException {
        List<String> itens = new ArrayList<>();
        System.out.println("Informe os itens. Deixe o código vazio para finalizar.");
        while (true) {
            String codigo = ler("Código do item " + (itens.size() + 1) + ": ");
            if (codigo == null || codigo.isBlank()) break;
            String quantidade = ler("Quantidade: ");
            itens.add("{\"codigo\":" + json(codigo) + ",\"quantidade\":" + json(quantidade) + "}");
        }
        enviar("POST", "/loja/venda", "{\"itens\":[" + String.join(",", itens) + "]}");
    }

    private static void enviar(String metodo, String caminho, String corpo) throws IOException, InterruptedException {
        HttpRequest.Builder requisicao = HttpRequest.newBuilder(URI.create(base + caminho));
        if (corpo == null) {
            requisicao.method(metodo, HttpRequest.BodyPublishers.noBody());
        } else {
            requisicao.header("Content-Type", "application/json").method(metodo, HttpRequest.BodyPublishers.ofString(corpo));
        }
        HttpResponse<String> resposta = CLIENTE.send(requisicao.build(), HttpResponse.BodyHandlers.ofString());
        System.out.println("HTTP " + resposta.statusCode());
        System.out.println(resposta.body());
    }

    /** Repassa números como estão e o resto como texto JSON; a validação fica a cargo da API. */
    private static String json(String valor) {
        if (valor == null || valor.isBlank()) return "null";
        String v = valor.trim();
        if (v.matches("-?\\d+(\\.\\d+)?")) return v;
        return "\"" + v.replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
    }

    private static String caminho(String valor) {
        return java.net.URLEncoder.encode(valor == null ? "" : valor.trim(), java.nio.charset.StandardCharsets.UTF_8);
    }

    private static String ler(String rotulo) {
        System.out.print(rotulo);
        return entrada.hasNextLine() ? entrada.nextLine() : null;
    }
}
