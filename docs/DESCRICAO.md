# Descrição do caso: Produtora de Vídeo

## O problema

Uma produtora de vídeo precisa controlar os **clientes** que contratam trabalhos, os **projetos de vídeo** de cada
cliente (título, datas e orçamento) e as **entregas** de cada projeto (data, formato do arquivo e status).

## Modelo de dados (MySQL, banco `produtoravideo`)

| Tabela | Campos | Regras |
|---|---|---|
| `Cliente` | idCliente, nomeCliente, cpfCnpj, telefone, email | CPF/CNPJ e e-mail únicos; todos obrigatórios |
| `ProjetoVideo` | idProjeto, idCliente (FK), tituloProjeto, dataInicio, dataEntrega, orcamento | orçamento `DECIMAL(10,2)` (até 99.999.999,99); precisa de um cliente existente |
| `EntregaProjeto` | idEntrega, idProjeto (FK), dataEntrega, formatoArquivo, statusEntrega | precisa de um projeto existente |

Por causa das chaves estrangeiras, não dá para excluir um cliente que tem projetos, nem um projeto que tem entregas.
O script com a criação das tabelas, os dados de exemplo e as consultas está em [banco/](banco/).

## Etapas da evolução

1. **Back-end (Spring Boot + MySQL):** entidades JPA, repositories, services e controllers REST. As regras e os
   endpoints não mudaram nesta etapa: nenhuma classe Java foi alterada para o Angular.
2. **Lombok:** as entidades usam `@Getter`, `@Setter`, `@NoArgsConstructor` e `@AllArgsConstructor`, e os
   controllers usam `@RequiredArgsConstructor` (injeção pelo construtor sem escrever o construtor).
3. **Postman:** os endpoints foram testados com prints em [postman/](postman/). A coleção
   `produtora_video.postman_collection.json` tem as 18 requisições (6 por cadastro) para importar no Postman.
4. **Thymeleaf:** a interface HTML original continua funcionando em `/web/clientes`, `/web/projetos` e
   `/web/entregas` (prints em [prints_thymeleaf/](prints_thymeleaf/)).
5. **Angular 22:** pasta `frontend-angular`, criada com o Angular CLI 22.2.5, com:
   - `proxy.conf.json`, que manda `/clientes`, `/projetos` e `/entregas` para `http://127.0.0.1:8080`;
   - `app.config.ts` com `provideHttpClient(withFetch())` e o idioma pt-BR (datas e R$);
   - `models/`: as interfaces `Cliente`, `Projeto` e `Entrega`, iguais ao JSON da API;
   - `services/`: `ClienteApiService`, `ProjetoApiService` e `EntregaApiService` (`@Injectable`), com listar,
     buscarPorId, cadastrar, atualizar e excluir;
   - componentes `clientes`, `projetos` e `entregas` (`.ts`, `.html`, `.css`), cada um com listagem, filtro,
     cadastro, edição e exclusão.

## Como o Angular conversa com o Spring

```
Navegador (4200) --GET /clientes/buscar--> ng serve (proxy) --> Spring Boot (8080) --> MySQL
```

No projeto e na entrega, o cliente do projeto é enviado só pelo id (`idCliente`, `idProjeto`). As entidades
têm `setIdCliente`/`setIdProjeto`, então o Spring monta a relação sem o Angular mandar o objeto inteiro.

## Evidências do Angular (porta 4200)

| Print | Operação |
|---|---|
| 01 | Listagem de clientes (GET) |
| 02 | Filtro de clientes (busca "mariana") |
| 03 e 04 | Cadastro de cliente (POST) e mensagem de sucesso |
| 05 e 06 | Edição do telefone do cliente (PUT) |
| 07 a 09 | Listagem e cadastro de projeto, com o select de clientes |
| 10 a 12 | Listagem e cadastro de entrega, com o select de projetos |
| 13 e 14 | Edição do status para "Aprovado" e filtro por "aprovado" |
| 15 | Exclusão da entrega (DELETE) |
| 16 | Exclusão bloqueada: cliente com projeto (chave estrangeira) |
| 17 e 18 | Exclusão do projeto e do cliente de teste |
| 19 | Tela no celular (390 px) |

Os registros de teste ("Estúdio Aurora Filmes" e o projeto "Clipe musical - Banda Aurora") foram criados e
excluídos pelo próprio Angular, e o banco voltou aos 3 registros de cada tabela.

Observação: no computador em que as prints foram feitas, a porta 8080 estava ocupada por outro programa
(Steam), então a API rodou na 8081 só para o teste, com uma cópia do proxy apontando para lá. O projeto
entregue usa a porta padrão 8080.
