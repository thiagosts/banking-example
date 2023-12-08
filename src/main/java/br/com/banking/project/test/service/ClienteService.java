package br.com.banking.project.test.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.com.banking.project.test.model.Cliente;
import br.com.banking.project.test.repository.ClienteRepository;

import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public Optional<Cliente> buscarClientePorNome(String nome) {
        return clienteRepository.findByNome(nome);
    }
}
