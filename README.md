# Projeto de Extensão do curso ADS

## Desenvolvimento de uma aplicação para auxílio no tratamento de pessoas com dor crônica


* O objetivo do sistema é fornecer uma base inicial para um aplicativo de acompanhamento de dor crônica, permitindo registro de crises e geração de históricos.
*  Features que serão adicionadas no futuro: compartilhamento com profissionais de saúde, comunidade e funcionalidades avançadas.

---

## Tecnologias Utilizadas

| Tecnologia / Ferramenta | Descrição                                           |
|--------------------------|-----------------------------------------------------|
| **Kotlin (JDK 21)** | Linguagem base do projeto                           |
| **Spring Boot 3** | Framework principal (Web, JPA, Security, Validation) |
| **Spring Data JPA** | Persistência e mapeamento ORM                       |
| **Spring Security** | Segurança e autenticação                            |
| **Liquibase** | Controle de versão do schema do banco               |
| **PostgreSQL** | Banco de dados relacional                           |
| **Docker Compose** | Orquestração dos containers (DB, pgAdmin)           |
| **pgAdmin 4** | Interface gráfica para o banco                      |
| **Actuator** | Monitoramento da aplicação                          |
| **IntelliJ IDEA** | IDE utilizada                                      |

---

## Executando com Docker Compose

Certifique-se de ter **Docker** e **Docker Compose** instalados.

### 1. Subir Postgres e pgAdmin:

```bash
docker-compose up -d
```

### - Serviços expostos:

| Serviço      | 	Porta |
|--------------|--------|
| **Postgres** | 	5432  |
| **pgAdmin**  | 5050   |

#### - Acessando o pgAdmin
- url: http://localhost:5050
```
user: admin@admin.com  
senha: admin
```

### - Testando se a API está funcionando

http://localhost:8080/health

Deve retornar um JSON:

```
{
    "status": "UP"
}
```