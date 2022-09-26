package br.com.brunadelmouro.msclientes.controller;

import br.com.brunadelmouro.msclientes.dto.request.ClienteRequest;
import br.com.brunadelmouro.msclientes.model.Cliente;
import br.com.brunadelmouro.msclientes.service.ClienteService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
@Slf4j
public class ClienteController {

    private final ModelMapper modelMapper;
    private final ClienteService clienteService;

    @GetMapping
    public String status(){
        log.info("TESTANDO LOAD BALANCER - obtendo status do serviço de msclientes");
        return "Ok";
    }

    @PostMapping
    public ResponseEntity<Cliente> saveCustomer(@RequestBody ClienteRequest clienteRequest){
        var cliente  = modelMapper.map(clienteRequest, Cliente.class);

        cliente = clienteService.saveCustomer(cliente);

        URI headerLocation = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .query("cpf={cpf}")
                .buildAndExpand(cliente.getCpf())
                .toUri();

        return ResponseEntity.created(headerLocation).build();
    }

    @GetMapping(params = "cpf")
    public ResponseEntity<Cliente> getCustomerByCpf(@RequestParam("cpf") String cpf) {
        var cliente = clienteService.findByCpf(cpf);

        return ResponseEntity.ok(cliente);
    }
}
