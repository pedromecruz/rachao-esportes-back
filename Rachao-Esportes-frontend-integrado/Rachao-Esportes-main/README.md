# Rachão Esportes — Front-end integrado

Projeto Angular adaptado aos controllers, DTOs e regras do back-end presente nesta pasta de trabalho. O código do back-end foi preservado.

## Executar

1. Inicie o back-end na porta 8080: na pasta dele, execute `mvnw.cmd spring-boot:run` (Windows) ou `./mvnw spring-boot:run`.
2. Instale Node.js compatível com Angular 22 (a validação desta entrega usou Node 24.22.1).
3. Nesta pasta do front-end, execute `npm install`.
4. Execute `npm start` e acesse http://localhost:4200.

O `proxy.conf.json` encaminha `/api` para http://localhost:8080. Para outra porta/endereço do back-end, altere `target` nesse arquivo. O navegador deve acessar a porta 4200, conforme o CORS do back-end.

## Funcionalidades

- Cadastro real com nome, e-mail, senha, confirmação e data de nascimento; validação de maioridade.
- Login com `{ email, senha }`, token opaco Bearer, validade recebida do servidor e armazenamento por sessão do navegador.
- Proteção das páginas autenticadas, retorno à página solicitada após login e limpeza da sessão ao receber HTTP 401.
- Logout que invalida a sessão no servidor.
- Catálogo de partidas da API com filtros por esporte, local e organizador; estados de carregamento, erro e lista vazia.
- Criação e edição com IDs dos esportes carregados da API; quantidade de times e jogadores, local e data futura no horário de São Paulo.
- Capacidade e vagas informadas pelo back-end, inclusive a inscrição automática do organizador.
- Entrar e sair da partida; consulta de participantes e página Minhas partidas.
- Edição e cancelamento pelo organizador, com confirmação de cancelamento.
- Perfil com consulta dos dados e alteração de CPF, CEP, descrição e altura. O CPF não é exibido pelo servidor.
- Layout original preservado e adaptação do painel para telas pequenas.

## Contratos usados

| Ação | Método e rota |
|---|---|
| Cadastro | POST /api/jogadores |
| Login e logout | POST /api/auth/login e /api/auth/logout |
| Perfil | GET /api/jogadores/{id}, PUT /api/jogadores/{id}/perfil |
| Esportes | GET /api/esportes |
| Partidas | GET /api/partidas, POST /api/partidas, PUT /api/partidas/{id} |
| Cancelamento | POST /api/partidas/{id}/cancelar |
| Inscrições | GET/POST /api/partidas/{id}/inscricoes |
| Sair | DELETE /api/partidas/{id}/inscricoes/{jogadorId} |
| Minhas partidas | GET /api/jogadores/{id}/partidas |

A criação não envia `jogadorHostId` e a inscrição não envia `jogadorId`: o servidor identifica o jogador pelo token. O formulário usa somente os campos persistidos pelo PartidaRequest; duração e endereço estruturado foram retirados porque não existem nesse contrato. Informe o endereço completo em `local`.

## Verificação

- `npm run build`: compilação de produção.
- `npm test -- --watch=false`: testes das telas, contratos de autenticação, isolamento do token em chamadas externas, sessão expirada, filtros e inscrições.
- Os testes HTTP usam o cliente de teste do Angular; não substituem uma validação completa das telas contra o servidor e o banco.

## Publicação

`npm run build` gera `dist/rachao-esportes/browser`. Configure o servidor web para retornar `index.html` nas rotas do aplicativo e encaminhar `/api` ao back-end. O proxy do Angular funciona apenas durante `npm start`. A fonte Montserrat usa Google Fonts e sua incorporação no build requer acesso à internet.

## Limites do back-end

O H2 perde os dados quando o servidor é desligado e os tokens em memória deixam de funcionar após reinício. Não há recuperação de senha, pagamento, chat, amigos, reserva de quadras, busca geográfica ou distribuição automática de times neste MVP; a interface não oferece essas ações. Os endpoints legados de desempenho e times não fazem parte dos fluxos desta entrega.

No perfil, campos opcionais vazios de CPF, CEP e altura são omitidos para manter os valores atuais, conforme as regras da API. Para atualizar CPF ou CEP, informe o valor completo.

Resultado desta entrega: build de produção aprovado; 15 testes automatizados aprovados. A tentativa de verificar o servidor real foi impedida por uma falha de conexão local do Java (`Unable to establish loopback connection`); portanto, o fluxo completo contra o servidor real não foi validado neste ambiente. O back-end não foi alterado para contornar essa falha.
