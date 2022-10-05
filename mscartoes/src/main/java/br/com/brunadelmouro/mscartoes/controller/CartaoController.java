package br.com.brunadelmouro.mscartoes.controller;

import br.com.brunadelmouro.mscartoes.dto.request.CartaoRequest;
import br.com.brunadelmouro.mscartoes.model.Cartao;
import br.com.brunadelmouro.mscartoes.service.CartaoService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cartoes")
@AllArgsConstructor
public class CartaoController {

    ModelMapper modelMapper;
    CartaoService cartaoService;

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
}
