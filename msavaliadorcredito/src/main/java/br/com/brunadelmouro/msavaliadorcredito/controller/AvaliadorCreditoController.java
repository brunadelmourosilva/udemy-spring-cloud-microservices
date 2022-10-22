package br.com.brunadelmouro.msavaliadorcredito.controller;

import br.com.brunadelmouro.msavaliadorcredito.exception.DadosClienteNotFoundException;
import br.com.brunadelmouro.msavaliadorcredito.exception.ErroComunicacaoMicroservicesException;
import br.com.brunadelmouro.msavaliadorcredito.service.AvaliadorCreditoService;
import br.com.brunadelmouro.msavaliadorcredito.model.SituacaoCliente;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/avaliacoes-credito")
@RequiredArgsConstructor
public class AvaliadorCreditoController {

    private final AvaliadorCreditoService avaliadorCreditoService;

    @GetMapping
    public String status() {
        return "ok";
    }

    @GetMapping(value = "/situacao-cliente")
    public ResponseEntity getCustomerSituation(@RequestParam("cpf") String cpf) {

        try {
            final var response = avaliadorCreditoService.getCustomerSituation(cpf);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch(DadosClienteNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (ErroComunicacaoMicroservicesException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.resolve(e.getStatus()));
        }
    }
}
