package br.com.brunadelmouro.mscartoes.repository;

import br.com.brunadelmouro.mscartoes.model.Cartao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface CartaoRepository extends JpaRepository<Cartao, Long> {
    List<Cartao> findByRendaLessThanEqual(BigDecimal wageBigDecimal);
}
