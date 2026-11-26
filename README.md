# Produtora de Vídeo: Spring Boot + Lombok + Thymeleaf + Angular 22

Estudo de caso individual da disciplina **Desenvolvimento de Sistemas** (Fatec Ferraz de Vasconcelos, ADS).
Aluno: **Eduardo Ferreira Martins**.

Sistema de uma produtora de vídeo com três cadastros ligados entre si:

**Cliente** 1 ── N **Projeto de vídeo** 1 ── N **Entrega**

| Camada | O que é | Endereço |
|---|---|---|
| API REST (Spring Boot 4 + Lombok + JPA) | Fornece os dados em JSON | `http://localhost:8080/clientes/buscar`, `/projetos/buscar`, `/entregas/buscar` |
| Thymeleaf | Interface HTML original, gerada pelo Spring | `http://localhost:8080/web/clientes` |
| Angular 22 | Nova interface (HTML + TypeScript) | `http://localhost:4200` |
| Banco | MySQL (XAMPP), banco `produtoravideo` | `localhost:3306` |

## Pastas

```
api/                 código-fonte Spring Boot (entidades com Lombok, controllers REST e Thymeleaf)
frontend-angular/    código-fonte Angular 22 (sem node_modules)
docs/                descrição do caso, Postman (prints + coleção .json), prints Thymeleaf e Angular, SQL
```

## Como executar

Pré-requisitos: JDK 25, MySQL ligado (XAMPP), Node.js 22.22+ ou 24.15+ (exigência do Angular 22).

**1. API e Thymeleaf (porta 8080).** O banco `produtoravideo` é criado sozinho (`createDatabaseIfNotExist`).
Para ter dados de exemplo, rode `docs/banco/099_recuperacao_produtora_video.sql` no MySQL.

```bash
cd api
./mvnw spring-boot:run        # no Windows: mvnw.cmd spring-boot:run
```

**2. Angular (porta 4200)**, em outro terminal:

```bash
cd frontend-angular
npm install
npx ng serve --host 127.0.0.1 --port 4200 --proxy-config proxy.conf.json
```

Abra `http://127.0.0.1:4200`. O `proxy.conf.json` manda as chamadas `/clientes`, `/projetos` e
`/entregas` do Angular para a API na porta 8080, então não foi preciso mexer em nenhuma classe Java (nem CORS).

## Endpoints REST usados pelo Angular

| Operação | Clientes | Projetos | Entregas |
|---|---|---|---|
| Listar (GET) | `/clientes/buscar` | `/projetos/buscar` | `/entregas/buscar` |
| Buscar por id (GET) | `/clientes/buscar/{id}` | `/projetos/buscar/{id}` | `/entregas/buscar/{id}` |
| Cadastrar (POST) | `/clientes/salvar` | `/projetos/salvar` | `/entregas/salvar` |
| Atualizar (PUT) | `/clientes/atualizar/{id}` | `/projetos/atualizar/{id}` | `/entregas/atualizar/{id}` |
| Excluir (DELETE) | `/clientes/deletar/{id}` | `/projetos/deletar/{id}` | `/entregas/deletar/{id}` |

Detalhes do caso, das regras e das evidências: [docs/DESCRICAO.md](docs/DESCRICAO.md).
