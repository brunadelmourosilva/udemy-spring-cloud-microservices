package br.com.brunadelmouro.msavaliadorcredito.model;

import lombok.Data;

@Data
public class SituacaoCliente {

    private DadosCliente cliente;
    private List<CartaoCliente> cartoes;
}
