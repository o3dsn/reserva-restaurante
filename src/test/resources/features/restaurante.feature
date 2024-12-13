Feature: Cadastro de restaurante

  Scenario: Registrar um novo restaurante com sucesso
  Quando registrar um novo restaurante com nome "Restaurante A" e descricao "Descrição do Restaurante A"
  Então o restaurante e registrado com sucesso

  Scenario: Exibir os detalhes do restaurante com sucesso
  Dado que exista um restaurante cadastrado com nome "Restaurante A" e descricao "Descrição do Restaurante A"
  Quando buscar os detalhes do restaurante
  Então os detalhes do restaurante sao exibidos com sucesso

  Scenario: Atualizar os detalhes do restaurante com sucesso
  Dado que exista um restaurante cadastrado com nome "Restaurante A" e descricao "Descrição do Restaurante A"
  Quando atualizar o restaurante com nome "Restaurante B" e descricao "Descrição do Restaurante B"
  Então o restaurante e atualizado com sucesso

  Scenario: Remover um restaurante com sucesso
  Dado que exista um restaurante cadastrado com nome "Restaurante A" e descricao "Descrição do Restaurante A"
  Quando remover o restaurante
  Então o restaurante e removido com sucesso
