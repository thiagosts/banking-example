package br.com.banking.project.test.controller;

import br.com.banking.project.test.dto.ClienteDTO;
import br.com.banking.project.test.dto.TransferenciaDTO;
import br.com.banking.project.test.exception.BacenNotificationException;
import br.com.banking.project.test.model.Cliente;
import br.com.banking.project.test.service.ClienteService;
import br.com.banking.project.test.service.TransacaoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/transacoes")
@Slf4j
public class TransacaoController {

    @Autowired
    private TransacaoService transacaoService;

    @Autowired
    private ClienteService clienteService;

    @GetMapping("/cliente")
    public ResponseEntity<ClienteDTO> buscarClientePorNome(@RequestParam String nome) {

        Optional<Cliente> cliente = clienteService.buscarClientePorNome(nome.toUpperCase());

        if (cliente.isPresent()) {
            ClienteDTO clienteDTO = ClienteDTO.fromCliente(cliente.get());
            return ResponseEntity.ok(clienteDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/transferir")
    public ResponseEntity<String> transferir(@RequestBody TransferenciaDTO transferenciaDTO) {
        try {
            log.info("Iniciando transferencia");
            transacaoService.realizarTransferencia(transferenciaDTO);
            return ResponseEntity.ok("Transferência realizada com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao realizar a transferencia");
        } catch (BacenNotificationException e) {
            throw new RuntimeException(e);
        }
    }
}
