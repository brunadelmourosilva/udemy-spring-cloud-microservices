package br.com.brunadelmouro.msavaliadorcredito.service;

import br.com.brunadelmouro.msavaliadorcredito.infra.clients.ClienteControllerClient;
import br.com.brunadelmouro.msavaliadorcredito.model.SituacaoCliente;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AvaliadorCreditoService {

    private final ClienteControllerClient client;

    public SituacaoCliente getCustomerSituation(String cpf) {
        //obter dados do cliente - microservice MSCLIENTES
        //obter cartões do cliente - microservice MSCARTOES

        final var customerResponse = client.getCustomerByCpf(cpf);

        return SituacaoCliente
                .builder()
                .cliente(customerResponse.getBody())
                .build();
    }
}
