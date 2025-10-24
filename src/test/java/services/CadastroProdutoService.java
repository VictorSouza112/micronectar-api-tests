package services;

import model.LoginModel;
import model.MicroempreendedorCadastroModel;
import model.ProdutoCadastroModel;
import steps.CommonSteps;
import com.google.gson.Gson;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.Getter;

import static io.restassured.RestAssured.given;

@Getter
public class CadastroProdutoService {

    private Response response;
    private final String BASE_URL = "http://localhost:8080";
    private final Gson gson = new Gson();

    // Método para cadastrar o usuário do Contexto
    public Response cadastrarMicroempreendedor(MicroempreendedorCadastroModel model) {
        String jsonBody = gson.toJson(model);
        Response responseCadastro = given()
                .contentType(ContentType.JSON)
                .body(jsonBody)
                .when()
                .post(BASE_URL + "/api/auth/register/microempreendedor");

        // Atualiza a resposta para o step comum
        CommonSteps.lastResponse = responseCadastro;
        return responseCadastro;
    }

    // Método para fazer login e extrair o token
    public String login(LoginModel loginModel) {
        String jsonBody = gson.toJson(loginModel);
        this.response = given()
                .contentType(ContentType.JSON)
                .body(jsonBody)
                .when()
                .post(BASE_URL + "/api/auth/login")
                .then()
                .extract().response();

        // Atualiza a resposta para o step comum
        CommonSteps.lastResponse = this.response;

        // Extrai o token do corpo da resposta de login
        return response.jsonPath().getString("token");
    }

    // Método para adicionar o produto, recebendo o token e o ID
    public void adicionarProduto(String token, Long microempreendedorId, ProdutoCadastroModel produtoModel) {
        String jsonBody = gson.toJson(produtoModel);
        String endpoint = "/api/microempreendedores/" + microempreendedorId + "/produtos";

        this.response = given()
                .header("Authorization", "Bearer " + token) // Adiciona o cabeçalho de autenticação
                .contentType(ContentType.JSON)
                .body(jsonBody)
                .when()
                .post(BASE_URL + endpoint)
                .then()
                .extract().response();

        // Atualiza a resposta para o step comum
        CommonSteps.lastResponse = this.response;
    }
}