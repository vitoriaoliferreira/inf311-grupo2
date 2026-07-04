# GeSUAS 360 — Documentação Técnica

Aplicativo Android para o evento **SUAS 360**, permitindo que participantes acompanhem a programação, confirme presença nas palestras via QR code, gerenciem favoritos e recebam notificações push dos organizadores.

---

## Sumário

1. [Visão Geral da Arquitetura](#1-visão-geral-da-arquitetura)
2. [Configuração do Firebase (FCM)](#2-configuração-do-firebase-fcm)
3. [Autenticação](#3-autenticação)
4. [Especificação da API REST](#4-especificação-da-api-rest)
   - [Auth](#41-auth)
   - [Participante](#42-participante)
   - [Palestras](#43-palestras)
   - [Palestrantes](#44-palestrantes)
   - [Presença](#45-presença)
   - [Notificações](#46-notificações)
   - [Mensagens](#47-mensagens)
   - [Enquetes](#48-enquetes)
   - [Avaliações](#49-avaliações)
5. [Notificações Push (FCM)](#5-notificações-push-fcm)
6. [Modelos de Dados](#6-modelos-de-dados)
7. [Como Substituir os Mocks](#7-como-substituir-os-mocks)
8. [Variáveis de Configuração do App](#8-variáveis-de-configuração-do-app)

---

## 1. Visão Geral da Arquitetura

```
app/src/main/java/com/example/gesuas360/
├── models/               Entidades de dados (Participante, Palestra, etc.)
├── repositories/         Camada de acesso a dados (mock → API)
├── services/             GeSuasMessagingService (FCM)
├── adapters/             RecyclerView adapters
├── views/                Fragments (telas)
├── SessaoUsuario.java    Singleton com o participante logado
└── MainActivity.java     Navegação + drawer + canal de notificação
```

### Padrão Repository

Cada domínio tem um `Repository` singleton responsável por todos os dados daquela área. Atualmente todos os repositórios retornam dados mockados com um delay simulado. Para integrar a API real, basta substituir o corpo de cada método pelo cliente HTTP (Retrofit, Volley, etc.) sem alterar o contrato das interfaces de callback.

```
Fragment → Repository.metodo(callback) → [mock delay] → callback.onSuccess(dados)
                                       → [API real]   → callback.onSuccess(dados)
```

### Autenticação

O token JWT retornado em `POST /api/auth/login` é armazenado em `SessaoUsuario.getParticipante().getToken()` e deve ser enviado como `Authorization: Bearer {token}` em todas as requisições protegidas.

---

## 2. Configuração do Firebase (FCM)

O app usa **Firebase Cloud Messaging** para receber notificações push.

### Passo a passo (apenas uma vez)

1. Acesse [console.firebase.google.com](https://console.firebase.google.com)
2. Crie um projeto (ex: "GeSUAS 360")
3. Adicione um app Android com o package `com.example.gesuas360`
4. Baixe o arquivo `google-services.json` gerado
5. Substitua `app/google-services.json` pelo arquivo baixado

> O arquivo placeholder atual (`app/google-services.json`) permite compilar, mas **não entrega notificações reais**.

### Chaves e onde ficam

| Chave | Localização | Quem usa |
|---|---|---|
| `google-services.json` | `app/` (no repositório) | App Android — inicializa o SDK |
| **FCM Server Key** | Firebase Console → Project Settings → Cloud Messaging | **Apenas o backend da API** — nunca no app |

---

## 3. Autenticação

O token JWT retornado pelo login é a credencial de todas as requisições subsequentes.

```
Header de autenticação:
Authorization: Bearer <token>
```

O app **não** implementa refresh de token. Quando a API expirar o token, o comportamento esperado é um 401 que o app deve tratar redirecionando para a tela de login.

---

## 4. Especificação da API REST

**Base URL:** `https://api.suas360.com.br` (a definir)

Todas as respostas de erro seguem o formato:
```json
{ "erro": "Mensagem descritiva do erro." }
```

---

### 4.1 Auth

#### `POST /api/auth/login`

Autentica o participante pelo e-mail cadastrado no evento e pela senha gerada pela organização.

**Request:**
```json
{
  "email": "maria.silva@email.com",
  "senha": "suas2024"
}
```

**Response 200:**
```json
{
  "id":    "P001",
  "nome":  "Maria Silva",
  "email": "maria.silva@email.com",
  "token": "eyJhbGciOiJIUzI1NiIsInR5..."
}
```

**Erros:**
| Status | Corpo |
|---|---|
| 401 | `{ "erro": "Credenciais inválidas." }` |
| 404 | `{ "erro": "E-mail não encontrado." }` |

**Arquivo:** `AuthRepository.java`

---

### 4.2 Participante

Todos os endpoints abaixo requerem `Authorization: Bearer {token}`.

---

#### `PUT /api/participante/email`

Atualiza o e-mail do participante logado.

**Request:**
```json
{ "email": "novo@email.com" }
```

**Response 200:**
```json
{ "ok": true }
```

**Validação no app (antes de chamar a API):**
- Campo não pode ser vazio
- Deve ser um e-mail válido (regex `android.util.Patterns.EMAIL_ADDRESS`)

**Arquivo:** `ParticipanteRepository.java` → `atualizarEmail()`

---

#### `PUT /api/participante/telefone`

Atualiza o telefone do participante logado.

**Request:**
```json
{ "telefone": "(11) 99999-9999" }
```

**Response 200:**
```json
{ "ok": true }
```

**Validação no app:** mínimo 8 dígitos numéricos (remove formatação antes de contar).

**Arquivo:** `ParticipanteRepository.java` → `atualizarTelefone()`

---

#### `PUT /api/participante/cidade`

Atualiza a cidade do participante logado.

**Request:**
```json
{ "cidade": "São Paulo" }
```

**Response 200:**
```json
{ "ok": true }
```

**Arquivo:** `ParticipanteRepository.java` → `atualizarCidade()`

---

#### `PUT /api/participante/fcm-token`

Registra ou atualiza o token FCM do dispositivo do participante. Chamado automaticamente após o login e sempre que o SDK do Firebase gera um novo token.

**Request:**
```json
{ "fcmToken": "dGhpcyBpcyBhIHNhbXBsZSB0b2tlbg..." }
```

**Response 200:**
```json
{ "ok": true }
```

> A API deve armazenar este token associado ao `participanteId`. Ele é usado para enviar push notifications segmentadas via FCM Server Key (ver [Seção 5](#5-notificações-push-fcm)).

**Arquivo:** `ParticipanteRepository.java` → `registrarFcmToken()`

---

### 4.3 Palestras

#### `GET /api/palestras`

Retorna todas as palestras do evento.

**Response 200:**
```json
[
  {
    "horario":        "14:00",
    "data":           "11",
    "local":          "Sala A",
    "titulo":         "Vínculos que Protegem",
    "descricao":      "Conexões humanas que sustentam a proteção social no SUAS...",
    "palestranteNome": "Mariana Torres",
    "palestranteBio": "Assistente Social, Mestre e Doutora em Serviço Social pela PUC/SP"
  }
]
```

> O campo `data` é o dia do mês como string (`"11"`, `"12"`, `"13"`, `"14"`).

**Arquivo:** `PalestraRepository.java`

---

#### `GET /api/palestras?data={dia}`

Retorna as palestras filtradas por dia.

**Parâmetro:** `dia` — número do dia como string (`"11"`, `"12"`, etc.)

**Response 200:** mesmo formato de `GET /api/palestras`, filtrado.

---

#### `GET /api/evento/dias`

Retorna os dias do evento com rótulos formatados para exibição no seletor de datas.

**Response 200:**
```json
[
  { "dia": "11", "mes": "Maio", "label": "Segunda-Feira, 11 de Maio" },
  { "dia": "12", "mes": "Maio", "label": "Terça-Feira, 12 de Maio"  },
  { "dia": "13", "mes": "Maio", "label": "Quarta-Feira, 13 de Maio" },
  { "dia": "14", "mes": "Maio", "label": "Quinta-Feira, 14 de Maio" }
]
```

**Arquivo:** `PalestraRepository.java` → `getDiasDoEvento()`

---

### 4.4 Palestrantes

#### `GET /api/palestrantes`

Retorna todos os palestrantes do evento.

**Response 200:**
```json
[
  {
    "nome":     "Mariana Torres",
    "bio":      "Assistente Social, Mestre e Doutora em Serviço Social pela PUC/SP",
    "descricao": "Mariana Torres é doutora em Serviço Social pela PUC/SP..."
  }
]
```

**Arquivo:** `PalestranteRepository.java`

---

### 4.5 Presença

#### `POST /api/presenca/confirmar`

Confirma a presença de um participante em uma palestra após a leitura do QR code de presença.

Requer `Authorization: Bearer {token}`.

**Request:**
```json
{
  "participanteId":    "P001",
  "participanteNome":  "Maria Silva",
  "participanteEmail": "maria.silva@email.com",
  "palestTitulo":      "Vínculos que Protegem",
  "palestHorario":     "14:00",
  "palestData":        "11",
  "palestLocal":       "Sala A",
  "qrToken":           "<conteúdo lido do QR code físico>"
}
```

> O campo `qrToken` é o conteúdo bruto lido pela câmera. A API deve validar se ele corresponde a um QR code legítimo gerado para aquela palestra.

**Response 200:**
```json
{ "confirmado": true, "mensagem": "Presença registrada com sucesso!" }
```

**Erros:**
| Status | Corpo |
|---|---|
| 400 | `{ "erro": "QR code inválido ou expirado." }` |
| 409 | `{ "erro": "Presença já registrada para este participante." }` |

**Regra no app:** o botão "Confirmar Presença" só fica habilitado dentro de uma janela de **15 minutos antes** até **90 minutos após** o horário de início da palestra. Fora desse período o botão exibe a mensagem "Disponível apenas no horário da palestra (HH:mm)".

> Para desativar a restrição de horário durante desenvolvimento/demonstração, altere `MODO_DEMO = true` em `DetalhesPalestraFragment.java` linha 187.

**Arquivo:** `PresencaRepository.java` → `confirmarPresenca()`

---

### 4.6 Notificações

Todos os endpoints abaixo requerem `Authorization: Bearer {token}`.

---

#### `GET /api/notificacoes`

Retorna o histórico de notificações do participante logado. Deve ser chamado na inicialização do app (após login) para popular o histórico local.

**Response 200:**
```json
[
  {
    "id":        "uuid-v4",
    "titulo":    "Mudança de Horário",
    "corpo":     "A palestra das 15h teve seu horário alterado para às 15:30h.",
    "horario":   "10:30",
    "tipo":      3,
    "lida":      false,
    "timestamp": 1715422200000
  }
]
```

**Tipos de notificação (`tipo`):**

| Valor | Constante | Ícone | Cor |
|---|---|---|---|
| `0` | `TIPO_INFO` | info | azul `#15438B` |
| `1` | `TIPO_MENSAGEM` | chat | verde `#689F38` |
| `2` | `TIPO_PALESTRA` | estrela | azul `#15438B` |
| `3` | `TIPO_ALERTA` | sino | laranja `#E64A19` |

**Arquivo:** `NotificacaoRepository.java`

---

#### `PUT /api/notificacoes/{id}/lida`

Marca uma notificação específica como lida. Chamado quando o participante toca em uma notificação na aba de notificações.

**Response 200:**
```json
{ "ok": true }
```

**Arquivo:** `NotificacaoRepository.java` → `marcarComoLida()`

---

#### `PUT /api/notificacoes/lidas`

Marca **todas** as notificações do participante como lidas. Chamado pelo botão "Ler todas" na aba de notificações.

**Request:**
```json
{ "todas": true }
```

**Response 200:**
```json
{ "ok": true }
```

**Arquivo:** `NotificacaoRepository.java` → `marcarTodasComoLidas()`

---

### 4.7 Mensagens

#### `GET /api/mensagens`

Retorna as mensagens e avisos da organização para o participante logado.

Requer `Authorization: Bearer {token}`.

**Response 200:**
```json
[
  {
    "tipo":    "AVISO",
    "titulo":  "Bem-vindos ao SUAS 360!",
    "corpo":   "Sejam todos muito bem-vindos ao SUAS 360. Aproveitem cada momento!",
    "horario": "10:30",
    "lida":    true
  },
  {
    "tipo":    "CHAT - ORGANIZAÇÃO",
    "titulo":  "Dúvidas Gerais",
    "corpo":   "Olá! Em caso de dúvidas, estamos à disposição neste chat.",
    "horario": "10:30",
    "lida":    false
  }
]
```

**Arquivo:** `MensagemRepository.java`

---

### 4.8 Enquetes

Enquetes de participação em tempo real. Cada enquete pode estar **vinculada a uma palestra** (aparece na tela de detalhes daquela palestra) ou ser **geral** (sem vínculo). Todas as enquetes ativas aparecem na tela **Enquetes ativas**, acessível pela home ("Ver todas") e pelo menu lateral.

> **Vínculo com a palestra:** feito pelo campo `palestraTitulo` — o mesmo padrão de ligação por texto usado no restante do app (palestrante ↔ palestra por nome). Recomenda-se que o backend exponha um `palestraId` estável; enquanto o modelo `Palestra` não possuir id, o vínculo é resolvido pelo título. Enquetes gerais têm `palestraTitulo: null`.

Todos os endpoints abaixo requerem `Authorization: Bearer {token}`.

---

#### `GET /api/enquetes`

Retorna todas as enquetes **ativas** do evento (usado na tela "Enquetes ativas" e no card de destaque da home).

**Response 200:**
```json
[
  {
    "id":            "ENQ001",
    "palestraTitulo": "Vínculos que Protegem",
    "pergunta":      "Qual aspecto dos vínculos comunitários você considera mais urgente?",
    "encerraEm":     "Encerra em 1h 20m",
    "ativa":         true,
    "jaVotou":       false,
    "opcaoVotadaId": null,
    "opcoes": [
      { "id": "OP1", "texto": "Rede de proteção familiar", "votos": 12 },
      { "id": "OP2", "texto": "Articulação territorial",    "votos": 8  },
      { "id": "OP3", "texto": "Participação social",        "votos": 5  }
    ]
  }
]
```

> `encerraEm` é um rótulo já formatado para exibição. `jaVotou`/`opcaoVotadaId` refletem o estado do participante logado — quando `jaVotou` é `true`, o app exibe os resultados (percentuais) em vez dos botões de voto.

**Arquivo:** `EnqueteRepository.java` → construtor `enquetes`

---

#### `GET /api/palestras/{palestraId}/enquetes`

Retorna as enquetes **ativas** vinculadas a uma palestra específica. Usado na tela de detalhes da palestra.

**Response 200:** mesmo formato de `GET /api/enquetes`, filtrado pela palestra.

**Arquivo:** `EnqueteRepository.java` → `getEnquetesByPalestra()`

---

#### `POST /api/enquetes/{enqueteId}/votar`

Registra o voto do participante em uma opção da enquete.

**Request:**
```json
{ "opcaoId": "OP1" }
```

**Response 200:**
```json
{ "ok": true, "totalVotos": 26 }
```

**Erros:**
| Status | Corpo |
|---|---|
| 409 | `{ "erro": "Participante já votou nesta enquete." }` |
| 404 | `{ "erro": "Enquete ou opção não encontrada." }` |

**Regra no app:** após um voto bem-sucedido, os botões de opção são substituídos por barras de resultado com o percentual de cada opção; a opção escolhida é destacada.

**Arquivo:** `EnqueteRepository.java` → `votar()`

---

### 4.9 Avaliações

Ao final de uma palestra, o participante pode avaliar a **palestra** e o **palestrante** (notas de 1 a 5) e deixar um comentário. Na tela de detalhes da palestra, o botão "Avaliar palestra e palestrante" fica visível quando a palestra já terminou.

Requer `Authorization: Bearer {token}`.

---

#### `POST /api/avaliacoes`

**Request:**
```json
{
  "participanteId":   "P001",
  "participanteNome": "Maria Silva",
  "palestraTitulo":   "Vínculos que Protegem",
  "palestraData":     "11",
  "palestranteNome":  "Mariana Torres",
  "notaPalestra":     5,
  "notaPalestrante":  4,
  "comentario":       "Excelente palestra, muito didática."
}
```

> `notaPalestra` e `notaPalestrante` vão de **1 a 5**. `notaPalestrante` é `0` quando a palestra não tem palestrante (ex.: credenciamento, abertura), caso em que o app oculta a nota do palestrante. `comentario` pode ser string vazia.

**Response 200:**
```json
{ "ok": true, "mensagem": "Avaliação registrada. Obrigado!" }
```

**Erros:**
| Status | Corpo |
|---|---|
| 409 | `{ "erro": "Você já avaliou esta palestra." }` |

**Regra no app:** o botão só habilita o envio depois de o participante dar a nota da palestra (e do palestrante, quando houver). Ver `MODO_DEMO_AVALIACAO` na [Seção 8](#8-variáveis-de-configuração-do-app) para forçar a exibição do botão durante a demonstração.

**Arquivo:** `AvaliacaoRepository.java` → `enviarAvaliacao()`

---

## 5. Notificações Push (FCM)

### Fluxo completo

```
1. App inicia / usuário faz login
        ↓
2. Firebase SDK gera token do dispositivo (GeSuasMessagingService.onNewToken)
        ↓
3. App envia token para PUT /api/participante/fcm-token
        ↓
4. API armazena { participanteId, fcmToken } no banco
        ↓
5. Organizador dispara notificação via painel/API
        ↓
6. Backend chama FCM com a Server Key:
   POST https://fcm.googleapis.com/fcm/send
        ↓
7. FCM entrega ao dispositivo → GeSuasMessagingService.onMessageReceived
        ↓
8. App salva em NotificacaoRepository + exibe no sistema + atualiza badge
```

### Payload que o backend deve enviar ao FCM

```
POST https://fcm.googleapis.com/fcm/send
Authorization: key=<FCM_SERVER_KEY>
Content-Type: application/json

{
  "to": "<fcmToken do participante>",
  "data": {
    "titulo": "Aviso dos organizadores",
    "corpo":  "O coffee break começa em 10 minutos no corredor principal.",
    "tipo":   "3"
  },
  "notification": {
    "title": "Aviso dos organizadores",
    "body":  "O coffee break começa em 10 minutos no corredor principal."
  }
}
```

> Incluir tanto `data` quanto `notification` garante entrega em **foreground, background e app fechado**.

### Envio para múltiplos participantes

Para enviar para todos os participantes de uma vez, use o endpoint de multicast do FCM:

```
POST https://fcm.googleapis.com/fcm/send
Authorization: key=<FCM_SERVER_KEY>
Content-Type: application/json

{
  "registration_ids": ["token1", "token2", "token3", ...],
  "data": { ... },
  "notification": { ... }
}
```

> O FCM aceita até **500 tokens por requisição**. Para listas maiores, faça múltiplas chamadas em lotes.

### Tipos de notificação

| `tipo` | Uso sugerido |
|---|---|
| `"0"` | Informativo geral |
| `"1"` | Nova mensagem no chat |
| `"2"` | Atualização relacionada a palestra |
| `"3"` | Alerta urgente (mudança de horário, sala, etc.) |

---

## 6. Modelos de Dados

### Participante

```json
{
  "id":       "P001",
  "nome":     "Maria Silva",
  "email":    "maria.silva@email.com",
  "token":    "eyJhbGciOiJIUzI1NiIsInR5...",
  "telefone": "(11) 99999-9999",
  "cidade":   "São Paulo"
}
```

### Palestra

```json
{
  "horario":         "14:00",
  "data":            "11",
  "local":           "Sala A",
  "titulo":          "Vínculos que Protegem",
  "descricao":       "Conexões humanas que sustentam...",
  "palestranteNome": "Mariana Torres",
  "palestranteBio":  "Assistente Social, Mestre e Doutora..."
}
```

### Palestrante

```json
{
  "nome":     "Mariana Torres",
  "bio":      "Assistente Social, Mestre e Doutora em Serviço Social pela PUC/SP",
  "descricao": "Mariana Torres é doutora em Serviço Social..."
}
```

### DataEvento

```json
{
  "dia":   "11",
  "mes":   "Maio",
  "label": "Segunda-Feira, 11 de Maio"
}
```

### Notificacao

```json
{
  "id":        "3fa85f64-5717-4562-b3fc-2c963f66afa6",
  "titulo":    "Mudança de Horário",
  "corpo":     "A palestra das 15h foi alterada para às 15:30h.",
  "horario":   "10:30",
  "tipo":      3,
  "lida":      false,
  "timestamp": 1715422200000
}
```

### ConfirmacaoPresenca (enviada em POST /api/presenca/confirmar)

```json
{
  "participanteId":    "P001",
  "participanteNome":  "Maria Silva",
  "participanteEmail": "maria.silva@email.com",
  "palestTitulo":      "Vínculos que Protegem",
  "palestHorario":     "14:00",
  "palestData":        "11",
  "palestLocal":       "Sala A",
  "qrToken":           "<conteúdo lido do QR code>"
}
```

### Mensagem

```json
{
  "tipo":    "AVISO",
  "titulo":  "Bem-vindos ao SUAS 360!",
  "corpo":   "Sejam todos muito bem-vindos ao SUAS 360.",
  "horario": "10:30",
  "lida":    false
}
```

### Enquete

```json
{
  "id":             "ENQ001",
  "palestraTitulo": "Vínculos que Protegem",
  "pergunta":       "Qual aspecto dos vínculos comunitários você considera mais urgente?",
  "encerraEm":      "Encerra em 1h 20m",
  "ativa":          true,
  "jaVotou":        false,
  "opcaoVotadaId":  null,
  "opcoes": [
    { "id": "OP1", "texto": "Rede de proteção familiar", "votos": 12 }
  ]
}
```

> `palestraTitulo: null` indica uma enquete geral (não vinculada a uma palestra).

### OpcaoEnquete

```json
{
  "id":    "OP1",
  "texto": "Rede de proteção familiar",
  "votos": 12
}
```

### Avaliacao (enviada em POST /api/avaliacoes)

```json
{
  "participanteId":   "P001",
  "participanteNome": "Maria Silva",
  "palestraTitulo":   "Vínculos que Protegem",
  "palestraData":     "11",
  "palestranteNome":  "Mariana Torres",
  "notaPalestra":     5,
  "notaPalestrante":  4,
  "comentario":       "Excelente palestra, muito didática."
}
```

---

## 7. Como Substituir os Mocks

Cada repositório tem um comentário indicando o endpoint e o formato esperado. Para integrar a API real:

1. Adicione uma dependência de cliente HTTP (ex: Retrofit):
   ```kotlin
   // app/build.gradle.kts
   implementation("com.squareup.retrofit2:retrofit:2.11.0")
   implementation("com.squareup.retrofit2:converter-gson:2.11.0")
   ```

2. Crie uma interface Retrofit espelhando os endpoints desta documentação.

3. Em cada repositório, **substitua o bloco `new Handler(...).postDelayed(...)`** pela chamada Retrofit correspondente, mantendo as chamadas `callback.onSuccess()` e `callback.onError()` nos retornos.

**Exemplo — substituindo o mock de login:**

```java
// AuthRepository.java (atual — mock)
public void login(String email, String senha, AuthCallback callback) {
    new Handler(Looper.getMainLooper()).postDelayed(() -> {
        // ... lógica mock
    }, 1200);
}

// AuthRepository.java (com API real)
public void login(String email, String senha, AuthCallback callback) {
    apiService.login(new LoginRequest(email, senha))
        .enqueue(new Callback<ParticipanteResponse>() {
            @Override public void onResponse(Call<ParticipanteResponse> call, Response<ParticipanteResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ParticipanteResponse r = response.body();
                    callback.onSuccess(new Participante(r.id, r.nome, r.email, r.token));
                } else {
                    callback.onError("Credenciais inválidas.");
                }
            }
            @Override public void onFailure(Call<ParticipanteResponse> call, Throwable t) {
                callback.onError("Erro de conexão.");
            }
        });
}
```

### Mapa de arquivos por endpoint

| Endpoint | Arquivo |
|---|---|
| `POST /api/auth/login` | `AuthRepository.java` → `login()` |
| `PUT /api/participante/email` | `ParticipanteRepository.java` → `atualizarEmail()` |
| `PUT /api/participante/telefone` | `ParticipanteRepository.java` → `atualizarTelefone()` |
| `PUT /api/participante/cidade` | `ParticipanteRepository.java` → `atualizarCidade()` |
| `PUT /api/participante/fcm-token` | `ParticipanteRepository.java` → `registrarFcmToken()` |
| `GET /api/palestras` | `PalestraRepository.java` → construtor `palestrasMock` |
| `GET /api/evento/dias` | `PalestraRepository.java` → `getDiasDoEvento()` |
| `GET /api/palestrantes` | `PalestranteRepository.java` → construtor `palestrantes` |
| `POST /api/presenca/confirmar` | `PresencaRepository.java` → `confirmarPresenca()` |
| `GET /api/notificacoes` | `NotificacaoRepository.java` → construtor |
| `PUT /api/notificacoes/{id}/lida` | `NotificacaoRepository.java` → `marcarComoLida()` |
| `PUT /api/notificacoes/lidas` | `NotificacaoRepository.java` → `marcarTodasComoLidas()` |
| `GET /api/mensagens` | `MensagemRepository.java` → construtor `mensagens` |
| `GET /api/enquetes` | `EnqueteRepository.java` → construtor `enquetes` |
| `GET /api/palestras/{palestraId}/enquetes` | `EnqueteRepository.java` → `getEnquetesByPalestra()` |
| `POST /api/enquetes/{enqueteId}/votar` | `EnqueteRepository.java` → `votar()` |
| `POST /api/avaliacoes` | `AvaliacaoRepository.java` → `enviarAvaliacao()` |
| FCM push (backend → app) | `GeSuasMessagingService.java` → `onMessageReceived()` |

---

## 8. Variáveis de Configuração do App

| Constante | Arquivo | Valor padrão | Descrição |
|---|---|---|---|
| `MODO_DEMO` | `DetalhesPalestraFragment.java` | `true` | `true` = botão "Confirmar Presença" sempre disponível. Para produção, setar `false`. |
| `MODO_DEMO_AVALIACAO` | `DetalhesPalestraFragment.java` | `true` | `true` = botão "Avaliar palestra e palestrante" sempre visível. Para produção, setar `false` (aparece só 90 min após o início da palestra). |
| `DELAY_SIMULADO_MS` | `AuthRepository.java:38` | `1200` | Delay em ms do mock de login. Remover ao integrar API real. |
| `CANAL_ID` | `GeSuasMessagingService.java:28` | `"gesuas_channel"` | ID do canal de notificação Android. Deve coincidir com o `default_notification_channel_id` no `AndroidManifest.xml`. |
| `google-services.json` | `app/` | placeholder | Substituir pelo arquivo real do Firebase Console para ativar FCM. |
