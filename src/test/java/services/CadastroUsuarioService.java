package services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.Getter;
import lombok.Setter;
import model.MicroempreendedorCadastroModel;
import org.json.JSONObject;
import org.json.JSONTokener;
import steps.CommonSteps;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;

import static io.restassured.RestAssured.given;

@Getter
@Setter
public class CadastroUsuarioService {

    private Response response;
    private final String BASE_URL = System.getProperty("base.url", "http://localhost:8080");
    private final Gson gson = new Gson();

    private final String schemasPath = "src/test/resources/schemas/";
    private JSONObject jsonSchema;
    private final ObjectMapper mapper = new ObjectMapper();

    public void cadastrarMicroempreendedor(MicroempreendedorCadastroModel model, String endpoint) {
        String jsonBody = gson.toJson(model);

        this.response = given()
                .contentType(ContentType.JSON)
                .body(jsonBody)
                .when()
                .post(BASE_URL + endpoint)
                .then()
                .extract()
                .response();

        CommonSteps.lastResponse = this.response; // Adicione esta linha
    }

    private JSONObject loadJsonFromFile(String filePath) throws IOException {
        try (InputStream inputStream = Files.newInputStream(Paths.get(filePath))) {
            JSONTokener tokener = new JSONTokener(inputStream);
            return new JSONObject(tokener);
        }
    }

    public void setContract(String contractLogicalName) throws IOException {
        switch (contractLogicalName) {
            case "Cadastro de microempreendedor com sucesso" ->
                    jsonSchema = loadJsonFromFile(schemasPath + "cadastro-microempreendedor-sucesso-schema.json");
            default -> throw new IllegalStateException("Contrato inesperado: " + contractLogicalName);
        }
    }

    public Set<ValidationMessage> validateResponseAgainstSchema() throws IOException {
        JSONObject jsonResponse = new JSONObject(response.getBody().asString());
        JsonSchemaFactory schemaFactory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V4);
        JsonSchema schema = schemaFactory.getSchema(jsonSchema.toString());
        JsonNode jsonResponseNode = mapper.readTree(jsonResponse.toString());
        return schema.validate(jsonResponseNode);
    }
}