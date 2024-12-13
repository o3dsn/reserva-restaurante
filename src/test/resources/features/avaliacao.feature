Feature: Avaliação de restaurante

  Scenario: Registrar uma nova avaliação com sucesso
  Dado que exista um restaurante com nome "Restaurante 1" e descricao "Descrição do Restaurante 1" cadastrado
  E que exista um usuario cadastrado
  E uma reserva feita pelo usuario para o restaurante com status finalizada
  Quando registrar uma nova avaliacao
  Então a avaliacao e registrada com sucesso

  Scenario: Exibir uma avaliação registrada com sucesso
  Dado que uma avaliacao ja foi registrada
  Quando efetuar a busca da avaliacao
  Então a avaliacao e exibida com sucesso

  Scenario: Exibir todas as avaliações registradas com sucesso
  Dado que exista duas avaliacoes registradas
  Quando efetuar a busca por todas avaliacoes
  Então as avaliacoes sao exibidas com sucesso

  Scenario: Exibir as avaliações de um restaurante com sucesso
  Dado que exista um restaurante com nome "Restaurante 1" e descricao "Descrição do Restaurante 1" cadastrado
  E que exista duas avaliacoes registradas
  Quando efetuar a busca pelas avaliacoes
  Então as avaliacoes sao exibidas com sucesso

  Scenario: Exibir a nota de um restaurante com sucesso
  Dado que exista um restaurante com nome "Restaurante 1" e descricao "Descrição do Restaurante 1" cadastrado
  E que exista uma avaliacao registrada
  Quando efetuar a busca por sua nota
  Então a nota deve ser exibida com sucesso

  Scenario: Atualizar uma avaliação com sucesso
  Dado que uma avaliacao ja foi registrada
  Quando efetuar requisicao para alterar avaliacao
  Então a avaliacao e atualizada com sucesso

  Scenario: Remover uma avaliação com sucesso
  Dado que uma avaliacao ja foi registrada
  Quando efetuar requisicao para remover avaliacao
  Então a avaliacao e removida com sucesso
