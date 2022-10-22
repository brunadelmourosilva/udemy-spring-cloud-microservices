package br.com.brunadelmouro.msavaliadorcredito.infra.clients;

import br.com.brunadelmouro.msavaliadorcredito.model.DadosCliente;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

// url faz referência ao nome do microservice presente no load balancer do gateway
@FeignClient(value = "msclientes", path = "/clientes")
public interface ClienteControllerClient {

    // adiciona-se a mesma assinatura do método à ser consumido

    @GetMapping(params = "cpf")
    ResponseEntity<DadosCliente> getCustomerByCpf(@RequestParam("cpf") String cpf);
}
