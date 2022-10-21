package br.com.brunadelmouro.mscloudgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

/*
 * Doc: https://spring.io/projects/spring-cloud-gateway
 * */
@SpringBootApplication
@EnableEurekaClient
@EnableDiscoveryClient
public class CloudGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudGatewayApplication.class, args);
    }

    /**
     * register Gateway on Eureka Server, to identify the registered
     * microservices(instances of msclientes) and also to do the load balancer.
     */
    @Bean
    public RouteLocator routes(RouteLocatorBuilder routeLocator) {
        return routeLocator
                .routes()
                .route(route -> route.path("/clientes/**").uri("lb://msclientes"))
                .route(route -> route.path("/cartoes/**").uri("lb://mscartoes"))
                .route(route -> route.path("/avaliacoes-credito/**").uri("lb://msavaliadorcredito"))
                .build();
    }
}
