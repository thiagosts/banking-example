package br.com.banking.project.test.dto;

import br.com.banking.project.test.model.Cliente;

import java.util.Set;
import java.util.stream.Collectors;

public record ClienteDTO(String nome, Set<ContaDTO> contas) {
    public static ClienteDTO fromCliente(Cliente cliente) {
        Set<ContaDTO> contaDTOs = cliente.getContas()
                .stream()
                .map(ContaDTO::fromConta)
                .collect(Collectors.toSet());

        return new ClienteDTO(cliente.getNome(), contaDTOs);
    }
}
