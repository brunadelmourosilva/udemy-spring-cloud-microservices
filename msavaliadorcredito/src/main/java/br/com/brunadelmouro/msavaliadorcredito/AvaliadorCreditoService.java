package br.com.brunadelmouro.msavaliadorcredito;

import br.com.brunadelmouro.msavaliadorcredito.model.SituacaoCliente;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AvaliadorCreditoService {

    public SituacaoCliente getCustomerSituation(String cpf) {
        //obter dados do cliente - microservice MSCLIENTES
        //obter cartões do cliente - microservice MSCARTOES
    }
}
