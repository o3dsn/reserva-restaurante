# Tech Challenger FIAP

Bem-vindo ao repositório do projeto "Reserva Restaurante", desenvolvido como parte do desafio tecnológico da FIAP. 

Este projeto visa implementar uma API para gerenciamento de reservas e avaliações de restaurantes.

## 🎓 Projeto Acadêmico

Desenvolvido como trabalho de conclusão do curso de Pós-Graduação em **Arquitetura e Desenvolvimento Java** na FIAP - Turma 5ADJT.

## 👨‍💻 Desenvolvedores

- Edson Antonio da Silva Junior
- Gabriel Ricardo dos Santos
- Luiz Henrique Romão de Carvalho
- Marcelo de Souza

<h1 style="color: blue;">💡 Tecnologias</h1>

![Java](https://img.shields.io/badge/Java-17-blue?style=for-the-badge&logo=java)

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.5-brightgreen?style=for-the-badge)

![Maven](https://img.shields.io/badge/Maven-3.8.6-C71A36?style=for-the-badge&logo=apachemaven)

![Postgre](https://img.shields.io/badge/PostgreSQL-15-336791?style=for-the-badge&logo=postgresql)

![Docker](https://img.shields.io/badge/Docker-24.0.6-2496ED?style=for-the-badge&logo=docker)

![Swagger](https://img.shields.io/badge/Swagger-3.0-85EA2D?style=for-the-badge&logo=swagger)

## 📦 Estrutura do Projeto

```java
📦 br.com.fiap.reservarestaurante
├── 🎯 application
│   ├── 💎 domain
│   │   ├── avaliacao       // Domínio de avaliações de restaurantes
│   │   ├── paginacao       // Gerenciamento de dados paginados
│   │   ├── reserva         // Domínio de reservas de mesas
│   │   ├── restaurante     // Domínio de estabelecimentos
│   │   └── usuario         // Domínio de usuários do sistema
│   │
│   ├── ⚠️ exceptions       // Tratamento personalizado de erros
│   ├── 📝 repositories     // Contratos de acesso a dados
│   │
│   ├── ⚡️ usecases         // Regras de negócio da aplicação
│   │   ├── avaliacao       // Operações de avaliações
│   │   │   ├── create      // Criar nova avaliação
│   │   │   ├── delete      // Remover avaliação
│   │   │   ├── retrieve    // Consultar avaliações
│   │   │   │   ├── get     // Buscar uma avaliação
│   │   │   │   └── list    // Listar avaliações
│   │   │   └── update      // Atualizar avaliação
│   │   │
│   │   ├── reserva         // Operações de reservas
│   │   │   ├── create      // Criar nova reserva
│   │   │   ├── delete      // Cancelar reserva
│   │   │   ├── retrieve    // Consultar reservas
│   │   │   │   ├── get     // Buscar uma reserva
│   │   │   │   └── list    // Listar reservas
│   │   │   └── update      // Modificar reserva
│   │   │
│   │   ├── restaurante     // Operações de restaurantes
│   │   │   ├── create      // Cadastrar restaurante
│   │   │   ├── delete      // Remover restaurante
│   │   │   ├── retrieve    // Consultar restaurantes
│   │   │   │   ├── get     // Buscar um restaurante
│   │   │   │   └── list    // Listar restaurantes
│   │   │   └── update      // Atualizar restaurante
│   │   │
│   │   └── usuario         // Operações de usuários
│   │       ├── create      // Criar novo usuário
│   │       └── retrieve    // Consultar usuários
│   │           └── get     // Buscar um usuário
│   │
├── 🏗 infrastructure
│   ├── api                 // Controllers REST
│   ├── config              // Configurações da aplicação
│   ├── mappers             // Conversores DTO ↔ Entity
│   ├── persistence         // Configuração do banco
│   │   ├── entities        // Modelos do banco
│   │   └── repositories    // Interfaces JPA
│   │
│   └── repositories        // Implementação dos repositórios
│
└── 🚀 ReservaRestauranteApplication.java
```

## ▶️ Como Executar o Projeto

Para rodar o projeto localmente, siga os passos abaixo:

1. **Clone o repositório:**
    
    ```bash
    git clone <https://github.com/sdrgabriel/reserva-restaurante.git>
    
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
    

## 🧪 Como Fazer os Testes

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
    

## 🧪 Endpoint da API

A API pode ser explorada e testada utilizando o Swagger. A documentação está disponível em:
[`Swagger`](http://localhost:8080/swagger-ui/index.html)

## ⚙️ Configurando Variáveis de Ambiente

Deve-se criar um arquivo .env na raiz do projeto contendo as variáveis:

| Variável | Descrição | Exemplo |
| --- | --- | --- |
| `POSTGRES_HOST` | HOST para o postgres | `127.0.0.1` |
| `POSTGRES_USER` | Usuario do postgres | `postgres` |
| `POSTGRES_PASSWORD` | Senha do postgres | `minhasenha123` |
| `POSTGRES_PORT` | Porta em que a aplicação será executada | `3000` |
| `POSTGRES_DB` | Banco padrão do postgres | `test-db` |
| `POSTGRES_PASSWORD_PERFORMANCE` | Senha para o banco para teste de performance | `minhasenha123` |
| `SPRING_PROFILE` | Perfil para o Makefile selecionar o profile active quando executar o docker | `performance` |

## 📄 Relatório de Performance

Após executar o `make performance-test`, pode-se ver um relatório da execução abrindo o arquivo index.html dentro de `target/gatling/performancesimulation-\<dataexecução>/index.html \`
exemplo do caminho: `target/gatling/performancesimulation-20241209162646899/index.html`

## 👥 Contribuindo

Contribuições são bem-vindas! Para contribuir com o projeto, por favor siga estes passos:

1. Faça um fork do repositório.
2. Crie uma branch para sua feature ou correção (`git checkout -b feature/nova-feature`).
3. Faça commit das suas mudanças (`git commit -am 'Adiciona nova feature'`).
4. Envie suas alterações para o repositório (`git push origin feature/nova-feature`).
5. Abra um pull request.

## Licença

Este projeto está licenciado sob a [MIT License](https://www.notion.so/LICENSE).
