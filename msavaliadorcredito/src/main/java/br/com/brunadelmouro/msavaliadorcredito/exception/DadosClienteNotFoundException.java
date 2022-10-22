package br.com.brunadelmouro.msavaliadorcredito.exception;

public class DadosClienteNotFoundException extends Exception {

    public DadosClienteNotFoundException(String cpf) {
        super("Dados do cliente não encontrados para o CPF " + cpf);
    }
}
