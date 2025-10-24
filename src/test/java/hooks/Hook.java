package hooks;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

public class Hook {

    @Before
    public void setUp(Scenario scenario) {
        System.out.println("--------------------------------------------------");
        System.out.println("Iniciando cenário: " + scenario.getName());
        System.out.println("--------------------------------------------------");
    }

    @After
    public void tearDown(Scenario scenario) {
        System.out.println("--------------------------------------------------");
        System.out.println("Cenário finalizado: " + scenario.getName());
        System.out.println("Status: " + scenario.getStatus());
        System.out.println("--------------------------------------------------");
    }
}