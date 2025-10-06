# 📧 Mcqueen - Serviço de Email

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.6-green)
![Maven](https://img.shields.io/badge/Maven-3.8+-blue)
![Docker](https://img.shields.io/badge/Docker-Ready-blue)

## 📋 Descrição

O **Mcqueen** é um serviço RESTful desenvolvido em Spring Boot para envio de emails contendo dados de formulários e anexos CSV. Foi projetado especificamente para atender às necessidades de órgãos governamentais que precisam enviar dados estruturados via email de forma segura e auditável.

## ✨ Características Principais

- 📤 **Envio de emails** com dados de formulário estruturados
- 📎 **Suporte a múltiplos anexos CSV** (até 100MB total, 10MB por arquivo)
- ✅ **Validação robusta** de dados e arquivos
- 🔐 **Configuração SMTP segura** com TLS
- 📊 **Auditoria completa** de todos os emails enviados
- 🐳 **Docker Ready** para fácil deploy
- 📚 **Documentação automática** com Swagger/OpenAPI
- 🏗️ **Arquitetura modular** com separação de responsabilidades
- 🛡️ **Tratamento global de exceções**
- ✨ **Conformidade com LGPD**

## 🏗️ Arquitetura

O projeto segue uma arquitetura em camadas bem definida:

```
src/main/java/com/join/Mcqueen/
├── 📁 controllers/          # Camada de apresentação (REST endpoints)
├── 📁 services/             # Camada de negócio
├── 📁 repositories/         # Camada de acesso a dados
├── 📁 domain/
│   ├── 📁 dtos/            # Data Transfer Objects
│   └── 📁 models/          # Entidades JPA
├── 📁 mappers/             # Conversores entre DTOs e Entities
├── 📁 config/              # Configurações da aplicação
└── 📁 exceptions/          # Exceções personalizadas e handlers
```

## 🚀 Início Rápido

### Pré-requisitos

- ☕ Java 17+
- 📦 Maven 3.8+
- 🐳 Docker (opcional)

### Instalação Local

1. **Clone o repositório:**

   ```bash
   git clone <repository-url>
   cd Mcqueen
   ```

2. **Configure as credenciais de email** no `application.properties`:

   ```properties
   spring.mail.username=seu-email@dominio.com
   spring.mail.password=sua-senha
   ```

3. **Execute a aplicação:**

   ```bash
   ./mvnw spring-boot:run
   ```

4. **Acesse a documentação:**
   - Swagger UI: http://localhost:8080/swagger-ui.html
   - API Docs: http://localhost:8080/api-docs

### Deploy com Docker

1. **Build da imagem:**

   ```bash
   docker build -t mcqueen-email-service .
   ```

2. **Execute o container:**
   ```bash
   docker run -p 8080:8080 mcqueen-email-service
   ```

### Deploy no Render

1. **Configure as variáveis de ambiente no Render:**

   ```
   SPRING_MAIL_USERNAME=gabriel.almeida1@sad.pe.gov.br
   SPRING_MAIL_PASSWORD=86116123
   SPRING_MAIL_HOST=smtps.expresso.pe.gov.br
   SPRING_MAIL_PORT=587
   ```

2. **Use o Dockerfile** para build automático no Render

## 📡 Endpoints da API

### 🔗 Endpoint Principal

#### `POST /api/emails/enviar`

Envia email com dados do formulário e anexos CSV.

**Content-Type:** `multipart/form-data`

**Parâmetros:**

- `nomeOrgao` (string, obrigatório): Nome do órgão responsável
- `contatoResponsavel` (string, obrigatório): Email do responsável
- `tema` (string, obrigatório): Tema do formulário
- `periodoReferencia` (string, obrigatório): Período dos dados (ex: 2022-2024)
- `descricaoDado` (string, obrigatório): Descrição detalhada
- `conformidadeLGPD` (boolean): Confirmação LGPD
- `arquivos` (files, obrigatório): Um ou mais arquivos CSV
- `destinatario` (string, opcional): Email de destino

### 🔗 Endpoints de Consulta

#### `GET /api/emails/historico`

Consulta histórico de emails enviados.

**Parâmetros opcionais:**

- `inicio`: Data início (yyyy-MM-ddTHH:mm:ss)
- `fim`: Data fim (yyyy-MM-ddTHH:mm:ss)

#### `GET /api/emails/recentes`

Retorna emails enviados nas últimas 24 horas.

#### `GET /api/emails/health`

Health check do serviço.

## 🧪 Testando a API

### Usando cURL

```bash
curl -X POST http://localhost:8080/api/emails/enviar \
  -H "Content-Type: multipart/form-data" \
  -F "nomeOrgao=Secretaria da Saúde" \
  -F "contatoResponsavel=responsavel@saude.pe.gov.br" \
  -F "tema=Dados de Saúde Pública" \
  -F "periodoReferencia=2022-2024" \
  -F "descricaoDado=Dados estatísticos de atendimentos hospitalares" \
  -F "conformidadeLGPD=true" \
  -F "arquivos=@dados1.csv" \
  -F "arquivos=@dados2.csv"
```

### Usando Postman

Consulte o arquivo `docs/postman-collection.json` para importar a coleção completa.

## 🔧 Configuração

### SMTP

O serviço está configurado para usar o servidor SMTP do Expresso PE:

```properties
spring.mail.host=smtps.expresso.pe.gov.br
spring.mail.port=587
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

### Upload de Arquivos

```properties
spring.servlet.multipart.max-file-size=10MB    # Máximo por arquivo
spring.servlet.multipart.max-request-size=100MB # Máximo total
```

### Banco de Dados

Por padrão, usa H2 em memória para auditoria. Para produção, configure um banco persistente:

```properties
# PostgreSQL (exemplo)
spring.datasource.url=jdbc:postgresql://localhost:5432/mcqueen
spring.datasource.username=usuario
spring.datasource.password=senha
spring.jpa.hibernate.ddl-auto=update
```

## 📊 Auditoria e Logs

Todos os emails enviados são auditados na tabela `email_audit` com as seguintes informações:

- 📧 Destinatário e assunto
- 🏢 Dados do órgão e responsável
- 📄 Informações dos arquivos anexados
- ✅ Status do envio (SUCCESS/ERROR)
- 🕐 Data e hora do envio
- 🔒 Conformidade LGPD

## 🔐 Segurança

- ✅ Validação de entrada com Bean Validation
- ✅ Sanitização de dados HTML em emails
- ✅ Validação de tipos de arquivo (apenas CSV)
- ✅ Limitação de tamanho de arquivos
- ✅ Execução com usuário não-root no Docker
- ✅ Logs estruturados para auditoria

## 🐛 Tratamento de Erros

A API retorna erros padronizados no formato:

```json
{
  "status": 400,
  "error": "Validation Error",
  "message": "Dados inválidos no formulário",
  "path": "/api/emails/enviar",
  "timestamp": "2025-10-06T10:30:00",
  "details": ["nomeOrgao: não pode estar vazio"]
}
```

## 📚 Documentação Adicional

- 📋 [API Documentation](docs/API_DOCUMENTATION.md) - Documentação detalhada das rotas
- 🧪 [Postman Collection](docs/postman-collection.json) - Coleção para testes
- 🔗 [Swagger UI](http://localhost:8080/swagger-ui.html) - Documentação interativa

## 🤝 Contribuição

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## 📄 Licença

Este projeto está licenciado sob a Licença MIT - veja o arquivo [LICENSE](LICENSE) para detalhes.

## 👥 Equipe

- **Ayrton** - Desenvolvimento principal
- **Gabriel Almeida** - Configuração SMTP e Deploy

## 📞 Suporte

Para suporte, entre em contato através do email: gabriel.almeida1@sad.pe.gov.br

---

**Desenvolvido com ❤️ para o Governo de Pernambuco**
