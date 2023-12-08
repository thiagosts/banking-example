package br.com.banking.project.test.dto;


import java.math.BigDecimal;

public record TransferenciaDTO(int contaOrigem, int contaDestino, BigDecimal valor) {

}
