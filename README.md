# Projeto de Testes Automatizados - Micronectar API

Este projeto contém a suíte de testes automatizados para a API `micronectar-api`, desenvolvida como parte da atividade acadêmica "Desafio de Testes Automatizados". O objetivo é validar as funcionalidades críticas da API, garantindo sua qualidade, estabilidade e conformidade com os requisitos de negócio, utilizando a metodologia BDD (Behavior-Driven Development).

---

## Tecnologias Utilizadas

A automação foi construída utilizando um ecossistema robusto baseado em Java:

- **Java 21:** Linguagem de programação base.
- **Maven:** Ferramenta de gerenciamento de dependências e build do projeto.
- **Cucumber (v7.18.1):** Framework para escrita de testes em BDD com a linguagem Gherkin.
- **JUnit:** Runner para a execução dos testes Cucumber.
- **Rest Assured (v5.5.0):** Biblioteca para testes de API REST, simplificando a realização de requisições HTTP e validações de respostas.
- **JSON Schema Validator:** Ferramenta para realizar testes de contrato, garantindo que a estrutura das respostas da API esteja correta.
- **Gson:** Biblioteca para serialização e desserialização de objetos Java para JSON.
- **GitHub Actions:** Ferramenta de Integração Contínua (CI) para validação automática dos testes.

---

## Pré-requisitos

Para executar os testes localmente, certifique-se de que os seguintes softwares estão instalados e configurados em seu ambiente:

1.  **Java JDK 21** ou superior.
2.  **Apache Maven 3.9** ou superior.
3.  **Docker e Docker Compose:** Necessários para executar a API `micronectar-api` em um ambiente local controlado.

---

## Execução Local dos Testes

Siga os passos abaixo para executar a suíte de testes completa.

### Método Padrão (Recomendado)

Este método executa os testes contra uma instância local da API, garantindo um ambiente controlado e repetível.

1.  **Clone e inicie a API `micronectar-api` localmente:**
    *   Primeiro, clone o repositório da API:
      ```bash
      git clone https://github.com/VictorSouza112/micronectar-api.git
      ```
    *   Navegue até a pasta raiz do projeto da API e inicie os contêineres (API + Banco de Dados):
      ```bash
      cd micronectar-api
      docker-compose up -d
      ```

2.  **Clone e execute os testes:**
    *   Em um novo terminal, clone o repositório deste projeto de testes.
    *   Navegue até a pasta raiz (`micronectar-api-tests`) e execute o comando Maven. Ele usará a URL padrão `http://localhost:8080`.
      ```bash
      cd micronectar-api-tests
      mvn clean test
      ```

### Método Alternativo (Contra um Ambiente Específico - Mais prático

Você pode apontar os testes para qualquer URL base (como um ambiente de staging) sem alterar o código, passando a URL como um parâmetro de sistema.

1.  **Execute os testes passando a URL como parâmetro:**
    Substitua `URL_DA_SUA_API` pela URL desejada.
    ```bash
    mvn clean test -Dbase.url=URL_DA_SUA_API
    ```
    *Exemplo para o ambiente de Staging:*
    ```bash
    mvn clean test -Dbase.url=https://app-micronectar-staging-vs-bwezgje4gpc2etag.brazilsouth-01.azurewebsites.net/
    ```

---

## Relatório de Testes

Após a execução, um relatório detalhado em HTML é gerado no diretório `target/`. Você pode abri-lo em seu navegador para uma visualização completa dos resultados:

`target/cucumber-reports.html`

---

## Pipeline de Integração Contínua (CI)

Este projeto está configurado com uma pipeline de Integração Contínua utilizando **GitHub Actions**. O workflow, definido em `.github/workflows/ci.yml`, é acionado automaticamente a cada `pull request` aberto para as branches `main` ou `develop`.

Este processo garante que todos os testes sejam validados antes que novas alterações sejam integradas ao código principal, mantendo a estabilidade e a qualidade do projeto.