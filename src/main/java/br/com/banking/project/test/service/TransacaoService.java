package br.com.banking.project.test.service;

import br.com.banking.project.test.exception.BacenNotificationException;
import br.com.banking.project.test.exception.SaldoExcedidoException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.com.banking.project.test.dto.TransferenciaDTO;
import br.com.banking.project.test.exception.ContaInativaException;
import br.com.banking.project.test.exception.LimiteExcedidoException;
import br.com.banking.project.test.model.Conta;
import br.com.banking.project.test.repository.ContaRepository;

import java.math.BigDecimal;

@Service
@Slf4j
public class TransacaoService {

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private BacenService bacenService;

        @CircuitBreaker(name = "transacaoService", fallbackMethod = "fallbackRealizarTransferencia")
        public void realizarTransferencia(TransferenciaDTO transferenciaDTO)
                throws ContaInativaException, LimiteExcedidoException, BacenNotificationException {

            log.info("Buscando conta de origem e destino");

        Conta origem = contaRepository.findContaByNumeroConta(transferenciaDTO.contaOrigem())
                .orElseThrow(() -> new ContaInativaException("Conta de origem não encontrada."));
        Conta destino = contaRepository.findContaByNumeroConta(transferenciaDTO.contaDestino())
                .orElseThrow(() -> new ContaInativaException("Conta de destino não encontrada."));

        log.info("Verificação de contas ativas");

        if (!origem.isAtiva() || !destino.isAtiva()) {
            throw new ContaInativaException("Uma das contas está inativa.");
        }

        log.info("Verificando saldo na conta de origem");

        if (origem.getSaldo().compareTo(transferenciaDTO.valor()) < 0) {
            throw new SaldoExcedidoException("Saldo insuficiente na conta de origem.");
        }

        log.info("Verificando Limite Diario");
        BigDecimal limiteDiario = origem.getLimiteDiario();
        if (transferenciaDTO.valor().compareTo(limiteDiario) > 0) {
            throw new LimiteExcedidoException("Transferência excede o limite diário.");
        }

        log.info("Debitando valor da conta de origem");
        origem.debitar(transferenciaDTO.valor());

        log.info("Creditando valor na  conta de destino");
        destino.creditar(transferenciaDTO.valor());

        contaRepository.save(origem);
        contaRepository.save(destino);

        notificarBacenSobreTransacao(transferenciaDTO);
    }

    public void notificarBacenSobreTransacao(TransferenciaDTO transferenciaDTO) throws BacenNotificationException {
        log.info("Notificando o BACEN");
        bacenService.notificarTransferencia(transferenciaDTO);
    }

    public void fallbackRealizarTransferencia(TransferenciaDTO transferenciaDTO, Throwable throwable) {
        // fallback por exemplo, registrar o erro em log ou executar alguma ação alternativa.
        log.error("Fallback: Não foi possível realizar a transferência. Motivo: {}", throwable.getMessage());
    }
}
