# language: pt

Funcionalidade: Gerenciamento de Produtos
  Como um microempreendedor autenticado
  Eu quero adicionar novos produtos ao meu portfólio
  Para que os clientes possam visualizá-los

  Contexto: Ter um microempreendedor cadastrado e pronto para login
    Dado que um microempreendedor foi cadastrado com sucesso usando os dados:
      | campo  | valor                 |
      | email  | felipe@email.com      |
      | senha  | senha123              |
      | nrCnpj | 99.888.777/0001-66    |
      | nome   | Felipe's Eco-Friendly |

  @cadastro-produto-sucesso
  Cenário: Adicionar um novo produto com sucesso
    Dado que o microempreendedor do contexto faz login para obter o token de autenticação
    E eu tenho os seguintes dados para um novo produto:
      | campo     | valor                  |
      | nmProduto | "Garrafa Reutilizável" |
      | vlProduto | 39.90                  |
      | qtProduto | 150                    |
      | dsProduto | "Garrafa de aço inox"  |
    Quando eu enviar uma requisição POST para o endpoint de produtos
    Então o status code da resposta deve ser 201