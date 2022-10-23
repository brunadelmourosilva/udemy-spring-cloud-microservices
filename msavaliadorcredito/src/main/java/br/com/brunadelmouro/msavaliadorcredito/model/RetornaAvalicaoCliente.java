package br.com.brunadelmouro.msavaliadorcredito.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RetornaAvalicaoCliente {

    private List<CartaoAprovado> cartoes;
}
