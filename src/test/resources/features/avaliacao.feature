# language: pt

Funcionalidade: API - Avaliacao

  Cenario: Registrar avaliacao
    Dado que exista um restaurante com nome "Restaurante 1" e descricao "Restaurante cucumber" cadastrado
    E que exista um usuario cadastrado
    E uma reserva feito polo usuario para o restaurante com status finalizada
    Quando registrar uma nova avaliacao
    Entao a avaliacao e registrada com sucesso
    E a avaliacao e exibida com sucesso

  Cenario: Buscar avaliacao
    Dado que uma avaliacao ja foi registrada
    Quando efetuar a busca da avaliacao
    Entao a avaliacao e exibida com sucesso

  Cenario: Buscar todas avaliacoes
    Dado que exista duas avaliacoes registradas
    Quando efetuar a busca por todas avaliacoes
    Entao as avaliacoes sao exibidas com sucesso

  Cenario: Buscar avaliacoes por id do restaurante
    Dado que exista duas avaliacoes registradas
    Quando efetuar a busca pelas avaliacoes
    Entao as avaliacoes sao exibidas com sucesso

  Cenario: Buscar nota do restaurante por seu id
    Dado que exista duas avaliacoes registradas
    Quando efetuar a busca por sua nota
    Entao a nota deve ser exibida com sucesso

  Cenario: Alterar avaliacao
    Dado que uma avaliacao ja foi registrada
    Quando efetuar requisicao para alterar avaliacao
    Entao a avaliacao e atualizada com sucesso
    E a avaliacao e exibida com sucesso

  Cenario: Remover avaliacao
    Dado que uma avaliacao ja foi registrada
    Quando efetuar requisicao para remover avaliacao
    Entao a avaliacao e removida com sucesso