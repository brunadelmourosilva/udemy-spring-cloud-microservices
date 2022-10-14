package br.com.brunadelmouro.mscartoes.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ClienteCartaoResponse {

    private String nome;
    private String bandeira;
    private BigDecimal limite;
}
