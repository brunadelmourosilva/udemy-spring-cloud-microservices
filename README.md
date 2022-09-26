# udemy-microservices

## Microservices

### :arrow_right: eurekaserver: 
Microsserviço que realizará o gerencimento das instâncias criadas do microsserviço msclientes.

### :arrow_right: msclientes: 
Microsserviço que realiza a lógica da aplicação, com as devidas regras de negócio e endpoints. Além disso, o microsserviço do tipo _client_ poderá 
fornecer várias instâncias do mesmo e será gerenciado pelo eureka server.

### :arrow_right: mscloudgateway: 
Microsserviço que será responsável por receber todas as requisisões provinientes da arquitetura citada anteriormente(eurekaserver e msclientes)
realizando dessa forma, o roteamento/encaminhamento para a instância do microsserviço adequado, ou seja, que possui menos requisições.
