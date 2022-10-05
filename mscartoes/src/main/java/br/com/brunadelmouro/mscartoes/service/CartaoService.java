package br.com.brunadelmouro.mscartoes.service;

import br.com.brunadelmouro.mscartoes.model.Cartao;
import br.com.brunadelmouro.mscartoes.repository.CartaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartaoService {

    private final CartaoRepository cartaoRepository;

    @Transactional
    public Cartao saveCard(Cartao cartao) {
        return cartaoRepository.save(cartao);
    }

    public List<Cartao> getCardsWageLessThanEqual(Long renda) {
        var wageBigDecimal = BigDecimal.valueOf(renda);

        return cartaoRepository.findByRendaLessThanEqual(wageBigDecimal);
    }
}
