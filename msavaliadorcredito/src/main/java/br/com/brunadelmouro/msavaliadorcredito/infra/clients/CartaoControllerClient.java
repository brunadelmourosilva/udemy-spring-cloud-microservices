package br.com.brunadelmouro.msavaliadorcredito.infra.clients;

import br.com.brunadelmouro.msavaliadorcredito.model.Cartao;
import br.com.brunadelmouro.msavaliadorcredito.model.CartaoCliente;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

// url faz referência ao nome do microservice presente no load balancer do gateway
@FeignClient(value = "mscartoes", path = "/cartoes")
public interface CartaoControllerClient {

    // adiciona-se a mesma assinatura do método à ser consumido

    @GetMapping(params = "cpf")
    public ResponseEntity<List<CartaoCliente>> getCardsByCustomerCpf(@RequestParam("cpf") String cpf);

    @GetMapping(params = "renda")
    ResponseEntity<List<Cartao>> getCardsByIncomeLessThanEqual(@RequestParam("renda") Long renda);
}
