# udemy-microservices

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

---

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
---

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

Seção 5 - Segurança da API com keycloak

Site: https://www.keycloak.org/getting-started/getting-started-docker
