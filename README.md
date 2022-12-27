# Curso: Domine microservicos e mensageria com Spring Cloud e Docker

## Microservices

### :arrow_right: eurekaserver: 
Microsserviço que realizará o gerencimento das instâncias criadas do microsserviço msclientes e mscartoes.

### :arrow_right: mscloudgateway: 
Microsserviço que será responsável por receber todas as requisisões provinientes da arquitetura citada(eurekaserver, msclientes, mscartoes)
realizando dessa forma, o roteamento/encaminhamento para a instância do microsserviço adequado, ou seja, que possui menos requisições. Ademais, realiza o controle das portas de cada microsserviço.

### :arrow_right: msclientes: 
Microsserviço que realiza a lógica da aplicação para o cadastro dos clientes, com as devidas regras de negócio e endpoints. Além disso, o microsserviço do tipo _client_ poderá 
fornecer várias instâncias do mesmo e será gerenciado pelo eureka server.

### :arrow_right: mscartoes: 
Microsserviço que realiza a lógica da aplicação para os tipos de pagamento, com as devidas regras de negócio e endpoints. Além disso, o microsserviço do tipo _client_ poderá 
fornecer várias instâncias do mesmo e será gerenciado pelo eureka server.

### :arrow_right: msavaliadorcredito: 
Microsserviço que tem como principal função, realizar comunicações síncronas com o OpenFeign entre os microservices **msclientes** e **mscartoes**. Além disso, o microsserviço do tipo _client_ poderá 
fornecer várias instâncias do mesmo e será gerenciado pelo eureka server.

----

