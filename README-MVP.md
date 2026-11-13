# Rachão Esportes — Backend MVP

## O que foi removido
- Posições, Chat, Mensagem, FormaPagamento, Habilidades e Amigos (Controllers, Repositories e entidades relacionadas).
- AmigosService, que dependia da funcionalidade removida.

## O que foi mantido ou criado
- JogadorService: cadastro, validação de maioridade, perfil, e-mail, CPF e BCrypt.
- PartidaService: criação, consulta, atualização, cancelamento, contagem de vagas. O organizador ocupa uma vaga automaticamente.
- InscricaoService: entrar/sair, listagem por partida e por jogador, bloqueio de inscrições duplicadas e de excesso de vagas.
- EsporteService: listar e consultar esportes, com carga inicial de FUTEBOL, BASQUETE e VOLEI.
- AutenticacaoService: login com token de sessão aleatório válido por 12h; armazenado somente em memória. **Não é JWT nem solução de autenticação para produção.**
- Modelos/repositórios/endpoints legados de Jogo, Time, JogadorTime e Desempenho preservados para reduzir impacto sobre funcionalidades não solicitadas para remoção. Eles ainda precisam de revisão de regras de negócio/DTOs antes de produção.

## Como executar
Requer Java 17+ e Maven ou Maven Wrapper com acesso aos repositórios Maven.
No diretório do projeto: `./mvnw spring-boot:run` (Windows: `mvnw.cmd spring-boot:run`).
Banco H2 em memória. Todos os dados são perdidos ao parar a aplicação (`spring.jpa.hibernate.ddl-auto=create-drop`).

## Sequência de teste no Postman
1. `POST /api/jogadores` (sem token):
   `{"nome":"Maria Silva","email":"maria@example.com","senha":"Senha123!","dataNascimento":"2000-01-01"}`
2. `POST /api/auth/login` (sem token): `{"email":"maria@example.com","senha":"Senha123!"}`. Copie `token` do retorno.
3. Nas requisições protegidas, use `Authorization: Bearer SEU_TOKEN` e `Content-Type: application/json`.
4. `GET /api/esportes`: lista automática de esportes; escolha o ID desejado.
5. `POST /api/partidas` (autenticado): `{"jogadorHostId":1,"esporteId":1,"qtdTimes":2,"qtdJogadoresTime":5,"dataHora":"2027-12-10T19:00:00","local":"Quadra Central"}`. O ID do organizador deve ser o do login.
6. `GET /api/partidas` ou `GET /api/partidas/1` (público); `PUT /api/partidas/1` (organizador); `POST /api/partidas/1/cancelar` (organizador).
7. Com outro jogador cadastrado/logado: `POST /api/partidas/1/inscricoes` (autenticado): `{"jogadorId":2}`. O ID deve ser do token.
8. `GET /api/partidas/1/inscricoes` (autenticado); `GET /api/jogadores/2/partidas` (próprio jogador); `DELETE /api/partidas/1/inscricoes/2` (próprio jogador).
9. `PUT /api/jogadores/1/perfil` (próprio jogador): `{"cpf":"52998224725","cep":"01310930","descricao":"Gosto de futebol","altura":1.75}`.
10. `POST /api/auth/logout` (autenticado): invalida o token.

**Atenção à integração com o frontend:** `PartidaRequest` possui campo opcional `qtdJogadoresTime` (padrão 5). Operações de escrita exigem token. O backend só permite origem `http://localhost:4200` via CORS. Para clientes com outro endereço, ajuste a configuração.

## Pendências e limitações
- Tokens ficam em memória; ao reiniciar o backend, é necessário fazer login novamente. Não implementar esse mecanismo como autenticação de produção.
- Inscrições controlam a participação nas partidas, mas **a distribuição automática dos jogadores em times não foi implementada**.
- Jogo, Time, JogadorTime e Desempenho continuam como endpoints legados que usam repositórios diretamente e precisam de validação de permissões e modelos de resposta próprios antes de publicar na internet.
- CPF e senha ficam ocultos nos JSONs produzidos a partir de Jogador, mas controle de acesso adicional deve ser revisado antes de produção.
- O banco H2 em memória não persiste dados entre reinícios.
