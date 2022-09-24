package br.com.brunadelmouro.msclientes.dto.request;

import lombok.Data;

@Data
public class ClienteRequest {

    private String cpf;
    private String nome;
    private Integer idade;
}
