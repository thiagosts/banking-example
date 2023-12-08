package br.com.banking.project.test.dto;

import br.com.banking.project.test.model.Conta;

import java.math.BigDecimal;

public record ContaDTO(int numeroConta, BigDecimal saldo, boolean ativa, BigDecimal limiteDiario, int agencia) {
    public static ContaDTO fromConta(Conta conta) {
        return new ContaDTO(
                conta.getNumeroConta(),
                conta.getSaldo(),
                conta.isAtiva(),
                conta.getLimiteDiario(),
                conta.getAgencia()
        );
    }
}