## Arquitetura
![arquitetura_inicial](https://user-images.githubusercontent.com/61791877/195881284-7f691772-fec4-46dc-8500-f1cc1906994b.png)

---

### Seção 2 - Desenvolvendo a arquitetura de microservices

### Testando Load Balancer

1. Subir, por exemplo, 3 instâncias no msclientes, por meio do comando ```./mvnw spring-boot:run```
2. Subir mscloudgateway
3. Realizar uma requisição e observar por meio dos logs das 3 instâncias, em qual delas a requisição foi direcionada
4. Realizar a ação novamente para observar tais comportamentos

---

### Seção 3 - Comunicação síncrona entre microservices

**OpenFeign**

É uma biblioteca pertencente ao Spring Cloud que consiste em realizar comunicações diretas entre microservices.

Implementação:
1. Inserir na classe de execução a seguinte anotação **@EnableFeignClients**

2. Criar interface para agrupar a assinatura das requisições à serem consumidas

    2.1 Anotar a interface com @FeignClient(value = "_referência microservice presente no load balancer do gateway(ou url="localhost:8080")_", path = "_endpoint a ser consumido_") 
  
    2.2 Adicionar os contratos copiando a assinatura do método em que se deseja acessar

---

### Seção 4 - Comunicação assíncrona entre microservices

**RabbitMQ**


-> Informações sobre cada tipo de exchange: https://github.com/azl6/udemy-rabbitmq

-> Configs iniciais:

- Comando para rodar Rabbit com Docker Run: ```docker run -it --rm --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3.10-management```
- Adicionar dependência do RabbitMQ no arquivo pom.xml
- Adicionar dependência AMQP no arquivo pom.xml
- Anotar classe da aplicação com ```@EnableRabbit```

-> Implementação para consumer:

- Criar fila na interface do RabbitMQ e realizar o binding entre exchange e fila

- Configurar broker do RabbitMQ e associar o nome da fila criada no application.yaml, seguindo a estrutura:

```
spring:
    application:
        name: mscartoes
    rabbit:
        host: localhost
        port: 5672
        username: guest
        password: guest
mq:
    queues:
        nome-da-fila: nome-da-fila
```        
- Criar classe(s) listener com método para consumo da mensagem, exemplo: 

```
//chamar método em service-repository
@RabbitListener(queues = "${mq.queues.nome-da-fila}")
  public void receiveMessage(final Message message) throws IOException {
    final var object = objectMapper.readValue(message.getBody(), CardEmissionRequestMessage.class);
}
```

-> Implementação para producer:

- Replicar configurações realizadas no consumer

- Adicionar classe de configuração para referência à fila do consumer, exemplo:

```
@Configuration
public class RabbitConfig() {
    
    @Value("${mq.queues.nome-da-fila}")
    private String nomeDaFila;
    
    @Bean
    public Queue nomeDaFilaQueue(){
        return new Queue(nomeDaFila, true);
    }
}

```

- Criar classe(s) producer com método para a publicação da mensagem, exemplo: 

Injetar classes:
```RabbitTemplate rabbitTemplate```
```Queue queueName```
```ObjectMapper objectMapper```

```
//chamar método em service - controller
public void publishMessage(Objeto objeto) throws JsonProcessingException{
    rabbitTemplate.convertAndSend(queueName, objectMapper.convertValueAsString(objeto));
}
```

---

### Seção 5 - Segurança da API com keycloak

Startar container do keycloak: https://www.keycloak.org/getting-started/getting-started-docker

**REALM**

Para criar um controle de acesso, é necessário criar um Realm. Tal recurso signifca uma aplicação, um conjunto de aplicações ou um domínio.

**Criar client para acessar aplicação**

- Inserir o nome do clientID de acordo com o nome do microservice, no caso usaremos o msavaliadorcredito.
- Adicionar **openid-connect** para o campo Client Protocol.
- Adicionar **confidential** para o campo Access Type.
- Habilitar Service Accounts Enable.
- Habilitar Authorization Enable.
- Inserir URL http://localhost:8080 para o campo Valid Redirect URIs.

**Configurando microservice para conectar-se ao keycloak**

- Adicionar dependência:

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-oauth2-resource-server</artifactId>
</dependency>
```

- Buscar informações na página do keycloak: Realm Settings -> OpenID Endpoint Configuration

- Adicionar configuração para application.yaml:

![image](https://user-images.githubusercontent.com/61791877/202879129-8e7e911b-f51a-4319-98a9-b18d133aafcf.png)

**Testes**

- Obter token do keycloak no Postman: 

    - Authorization
    - Grant Type(Client Credentials)
    - Página do keycloak -> Realm Settings -> OpenID Endpoint Configuration -> token_endpoint
    - Inserir token no campo Access Token URL
    - Página do keycloak -> Clients -> microservice criado -> Settings(obter Client ID) -> Credentials(obter Client Secret)
    - Gerar token e utilizar o mesmo nas requisições

- Estender tempo de expiração do token

    - Página do keycloak -> Clients -> microservice criado -> Settings -> Advanced Settings

---

### Seção 6 - Outros recursos

Nesta seção, iremos implementar o Spring Security no microservice eurekaserver, utilizando a autenticação do tipo basic. Ademais, iremos aprender outros recursos, tais como logs, actuator e documentação de API's.

**Spring security**

- Adicionar depêndencia do Spring Security microservice eurekaserver

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>

```

- Adicionar configurações no application.yaml

![image](https://user-images.githubusercontent.com/61791877/202927158-47d82f93-2be5-4229-8a4b-b64405b4f741.png)

- Criar classe de configuração

![image](https://user-images.githubusercontent.com/61791877/202927430-2830b785-e173-4bf2-80ce-d25c5c7e48ee.png)

Com a implementação de autenticação no microservice eurekaserver, os demais microservices que tentarem se registrar, não terão permissão. Dessa forma, deve-se aplicar algumas configurações nos demais microservices para que os mesmos tenham acesso ao eurekaserer.

- Adicionar user e password na url contida em **defaultZone** do application.yaml

```
http://username:password@localhost:8761/eureka

```

**Logs**

```
private static final Logger log = LoggerFactory.getLogger(ClassName.class)
```

**Acutator**

Corresponde ao monitoramento dos microservices através da exposição de endpoints.

- Adicionar dependência do actuator

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>

```

- Adicionar configurações para actuator e logs no application.yaml

![image](https://user-images.githubusercontent.com/61791877/203884906-5a58bc1f-fb47-4f0e-b81f-104830347a0c.png)

- Acessar URL do microservice através do eureka: localhost:xxxx/actuator

**OpenAPI e Swagger**

https://springdoc.org/

---

### Seção 7 - arquitetura completa com Docker

dockerhub: https://hub.docker.com/u/brunadelmouro

**Construindo imagens para os microservices**

- Gerar .jar do projeto(pasta target), com o comando(raiz do projeto):
Para windows
```
./mvnw clean package --DskipTests
```

- Verificar se o jar do microservice gerado está funcionando:

```
cd target
```

```
java -jar .\nome-do-jar.jar
```

- Na raiz do projeto, criar o Dockerfile, contendo as instruções para rodar o microservice

```
FROM openjdk:11
WORKDIR /app
COPY ./target/nome-do-jar app.jar
EXPOSE 8761
ENTRYPOINT java -jar app.jar
```

- Fazer build da imagem

```
docker build --tag nome-da-imagem:1.0 .
```

- Testar imagem

```
docker run --name nome-do-container -p 8761:8761 nome-da-imagem
```

- Subir imagem para o DockerHub

- Para criar uma arquitetura completa, por exemplo, preciso rodar imagens de 2 microservices e rabbitMQ. Para isso, pode-se utilizar o docker-compose.yaml, que agrupará todo esse conjunto. Lembrando que os microservices devem estar no DockerHub.

**Automatização de build das imagens**

- Na raiz do projeto, criar o Dockerfile, contendo as instruções para rodar o maven e microservice

```
FROM maven:3.8.5-openjdk-11 as build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:11
WORKDIR /app
COPY --from=build ./app/target/*.jar ./app.jar
EXPOSE 8761
ENTRYPOINT java -jar app.jar
```

- Fazer build da imagem

```
docker build --tag nome-da-imagem:1.0 .
```

- Testar imagem

```
docker run --name nome-do-container -p 8761:8761 nome-da-imagem
```

**Problema na conversação entre containers**

Por padrão, containers rodam de maneia isolada. Para o exemplo do projeto, quando o microservice precisa se registrar no microservice do eureka server, não há um canal de conexão para que isso aconteça, pois o mesmo aponta para localhost. Para isso, é necessário criar uma NETWORK, onde todos os containers estarão conectados a mesma rede e poderão se comunicarem.

- Por padrão, o nome do host(nome do container) será usado para substituir o localhost referenciado nos MS's, é o nome do container

- Para criar a network

```
docker network create nome-da-network
```

- Para visualizar a network

```
docker network ls
```

- Agora, cada container deverá ser iniciado seguindo a conexão com a Network criada


```
docker run --name nome-do-container -p 8761:8761 --network nome-da-network nome-da-imagem
```

**Criando variáveis de ambiente para referenciar containers**

- Formato de variáveis no Spring

exemplos
```
${RABBITMQ_SERVER}
${EUREKA_SERVER}
```

- Para o Dockerfile(lembrar de reconstruir imagem)

```
ARG RABBITMQ_SERVER=rabbitmq-host
ARG EUREKA_SERVER=localhost
```

- Para executar

```
docker run --name nome-do-container 
-p 8761:8761 
--network nome-da-network 
-e RABBITMQ_SERVER=nome-do-container
-e EUREKA_SERVER=nome-do-container
nome-da-imagem
```

_replicar seções 5 em diante_

