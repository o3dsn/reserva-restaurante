# language: pt

Funcionalidade: API - Reserva

  Cenario: Registrar reserva
    Dado que exista um restaurante com ID "093bff48-6e42-4939-99d9-959f61b41fdd"
    E que exista pelo menos um usuario cadastrado
    Quando registrar uma nova reserva
    E a reserva e registrada com sucesso

  Cenario: Buscar reserva
    Dado que exista um restaurante com ID "093bff48-6e42-4939-99d9-959f61b41fdd"
    E que exista pelo menos um usuario cadastrado
    E registrar uma nova reserva
    Quando buscar a reserva registrada
    Entao os detalhes da reserva sao exibidos com sucesso

  Cenario: Alterar reserva
    Dado que exista um restaurante com ID "093bff48-6e42-4939-99d9-959f61b41fdd"
    E que exista pelo menos um usuario cadastrado
    E registrar uma nova reserva
    Quando atualizar a reserva
    Entao a reserva e atualizada com sucesso

  Cenario: Remover reserva
    Dado que exista um restaurante com ID "093bff48-6e42-4939-99d9-959f61b41fdd"
    E que exista pelo menos um usuario cadastrado
    E registrar uma nova reserva
    Quando deletar a reserva registrada
    Entao a reserva e deletada com sucesso