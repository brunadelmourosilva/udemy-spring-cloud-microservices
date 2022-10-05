package br.com.brunadelmouro.mscartoes.dto.request;

import br.com.brunadelmouro.mscartoes.model.enums.BandeiraCartao;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartaoRequest {

    private String nome;
    private BandeiraCartao bandeira;
    private BigDecimal renda;
    private BigDecimal limite;
}
