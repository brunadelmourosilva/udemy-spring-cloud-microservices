package br.com.brunadelmouro.mscartoes.service;

import br.com.brunadelmouro.mscartoes.model.ClienteCartao;
import br.com.brunadelmouro.mscartoes.repository.ClienteCartaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteCartaoService {

    private final ClienteCartaoRepository clienteCartaoRepository;

    public List<ClienteCartao> getCardsByCpf(String cpf) {
        return clienteCartaoRepository.findByCpf(cpf);
    }
}
