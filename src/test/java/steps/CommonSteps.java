package steps;

import io.cucumber.java.pt.Então;
import io.restassured.response.Response;
import org.junit.Assert;

public class CommonSteps {

    // Este método precisa de uma maneira de saber qual 'response' validar.
    // A melhor forma é salvá-la em um contexto compartilhado.
    // Por simplicidade agora, vamos criar uma variável estática para armazenar a última resposta.

    public static Response lastResponse;

    @Então("o status code da resposta deve ser {int}")
    public void o_status_code_da_resposta_deve_ser(int statusCode) {
        Assert.assertNotNull("A resposta da API não pode ser nula para validar o status code", lastResponse);
        Assert.assertEquals(statusCode, lastResponse.getStatusCode());
    }
}