package br.com.brunadelmouro.msavaliadorcredito.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Cartao {

    private String nome;
    private String bandeira;
    private BigDecimal renda;
    private BigDecimal limite;
}
