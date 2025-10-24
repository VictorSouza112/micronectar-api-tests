package steps;

import model.MicroempreendedorCadastroModel;
import services.CadastroUsuarioService;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.networknt.schema.ValidationMessage;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.E;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import org.junit.Assert;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class CadastroUsuarioSteps {

    private final CadastroUsuarioService cadastroUsuarioService = new CadastroUsuarioService();
    private MicroempreendedorCadastroModel microempreendedorModel;
    private String emailExistente;

    @Dado("que os dados para um novo microempreendedor são gerados dinamicamente com base em:")
    public void que_os_dados_sao_gerados_dinamicamente(DataTable dataTable) {
        this.microempreendedorModel = new MicroempreendedorCadastroModel();
        Map<String, String> data = dataTable.asMap(String.class, String.class);

        // Gera dados únicos
        String timestamp = String.valueOf(System.currentTimeMillis()).substring(8);
        this.emailExistente = data.get("email").replace("@", "+" + timestamp + "@"); // Armazena o email que será usado
        String cnpjBase = data.get("nrCnpj").substring(0, data.get("nrCnpj").length() - 2);
        String cnpjUnico = cnpjBase + timestamp.substring(timestamp.length() - 2);

        // Popula o modelo
        microempreendedorModel.setNome(data.get("nome"));
        microempreendedorModel.setEmail(this.emailExistente);
        microempreendedorModel.setSenha(data.get("senha"));
        microempreendedorModel.setNrCnpj(cnpjUnico);
        microempreendedorModel.setNmFundador(data.get("nmFundador"));
    }

    @Dado("que um microempreendedor já foi cadastrado com os seguintes dados:")
    public void que_um_microempreendedor_ja_foi_cadastrado_com_os_seguintes_dados(DataTable dataTable) {
        // Passo 1: Preparar os dados (sem dinamismo aqui)
        this.microempreendedorModel = new MicroempreendedorCadastroModel();
        Map<String, String> data = dataTable.asMap(String.class, String.class);

        this.emailExistente = data.get("email"); // Armazena o e-mail que vamos reutilizar

        microempreendedorModel.setNome(data.get("nome"));
        microempreendedorModel.setEmail(this.emailExistente);
        microempreendedorModel.setSenha(data.get("senha"));
        microempreendedorModel.setNrCnpj(data.get("nrCnpj"));
        microempreendedorModel.setNmFundador(data.get("nmFundador"));

        // Passo 2: Executar o cadastro para garantir que o usuário existe
        cadastroUsuarioService.cadastrarMicroempreendedor(this.microempreendedorModel, "/api/auth/register/microempreendedor");
    }

    @Quando("eu tento cadastrar um novo microempreendedor com o mesmo e-mail")
    public void eu_tento_cadastrar_um_novo_microempreendedor_com_o_mesmo_e_mail() {
        // Prepara um novo modelo com CNPJ diferente, mas o mesmo e-mail já usado
        MicroempreendedorCadastroModel modeloDuplicado = new MicroempreendedorCadastroModel();
        modeloDuplicado.setNome("Outro Nome");
        modeloDuplicado.setEmail(this.emailExistente); // REUTILIZA O E-MAIL
        modeloDuplicado.setSenha("outrasenha");
        modeloDuplicado.setNrCnpj("44.444.444/0001-44");

        cadastroUsuarioService.cadastrarMicroempreendedor(modeloDuplicado, "/api/auth/register/microempreendedor");
    }

    @Quando("eu enviar uma requisição POST para {string}")
    public void eu_enviar_uma_requisição_post_para(String endpoint) {
        cadastroUsuarioService.cadastrarMicroempreendedor(this.microempreendedorModel, endpoint);
    }

    @E("que o arquivo de contrato esperado é {string}")
    public void que_o_arquivo_de_contrato_esperado_é(String contractLogicalName) throws IOException {
        cadastroUsuarioService.setContract(contractLogicalName);
    }

    @E("a resposta da requisição deve estar em conformidade com o contrato selecionado")
    public void a_resposta_da_requisição_deve_estar_em_conformidade_com_o_contrato_selecionado() throws IOException {
        Set<ValidationMessage> errors = cadastroUsuarioService.validateResponseAgainstSchema();
        Assert.assertTrue("A resposta da API não corresponde ao contrato. Erros: " + errors, errors.isEmpty());
    }

    @E("a resposta da requisição deve conter a mensagem de erro {string}")
    public void a_resposta_da_requisição_deve_conter_a_mensagem_de_erro(String mensagemEsperada) {
        String responseBody = cadastroUsuarioService.getResponse().getBody().asString();
        JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
        Assert.assertTrue("A resposta não contém a chave 'erro'", jsonObject.has("erro"));
        String mensagemReal = jsonObject.get("erro").getAsString();
        // Ajuste para lidar com o e-mail dinâmico na mensagem de erro
        Assert.assertEquals(mensagemEsperada.replace("user.existent@email.com", this.emailExistente), mensagemReal);
    }
}