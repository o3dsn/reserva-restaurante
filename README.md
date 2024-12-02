# tech-challenger-fiap

Bem-vindo ao repositório do projeto "Reserva Restaurante", desenvolvido como parte do desafio tecnológico da FIAP. Este projeto visa implementar uma API para gerenciamento de reservas e avaliações de restaurantes.

Projeto de pós-graduação em arquitetura e desenvolvimento JAVA pela FIAP
ALUNOS 5ADJT

<p>Edson Antonio da Silva Junior</p>
<p>Gabriel Ricardo dos Santos</p>
<p>Luiz Henrique Romão de Carvalho</p>
<p>Marcelo de Souza</p>

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.5-brightgreen?style=flat&logo=spring&logoColor=white)
![Java 17](https://img.shields.io/badge/Java-17-blue?style=flat&logo=java&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-3.3.2-orange?style=flat&logo=apachemaven&logoColor=white)
![PostgresSQL](https://img.shields.io/badge/PostgresSQL-17.1-blue?style=flat&logo=postgresql&logoColor=white)
![Swagger](https://img.shields.io/badge/Swagger-3.0-brightgreen?style=flat&logo=swagger&logoColor=white)

## Sumário

- [Estrutura do Projeto](#estrutura-do-projeto)
- [Como Executar o Projeto](#como-executar-o-projeto)
- [Endpoints da API](#endpoints-da-api)
- [Configurando as Variáveis de Ambiente](#configurando-as-variáveis-de-ambiente)
- [Contribuindo](#contribuindo)
- [Licença](#licença)

## Estrutura do Projeto

- **/src/main/java**: código-fonte da aplicação.
- **/src/main/resources**: arquivos de configuração e recursos.

## Como Executar o Projeto

Para rodar o projeto localmente, siga os passos abaixo:

1. **Clone o repositório:**

    ```bash
    git clone https://github.com/sdrgabriel/reserva-restaurante.git
    ```

2. **Navegue até o diretório do projeto:**

    ```bash
    cd reserva-restaurante
    ```

3. **Colocar as variáveis de ambiente do arquivo que foi anexado com a documentação**
   ```bash
   caso isso nao seja feito o projeto nao ira fazer o build
    ```
4. **Construa o projeto com Maven:**

    ```bash
    mvn clean install -U
    ```

5. **Inicie a aplicação localmente:**

    ```bash
    mvn spring-boot:run
    ```
## Testes
- **Para executar os testes unitários:**

   ```bash
   mvn test
   ```

- **Para executar os testes integrados:**

   ```bash
   mvn test -P integration-text
   ```

- **Para executar os testes de sistema:**

   ```bash
   mvn test -P system-text
   ```
  
## Endpoints da API

A API pode ser explorada e testada utilizando o Swagger. A documentação está disponível em:
http://localhost:8080/swagger-ui/index.html

## Configurando as Variáveis de Ambiente

## Contribuindo

Contribuições são bem-vindas! Para contribuir com o projeto, por favor siga estes passos:

1. Faça um fork do repositório.
2. Crie uma branch para sua feature ou correção (`git checkout -b feature/nova-feature`).
3. Faça commit das suas mudanças (`git commit -am 'Adiciona nova feature'`).
4. Envie suas alterações para o repositório (`git push origin feature/nova-feature`).
5. Abra um pull request.

## Licença

Este projeto está licenciado sob a [MIT License](LICENSE).