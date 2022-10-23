package br.com.brunadelmouro.msavaliadorcredito.service;

import br.com.brunadelmouro.msavaliadorcredito.exception.DadosClienteNotFoundException;
import br.com.brunadelmouro.msavaliadorcredito.exception.ErroComunicacaoMicroservicesException;
import br.com.brunadelmouro.msavaliadorcredito.infra.clients.CartaoControllerClient;
import br.com.brunadelmouro.msavaliadorcredito.infra.clients.ClienteControllerClient;
import br.com.brunadelmouro.msavaliadorcredito.model.*;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

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

    public RetornaAvalicaoCliente makeTestCard(String cpf, Long renda) throws DadosClienteNotFoundException, ErroComunicacaoMicroservicesException{
        try {
            final var customerResponse = clientClient.getCustomerByCpf(cpf);
            final var cardResponse = cardClient.getCardsByIncomeLessThanEqual(renda);

            var listaCartoesAprovados = cardResponse.getBody()
                    .stream()
                    .map(cartao -> {

                        var dadosCliente = customerResponse.getBody();

                        var limiteBasico = cartao.getLimite();
                        var idadeBD = BigDecimal.valueOf(dadosCliente.getIdade());

                        var limiteAprovado = limiteBasico.multiply(idadeBD.divide(BigDecimal.TEN));

                        var cartaoAprovado = new CartaoAprovado();
                        cartaoAprovado.setCartao(cartao.getNome());
                        cartaoAprovado.setBandeira(cartao.getBandeira());
                        cartaoAprovado.setLimiteAprovado(limiteAprovado);

                        return cartaoAprovado;
                    }).collect(Collectors.toList());

            return new RetornaAvalicaoCliente(listaCartoesAprovados);

        } catch (FeignException.FeignClientException e) {
            int status = e.status();
            if (status == HttpStatus.NOT_FOUND.value())
                throw new DadosClienteNotFoundException(cpf);

            throw new ErroComunicacaoMicroservicesException(e.getMessage(), status);
        }
    }
}
