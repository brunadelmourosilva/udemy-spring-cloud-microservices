package br.com.brunadelmouro.msavaliadorcredito.service;

import br.com.brunadelmouro.msavaliadorcredito.exception.DadosClienteNotFoundException;
import br.com.brunadelmouro.msavaliadorcredito.exception.ErroComunicacaoMicroservicesException;
import br.com.brunadelmouro.msavaliadorcredito.infra.clients.CartaoControllerClient;
import br.com.brunadelmouro.msavaliadorcredito.infra.clients.ClienteControllerClient;
import br.com.brunadelmouro.msavaliadorcredito.model.SituacaoCliente;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AvaliadorCreditoService {

    private final ClienteControllerClient clientClient;
    private final CartaoControllerClient cardClient;

    public SituacaoCliente getCustomerSituation(String cpf) throws DadosClienteNotFoundException, ErroComunicacaoMicroservicesException {
        //obter dados do cliente - microservice MSCLIENTES
        //obter cartões do cliente - microservice MSCARTOES

        try {
            final var customerResponse = clientClient.getCustomerByCpf(cpf);
            final var cardResponse = cardClient.getCardsByCustomerCpf(cpf);

            return SituacaoCliente
                    .builder()
                    .cliente(customerResponse.getBody())
                    .cartoes(cardResponse.getBody())
                    .build();
        } catch (FeignException.FeignClientException e) {
            int status = e.status();
            if (status == HttpStatus.NOT_FOUND.value())
                throw new DadosClienteNotFoundException(cpf);

            throw new ErroComunicacaoMicroservicesException(e.getMessage(), status);
        }
    }
}
