# udemy-microservices

## Microservices

### :arrow_right: eurekaserver: 
Microsserviço que realizará o gerencimento das instâncias criadas do microsserviço msclientes.

### :arrow_right: msclientes: 
Microsserviço que realiza a lógica da aplicação para o cadastro dos clientes, com as devidas regras de negócio e endpoints. Além disso, o microsserviço do tipo _client_ poderá 
fornecer várias instâncias do mesmo e será gerenciado pelo eureka server.

### :arrow_right: mscartoes: 
Microsserviço que realiza a lógica da aplicação para os tipos de pagamento, com as devidas regras de negócio e endpoints. Além disso, o microsserviço do tipo _client_ poderá 
fornecer várias instâncias do mesmo e será gerenciado pelo eureka server.

### :arrow_right: mscloudgateway: 
Microsserviço que será responsável por receber todas as requisisões provinientes da arquitetura citada anteriormente(eurekaserver e msclientes)
realizando dessa forma, o roteamento/encaminhamento para a instância do microsserviço adequado, ou seja, que possui menos requisições.

----

### Testando Load Balancer

1. Subir, por exemplo, 3 instâncias no msclientes, por meio do comando ```./mvnw spring-boot:run```
2. Subir mscloudgateway
3. Realizar uma requisição e observar por meio dos logs das 3 instâncias, em qual delas a requisição foi direcionada
4. Realizar a ação novamente para observar tais comportamentos
