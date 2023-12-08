package br.com.banking.project.test.service;

import br.com.banking.project.test.dto.TransferenciaDTO;
import br.com.banking.project.test.exception.BacenNotificationException;
import br.com.banking.project.test.exception.ContaInativaException;
import br.com.banking.project.test.exception.LimiteExcedidoException;
import br.com.banking.project.test.exception.SaldoExcedidoException;
import br.com.banking.project.test.model.Conta;
import br.com.banking.project.test.repository.ContaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class TransacaoServiceTest {

    @InjectMocks
    private TransacaoService transacaoService;

    @Mock
    private ContaRepository contaRepository;

    @Mock
    private BacenService bacenService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testRealizarTransferenciaContaInativa() {
        TransferenciaDTO transferenciaDTO = new TransferenciaDTO(10001,10002,BigDecimal.valueOf(50.00));
        Conta contaOrigem = new Conta(1L,null,1001, BigDecimal.valueOf(100.00),true, BigDecimal.valueOf(100.00),1);
        contaOrigem.setAtiva(false);

        when(contaRepository.findContaByNumeroConta(anyInt())).thenReturn(Optional.of(contaOrigem));

        assertThrows(ContaInativaException.class, () -> transacaoService.realizarTransferencia(transferenciaDTO));

        verify(contaRepository, never()).save(any());
    }

    @Test
    public void testRealizarTransferenciaSaldoExcedido() {
        TransferenciaDTO transferenciaDTO = new TransferenciaDTO(10001,10002,BigDecimal.valueOf(100.00));
        Conta contaOrigem = new Conta(1L,null,1001, BigDecimal.valueOf(200.00),true, BigDecimal.valueOf(50.00),1);
        contaOrigem.setAtiva(true);
        contaOrigem.setSaldo(BigDecimal.valueOf(50.00));

        when(contaRepository.findContaByNumeroConta(anyInt())).thenReturn(Optional.of(contaOrigem));

        assertThrows(SaldoExcedidoException.class, () -> transacaoService.realizarTransferencia(transferenciaDTO));

        verify(contaRepository, never()).save(any());
    }

    @Test
    public void testRealizarTransferenciaLimiteExcedido() {
        TransferenciaDTO transferenciaDTO = new TransferenciaDTO(10001,10002,BigDecimal.valueOf(100.00));
        Conta contaOrigem = new Conta(1L,null,1001, BigDecimal.valueOf(200.00),true, BigDecimal.valueOf(50.00),1);
        contaOrigem.setAtiva(true);
        contaOrigem.setLimiteDiario(BigDecimal.valueOf(50.00));

        when(contaRepository.findContaByNumeroConta(anyInt())).thenReturn(Optional.of(contaOrigem));

        assertThrows(LimiteExcedidoException.class, () -> transacaoService.realizarTransferencia(transferenciaDTO));

        verify(contaRepository, never()).save(any());
    }

    @Test
    public void testRealizarTransferenciaSucesso() throws BacenNotificationException {
        TransferenciaDTO transferenciaDTO = new TransferenciaDTO(10001,10002, BigDecimal.valueOf(50.00));
        Conta contaOrigem = new Conta(1L,null,10001, BigDecimal.valueOf(200.00),true, BigDecimal.valueOf(500.00),1);
        Conta contaDestino = new Conta(2L,null,10002, BigDecimal.valueOf(200.00),true, BigDecimal.valueOf(500.00),1);
        contaOrigem.setAtiva(true);
        contaDestino.setAtiva(true);
        contaOrigem.setSaldo(BigDecimal.valueOf(100.00));
        contaOrigem.setLimiteDiario(BigDecimal.valueOf(500.00));

        when(contaRepository.findContaByNumeroConta(transferenciaDTO.contaOrigem())).thenReturn(Optional.of(contaOrigem));
        when(contaRepository.findContaByNumeroConta(transferenciaDTO.contaDestino())).thenReturn(Optional.of(contaDestino));

        transacaoService.realizarTransferencia(transferenciaDTO);

        verify(contaRepository, times(2)).save(any());
        verify(bacenService, times(1)).notificarTransferencia(transferenciaDTO);
    }

}

