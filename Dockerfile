# Usar uma imagem base com OpenJDK 17
FROM openjdk:17-jdk-slim

# Definir informações de metadados
LABEL maintainer="Ayrton - Mcqueen Email Service"
LABEL version="1.0"
LABEL description="Serviço de envio de emails com formulários e anexos CSV"

# Definir diretório de trabalho
WORKDIR /app

# Copiar arquivos Maven
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Dar permissão de execução para o Maven wrapper
RUN chmod +x ./mvnw

# Baixar dependências (cache layer)
RUN ./mvnw dependency:go-offline -B

# Copiar código fonte
COPY src ./src

# Compilar a aplicação
RUN ./mvnw clean package -DskipTests

# Criar um usuário não-root para segurança
RUN addgroup --system --gid 1001 mcqueen
RUN adduser --system --uid 1001 --gid 1001 mcqueen

# Mudar para o usuário não-root
USER mcqueen

# Expor a porta da aplicação
EXPOSE 8080

# Definir variáveis de ambiente para otimização da JVM
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=70.0 -XX:+UseG1GC -XX:+UseStringDeduplication"

# Comando para executar a aplicação
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar target/*.jar"]

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8080/api/emails/health || exit 1