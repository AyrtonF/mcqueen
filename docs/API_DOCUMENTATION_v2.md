# 📚 API Documentation - Mcqueen Email Service

Esta documentação fornece informações detalhadas sobre todas as rotas disponíveis na API do Mcqueen, incluindo exemplos de requisições e respostas.

## 📋 Índice

1. [Visão Geral](#visao-geral)
2. [Autenticação](#autenticacao)
3. [Endpoints](#endpoints)
4. [Modelos de Dados](#modelos-de-dados)
5. [Códigos de Status](#codigos-de-status)
6. [Exemplos de Uso](#exemplos-de-uso)

## 🔍 Visão Geral

**Base URL:** `http://localhost:8080/api/emails`

**Content-Type:** `multipart/form-data` (para upload de arquivos)

**Versão da API:** 2.0.0

**⚠️ IMPORTANTE:** Todos os parâmetros da API foram atualizados para inglês na versão 2.0.0

## 🔐 Autenticação

Esta API não requer autenticação para uso interno. Para produção, considere implementar autenticação JWT ou API Key.

## 🛣️ Endpoints

### 1. Enviar Email com Formulário

#### `POST /api/emails/send`

Endpoint principal para envio de emails contendo dados do formulário e anexos CSV.

**Parâmetros do Formulário:**

| Campo                | Tipo    | Obrigatório | Descrição                 | Exemplo                                 |
| -------------------- | ------- | ----------- | ------------------------- | --------------------------------------- |
| `organizationName`   | String  | ✅          | Nome do órgão responsável | "Secretaria da Saúde"                   |
| `responsibleContact` | String  | ✅          | Email do responsável      | "responsavel@saude.pe.gov.br"           |
| `subject`            | String  | ✅          | Tema do formulário        | "Dados de Saúde Pública"                |
| `referencePeriod`    | String  | ✅          | Período dos dados         | "2022-2024"                             |
| `dataDescription`    | String  | ✅          | Descrição detalhada       | "Dados estatísticos de atendimentos..." |
| `lgpdCompliance`     | Boolean | ❌          | Confirmação LGPD          | true                                    |
| `files`              | File[]  | ✅          | Arquivos CSV              | arquivo1.csv, arquivo2.csv              |
| `recipient`          | String  | ❌          | Email de destino          | "destino@exemplo.com"                   |

**Resposta de Sucesso (200):**

```json
{
  "status": "SUCCESS",
  "message": "Email enviado com sucesso",
  "sendDate": "2025-10-06T14:30:00",
  "recipient": "gabriel.almeida1@sad.pe.gov.br",
  "fileCount": 2,
  "fileNames": ["dados_saude.csv", "estatisticas.csv"]
}
```

**Exemplo de Requisição cURL:**

```bash
curl -X POST "http://localhost:8080/api/emails/send" \
  -F "organizationName=Secretaria da Saúde" \
  -F "responsibleContact=responsavel@saude.pe.gov.br" \
  -F "subject=Dados de Saúde Pública" \
  -F "referencePeriod=2022-2024" \
  -F "dataDescription=Dados estatísticos de atendimentos hospitalares" \
  -F "lgpdCompliance=true" \
  -F "files=@dados_saude.csv" \
  -F "recipient=destino@exemplo.com"
```

---

### 2. Enviar Email com JSON + Multipart

#### `POST /api/emails/send-json`

Alternativa para envio usando JSON para os dados do formulário e multipart para arquivos.

**Parâmetros:**

| Campo       | Tipo        | Obrigatório | Descrição                   |
| ----------- | ----------- | ----------- | --------------------------- |
| `formData`  | JSON Object | ✅          | Dados do formulário em JSON |
| `files`     | File[]      | ✅          | Arquivos CSV                |
| `recipient` | String      | ❌          | Email de destino (opcional) |

**Estrutura do JSON (formData):**

```json
{
  "organizationName": "Secretaria de Educação",
  "responsibleContact": "educacao@pe.gov.br",
  "subject": "Dados Educacionais",
  "referencePeriod": "2023-2024",
  "dataDescription": "Estatísticas de matrículas e aprovações",
  "lgpdCompliance": true
}
```

**Exemplo de Requisição cURL:**

```bash
curl -X POST "http://localhost:8080/api/emails/send-json" \
  -F 'formData={"organizationName":"Secretaria de Educação","responsibleContact":"educacao@pe.gov.br","subject":"Dados Educacionais","referencePeriod":"2023-2024","dataDescription":"Estatísticas de matrículas","lgpdCompliance":true};type=application/json' \
  -F "files=@matriculas.csv" \
  -F "recipient=destino@exemplo.com"
```

---

### 3. Consultar Histórico de Emails

#### `GET /api/emails/history`

Retorna o histórico de emails enviados, opcionalmente filtrado por período.

**Parâmetros de Query (Opcionais):**

| Parâmetro   | Tipo     | Descrição                         | Exemplo             |
| ----------- | -------- | --------------------------------- | ------------------- |
| `startDate` | DateTime | Data de início (formato ISO 8601) | 2025-10-01T00:00:00 |
| `endDate`   | DateTime | Data de fim (formato ISO 8601)    | 2025-10-31T23:59:59 |

**Resposta de Sucesso (200):**

```json
[
  {
    "id": 1,
    "recipient": "gabriel.almeida1@sad.pe.gov.br",
    "emailSubject": "Envio de Dados - Dados de Saúde Pública - Secretaria da Saúde",
    "organizationName": "Secretaria da Saúde",
    "responsibleContact": "responsavel@saude.pe.gov.br",
    "subject": "Dados de Saúde Pública",
    "referencePeriod": "2022-2024",
    "dataDescription": "Dados estatísticos de atendimentos hospitalares",
    "lgpdCompliance": true,
    "fileCount": 1,
    "fileNames": "dados_saude.csv",
    "sendStatus": "SUCCESS",
    "sendDate": "2025-10-06T14:30:00"
  }
]
```

**Exemplo de Requisição:**

```bash
# Histórico completo
curl "http://localhost:8080/api/emails/history"

# Histórico filtrado por período
curl "http://localhost:8080/api/emails/history?startDate=2025-10-01T00:00:00&endDate=2025-10-31T23:59:59"
```

---

### 4. Consultar Emails Recentes

#### `GET /api/emails/recent`

Retorna os emails enviados nas últimas 24 horas.

**Resposta de Sucesso (200):**

```json
[
  {
    "id": 2,
    "recipient": "destinatario@exemplo.com",
    "emailSubject": "Envio de Dados - Dados Educacionais - Secretaria de Educação",
    "organizationName": "Secretaria de Educação",
    "sendStatus": "SUCCESS",
    "sendDate": "2025-10-06T10:15:00"
  }
]
```

**Exemplo de Requisição:**

```bash
curl "http://localhost:8080/api/emails/recent"
```

---

### 5. Health Check

#### `GET /api/emails/health`

Verifica se o serviço está funcionando corretamente.

**Resposta de Sucesso (200):**

```
Serviço de email funcionando normalmente
```

**Exemplo de Requisição:**

```bash
curl "http://localhost:8080/api/emails/health"
```

---

## 📊 Modelos de Dados

### EmailFormDTO

Estrutura dos dados do formulário:

```json
{
  "organizationName": "string", // Nome do órgão (obrigatório)
  "responsibleContact": "string", // Email do responsável (obrigatório)
  "subject": "string", // Tema dos dados (obrigatório)
  "referencePeriod": "string", // Período de referência (obrigatório)
  "dataDescription": "string", // Descrição detalhada (obrigatório)
  "lgpdCompliance": "boolean" // Confirmação LGPD (opcional, padrão: false)
}
```

### EmailResponseDTO

Estrutura da resposta de envio:

```json
{
  "status": "SUCCESS|ERROR", // Status do envio
  "message": "string", // Mensagem descritiva
  "sendDate": "2025-10-06T14:30:00", // Data/hora do envio
  "recipient": "string", // Email de destino
  "fileCount": 1, // Quantidade de arquivos
  "fileNames": ["arquivo1.csv"] // Nomes dos arquivos
}
```

### EmailAudit

Estrutura do histórico de emails:

```json
{
  "id": 1, // ID único do registro
  "recipient": "string", // Email de destino
  "emailSubject": "string", // Assunto do email
  "organizationName": "string", // Nome do órgão
  "responsibleContact": "string", // Email do responsável
  "subject": "string", // Tema dos dados
  "referencePeriod": "string", // Período de referência
  "dataDescription": "string", // Descrição dos dados
  "lgpdCompliance": true, // Confirmação LGPD
  "fileCount": 1, // Quantidade de arquivos
  "fileNames": "string", // Nomes dos arquivos (separados por vírgula)
  "sendStatus": "SUCCESS|ERROR", // Status do envio
  "sendDate": "2025-10-06T14:30:00" // Data/hora do envio
}
```

---

## 🔢 Códigos de Status

| Código | Descrição              | Situação                                        |
| ------ | ---------------------- | ----------------------------------------------- |
| 200    | OK                     | Requisição processada com sucesso               |
| 400    | Bad Request            | Dados inválidos ou campos obrigatórios faltando |
| 413    | Payload Too Large      | Arquivo muito grande (>10MB por arquivo)        |
| 415    | Unsupported Media Type | Tipo de arquivo não suportado (apenas CSV)      |
| 500    | Internal Server Error  | Erro interno do servidor                        |

---

## 🛠️ Exemplos de Uso

### Exemplo 1: Envio Básico com um Arquivo

```bash
curl -X POST "http://localhost:8080/api/emails/send" \
  -F "organizationName=Secretaria da Fazenda" \
  -F "responsibleContact=fazenda@pe.gov.br" \
  -F "subject=Dados Fiscais" \
  -F "referencePeriod=2024" \
  -F "dataDescription=Relatórios de arrecadação mensal" \
  -F "lgpdCompliance=true" \
  -F "files=@arrecadacao_2024.csv"
```

### Exemplo 2: Envio com Múltiplos Arquivos

```bash
curl -X POST "http://localhost:8080/api/emails/send" \
  -F "organizationName=Secretaria de Educação" \
  -F "responsibleContact=educacao@pe.gov.br" \
  -F "subject=Dados Escolares" \
  -F "referencePeriod=2023-2024" \
  -F "dataDescription=Estatísticas de matrículas e aprovações" \
  -F "lgpdCompliance=true" \
  -F "files=@matriculas.csv" \
  -F "files=@aprovacoes.csv" \
  -F "recipient=analista@educacao.gov.br"
```

### Exemplo 3: Consulta de Histórico Filtrada

```bash
curl "http://localhost:8080/api/emails/history?startDate=2025-10-01T00:00:00&endDate=2025-10-06T23:59:59"
```

---

## 📝 Notas Importantes

1. **Tamanho dos Arquivos**:

   - Máximo 10MB por arquivo
   - Máximo 100MB total por requisição

2. **Tipos de Arquivo Suportados**:

   - Apenas arquivos CSV (.csv)

3. **Validações**:

   - Todos os campos marcados como obrigatórios devem ser preenchidos
   - Arquivos devem ter a extensão .csv
   - Email do responsável deve ter formato válido

4. **Configurações SMTP**:

   - O serviço utiliza SMTP configurado no `application.properties`
   - Email padrão: gabriel.almeida1@sad.pe.gov.br

5. **Logs de Auditoria**:
   - Todos os envios são registrados no banco H2
   - Disponível consulta via endpoint `/history`

---

## 🔗 Links Úteis

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **API Docs**: http://localhost:8080/api-docs
- **H2 Console**: http://localhost:8080/h2-console
- **Health Check**: http://localhost:8080/api/emails/health

---

## 🆕 Changelog

### v2.0.0 (06/10/2025)

- ✅ Todos os parâmetros da API convertidos para inglês
- ✅ Rotas atualizadas: `/enviar` → `/send`, `/enviar-json` → `/send-json`
- ✅ Parâmetros atualizados: `nomeOrgao` → `organizationName`, etc.
- ✅ Configuração web adicionada para melhor roteamento
- ✅ Documentação completamente atualizada

### v1.0.0 (Versão anterior)

- Parâmetros em português
- Rotas em português
