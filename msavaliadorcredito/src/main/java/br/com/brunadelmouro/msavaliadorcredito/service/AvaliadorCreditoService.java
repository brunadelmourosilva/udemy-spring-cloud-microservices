package br.com.brunadelmouro.msavaliadorcredito.service;

import br.com.brunadelmouro.msavaliadorcredito.infra.clients.CartaoControllerClient;
import br.com.brunadelmouro.msavaliadorcredito.infra.clients.ClienteControllerClient;
import br.com.brunadelmouro.msavaliadorcredito.model.SituacaoCliente;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AvaliadorCreditoService {

    private final ClienteControllerClient clientClient;
    private final CartaoControllerClient cardClient;

    public SituacaoCliente getCustomerSituation(String cpf) {
        //obter dados do cliente - microservice MSCLIENTES
        //obter cartões do cliente - microservice MSCARTOES

        final var customerResponse = clientClient.getCustomerByCpf(cpf);
        final var cardResponse = cardClient.getCardsByCustomerCpf(cpf);

        return SituacaoCliente
                .builder()
                .cliente(customerResponse.getBody())
                .cartoes(cardResponse.getBody())
                .build();
    }
}
