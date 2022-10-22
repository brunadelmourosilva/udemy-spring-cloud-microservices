package br.com.brunadelmouro.msavaliadorcredito.exception;

import lombok.Getter;

public class ErroComunicacaoMicroservicesException extends Throwable {

    @Getter
    private Integer status;

    public ErroComunicacaoMicroservicesException(String message, int status) {
        super(message);
        this.status = status;
    }
}
