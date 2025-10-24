# language: pt

Funcionalidade: Cadastro de Usuários na Plataforma Micronectar
  Como um novo usuário
  Eu quero me cadastrar na plataforma
  Para poder acessar suas funcionalidades

  @cadastro-sucesso
  Cenário: Cadastro de um novo microempreendedor com sucesso
    Dado que os dados para um novo microempreendedor são gerados dinamicamente com base em:
      | campo              | valor                        |
      | nome               | Empreendimentos Sustentáveis |
      | email              | user.success@email.com       |
      | senha              | senhaForte123                |
      | nrCnpj             | 92.113.163/0001-07           |
      | nmFundador         | Eduardo da Silva             |
    Quando eu enviar uma requisição POST para "/api/auth/register/microempreendedor"
    Então o status code da resposta deve ser 201
    E que o arquivo de contrato esperado é "Cadastro de microempreendedor com sucesso"
    E a resposta da requisição deve estar em conformidade com o contrato selecionado

  @cadastro-duplicado
  Cenário: Tentar cadastrar um microempreendedor com e-mail já existente
    Dado que um microempreendedor já foi cadastrado com os seguintes dados:
      | campo              | valor                        |
      | nome               | Usuário Existente            |
      | email              | user.existent@email.com      |
      | senha              | senhaForte123                |
      | nrCnpj             | 33.333.333/0001-33           |
      | nmFundador         | Já Existe da Silva           |
    Quando eu tento cadastrar um novo microempreendedor com o mesmo e-mail
    Então o status code da resposta deve ser 409
    E a resposta da requisição deve conter a mensagem de erro "E-mail já cadastrado: user.existent@email.com"