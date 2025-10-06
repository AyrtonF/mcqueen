# 📧 Mcqueen - Serviço de Email

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.6-green)
![Maven](https://img.shields.io/badge/Maven-3.8+-blue)
![Docker](https://img.shields.io/badge/Docker-Ready-blue)

## 📋 Descrição

O **Mcqueen** é um serviço RESTful desenvolvido em Spring Boot para envio de emails contendo dados de formulários e anexos CSV. O sistema oferece uma API robusta para integração com aplicações que necessitam de funcionalidades de envio de email com anexos de forma segura e auditável.

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
   spring.mail.host=smtp.servidor.com
   spring.mail.port=587
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
   docker run -p 8080:8080 \
     -e SPRING_MAIL_USERNAME=seu-email@dominio.com \
     -e SPRING_MAIL_PASSWORD=sua-senha \
     mcqueen-email-service
   ```

## 📡 Endpoints da API

### 🔗 Endpoint Principal

#### `POST /api/emails/send`

Envia email com dados do formulário e anexos CSV.

**Content-Type:** `multipart/form-data`

**Parâmetros:**

- `organizationName` (string, obrigatório): Nome da organização
- `responsibleContact` (string, obrigatório): Email do responsável
- `subject` (string, obrigatório): Assunto do formulário
- `referencePeriod` (string, obrigatório): Período dos dados
- `dataDescription` (string, obrigatório): Descrição detalhada
- `lgpdCompliance` (boolean): Confirmação LGPD
- `files` (files, obrigatório): Um ou mais arquivos CSV
- `recipient` (string, opcional): Email de destino

### 🔗 Endpoints de Consulta

#### `GET /api/emails/history`

Consulta histórico de emails enviados.

**Parâmetros opcionais:**

- `startDate`: Data início (yyyy-MM-ddTHH:mm:ss)
- `endDate`: Data fim (yyyy-MM-ddTHH:mm:ss)

#### `GET /api/emails/recent`

Retorna emails enviados nas últimas 24 horas.

#### `GET /api/emails/health`

Health check do serviço.

## 🧪 Testando a API

### Usando cURL

```bash
curl -X POST http://localhost:8080/api/emails/send \
  -H "Content-Type: multipart/form-data" \
  -F "organizationName=Exemplo Org" \
  -F "responsibleContact=responsavel@exemplo.com" \
  -F "subject=Dados Exemplo" \
  -F "referencePeriod=2024-2025" \
  -F "dataDescription=Descrição dos dados exemplo" \
  -F "lgpdCompliance=true" \
  -F "files=@dados1.csv" \
  -F "files=@dados2.csv"
```

### Usando Postman

Consulte o arquivo `docs/postman-collection-updated.json` para importar a coleção completa.

## 🔧 Configuração

### SMTP

Configure o servidor SMTP no arquivo `application.properties`:

```properties
spring.mail.host=smtp.servidor.com
spring.mail.port=587
spring.mail.username=usuario@dominio.com
spring.mail.password=senha
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
spring.datasource.username=db_user
spring.datasource.password=db_password
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
  "message": "Invalid form data",
  "path": "/api/emails/send",
  "timestamp": "2025-10-06T10:30:00",
  "details": ["organizationName: cannot be empty"]
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

## 📞 Suporte

Para suporte, abra uma issue no repositório do projeto.

---

**Desenvolvido com ❤️ em Spring Boot**
