package br.com.brunadelmouro.mscartoes.controller;

import br.com.brunadelmouro.mscartoes.dto.request.CartaoRequest;
import br.com.brunadelmouro.mscartoes.dto.response.ClienteCartaoResponse;
import br.com.brunadelmouro.mscartoes.model.Cartao;
import br.com.brunadelmouro.mscartoes.service.CartaoService;
import br.com.brunadelmouro.mscartoes.service.ClienteCartaoService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cartoes")
@AllArgsConstructor
public class CartaoController {

    private final ModelMapper modelMapper;
    private final CartaoService cartaoService;
    private final ClienteCartaoService clienteCartaoService;

    @GetMapping
    public String status() {
        return "Ok";
    }

    @PostMapping
    public ResponseEntity<Cartao> saveCard(@RequestBody CartaoRequest cartaoRequest) {
        var cartao = modelMapper.map(cartaoRequest, Cartao.class);

        cartao = cartaoService.saveCard(cartao);

        return new ResponseEntity<>(cartao, HttpStatus.CREATED);
    }

    @GetMapping(params = "renda")
    public ResponseEntity<List<Cartao>> getCardsByIncomeLessThanEqual(@RequestParam("renda") Long renda) {
        var cartoes = cartaoService.getCardsByIncomeLessThanEqual(renda);

        return new ResponseEntity<>(cartoes, HttpStatus.OK);
    }

    @GetMapping(params = "cpf")
    public ResponseEntity<List<ClienteCartaoResponse>> getCardsByCustomerCpf(@RequestParam("cpf") String cpf) {
        var cartoes = clienteCartaoService.getCardsByCpf(cpf);

        var cartaoesDto = cartoes
                .stream()
                .map(cartao -> modelMapper.map(cartao, ClienteCartaoResponse.class))
                .collect(Collectors.toList());

        return new ResponseEntity<>(cartaoesDto, HttpStatus.OK);
    }
}
