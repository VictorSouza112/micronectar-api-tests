package steps;

import model.LoginModel;
import model.MicroempreendedorCadastroModel;
import model.ProdutoCadastroModel;
import services.CadastroProdutoService;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import io.restassured.response.Response;
import org.junit.Assert;

import java.math.BigDecimal;
import java.util.Map;

public class CadastroProdutoSteps {

    private final CadastroProdutoService produtoService = new CadastroProdutoService();
    private ProdutoCadastroModel produtoModel;

    private static Long microempreendedorId;
    private static LoginModel loginModel;
    private static String token;

    @Dado("que um microempreendedor foi cadastrado com sucesso usando os dados:")
    public void que_um_microempreendedor_foi_cadastrado_com_sucesso_usando_os_dados(DataTable dataTable) {
        Map<String, String> data = dataTable.asMap();
        MicroempreendedorCadastroModel userModel = new MicroempreendedorCadastroModel();

        // Gera um e-mail e CNPJ únicos para garantir que o Contexto seja sempre executável
        String timestamp = String.valueOf(System.currentTimeMillis()).substring(8);
        String emailUnico = data.get("email").replace("@", "+" + timestamp + "@");
        String cnpjUnico = data.get("nrCnpj").substring(0, data.get("nrCnpj").length() - 2) + timestamp.substring(timestamp.length() - 2);

        userModel.setNome(data.get("nome"));
        userModel.setEmail(emailUnico);
        userModel.setSenha(data.get("senha"));
        userModel.setNrCnpj(cnpjUnico);

        // Armazena as credenciais para o passo de login
        loginModel = new LoginModel(emailUnico, data.get("senha"));

        // Executa o cadastro e armazena o ID do usuário criado
        Response responseCadastro = produtoService.cadastrarMicroempreendedor(userModel);
        Assert.assertEquals(201, responseCadastro.getStatusCode());
        microempreendedorId = responseCadastro.jsonPath().getLong("idUsuario");
    }

    @Dado("que o microempreendedor do contexto faz login para obter o token de autenticação")
    public void que_o_microempreendedor_do_contexto_faz_login() {
        token = produtoService.login(loginModel);
        Assert.assertNotNull("Token não foi gerado no login", token);
        Assert.assertFalse("Token está vazio", token.isEmpty());
    }

    @Dado("eu tenho os seguintes dados para um novo produto:")
    public void eu_tenho_os_seguintes_dados_para_um_novo_produto(DataTable dataTable) {
        Map<String, String> data = dataTable.asMap();
        this.produtoModel = new ProdutoCadastroModel();
        produtoModel.setNmProduto(data.get("nmProduto"));
        produtoModel.setVlProduto(new BigDecimal(data.get("vlProduto")));
        produtoModel.setQtProduto(Integer.parseInt(data.get("qtProduto")));
        produtoModel.setDsProduto(data.get("dsProduto"));
    }

    @Quando("eu enviar uma requisição POST para o endpoint de produtos")
    public void eu_enviar_uma_requisição_post_para_o_endpoint_de_produtos() {
        produtoService.adicionarProduto(token, microempreendedorId, produtoModel);
    }
}