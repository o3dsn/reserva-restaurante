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
- [Como fazer Testes](#como-fazer-testes)
- [Endpoints da API](#endpoints-da-api)
- [Configurando as Variáveis de Ambiente](#configurando-as-variáveis-de-ambiente)
- [Comandos Makefile](#comandos-makefile)
- [Relatório de performance](#relatório-de-performance)
- [Contribuindo](#contribuindo)
- [Licença](#licença)

## Estrutura do Projeto

- **/src/main/java**: código-fonte da aplicação.
- **/src/main/resources**: arquivos de configuração e recursos.
- **/src/test/java**: código-fonte dos tests da aplicação.
- **/src/test/resources**: arquivos de configuração e recursos para os tests.

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
   
## Como fazer testes

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

Deve-se criar um arquivo .env na raiz do projeto contendo as variáveis:

| Variável                        | Descrição                                                                     | Exemplo         | 
|---------------------------------|-------------------------------------------------------------------------------|-----------------| 
| `POSTGRES_HOST`                 | HOST para o postgres                                                          | `127.0.0.1`     |
| `POSTGRES_USER`                 | Usuario do postgres                                                           | `postgres`      |
| `POSTGRES_PASSWORD`             | Senha do postgres                                                             | `minhasenha123` | 
| `POSTGRES_PORT`                 | Porta em que a aplicação será executada                                       | `3000`          | 
| `POSTGRES_DB`                   | Banco padrão do postgres                                                      | `test-db`       |
| `POSTGRES_PASSWORD_PERFORMANCE` | Senha para o banco para teste de performance                                  | `minhasenha123` |
| `SPRING_PROFILE`                | Perfil para o Makefile selecionar o profile active quando executar o docker   | `minhasenha123` |

## Comandos Makefile

<span style="color:red">Importante que para os comando make funcionar você deve ter o [Makefile](https://www.gnu.org/software/make/) instalado no computador e ter ele nas variáveis de ambiente</span>

Na raiz do projeto usando alguma ferramenta de linha de comando pode ser executar os seguintes comandos chamando make \<Comando>

| Comando            | Descrição                                                                                                                                                               |
|--------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `build`            | Executa o comando Maven `mvn compile`                                                                                                                                   |
| `package`          | Executa o comando Maven `mvn package`                                                                                                                                   |
| `unit-test`        | Executa o comando Maven `mvn test`                                                                                                                                      |
| `integration-test` | Executa o comando Maven `mvn test -P integration-test`                                                                                                                  |
| `test`             | Executa o comando Make `unit-test` e `integration-test`                                                                                                                 |
| `docker-rmi`       | Executa o comando Docker `docker rmi -f background:latest`                                                                                                              |
| `docker-build`     | Executa o comando Make `package`, `docker-rmi` e comando Docker `docker build -t background:latest -f ./Dockerfile .` deve-se informar  PROFILE='performance' ou 'test' |
| `docker-stop`      | Executa o comando Docker `docker stop rr-ap`                                                                                                                            |
| `docker-run`       | Executa o comando Make `docker-stop` e comando Docker `docker rm -f rr-api,docker run --env-file .env --name rr-api -p 8080:8080 -d background:latest`                  |
| `docker-start`     | Executa o comando Make `docker-down docker-rmi` e comando Docker `docker compose up --build -d`                                                                         |
| `docker-down`      | Executa o comando Docker `docker-compose down`                                                                                                                          |
| `performance-test` | Executa o comando Make `docker-start` e comando Maven `mvn gatling:test -P performance-test` e comando Make `docker-down`                                               |

## Relatório de performance

Após executar o `make performance-test`, pode-se ver um relatório da execução abrindo o arquivo index.html dentro de target/gatling/performancesimulation-\<dataexecução>/index.html \
exemplo do caminho: target/gatling/performancesimulation-20241209162646899/index.html

## Contribuindo

Contribuições são bem-vindas! Para contribuir com o projeto, por favor siga estes passos:

1. Faça um fork do repositório.
2. Crie uma branch para sua feature ou correção (`git checkout -b feature/nova-feature`).
3. Faça commit das suas mudanças (`git commit -am 'Adiciona nova feature'`).
4. Envie suas alterações para o repositório (`git push origin feature/nova-feature`).
5. Abra um pull request.

## Licença

Este projeto está licenciado sob a [MIT License](LICENSE).