package br.com.banking.project.test.service;

import br.com.banking.project.test.dto.TransferenciaDTO;
import br.com.banking.project.test.exception.BacenNotificationException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class BacenService {

    private final RestTemplate restTemplate;
    private final String bacenApiUrl;
    private final int maxRetries;

    public BacenService(RestTemplate restTemplate, @Value("${bacen.api.url}") String bacenApiUrl, @Value("${bacen.max.retries}") int maxRetries) {
        this.restTemplate = restTemplate;
        this.bacenApiUrl = bacenApiUrl;
        this.maxRetries = maxRetries;
    }

    @CircuitBreaker(name = "bacenService", fallbackMethod = "fallbackNotificarTransferencia")
    public void notificarTransferencia(TransferenciaDTO transferencia) throws BacenNotificationException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<TransferenciaDTO> requestEntity = new HttpEntity<>(transferencia, headers);

        int retries = 0;
        ResponseEntity<Void> response;

        do {
            try {
                response = restTemplate.exchange(
                        bacenApiUrl,
                        HttpMethod.POST,
                        requestEntity,
                        Void.class
                );

                if (response.getStatusCode() == HttpStatus.CREATED) {
                    // Notificação bem-sucedida
                    return;
                }
            } catch (HttpStatusCodeException e) {
                if (e.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS && retries < maxRetries) {
                    // Rate limit atingido, aguarda e tenta novamente
                    retries++;
                    try {
                        Thread.sleep(2000); // Aguarda 2 segundos antes de tentar novamente
                    } catch (InterruptedException ignored) {
                    }
                } else {
                    // Outros erros
                    throw new BacenNotificationException("Erro ao notificar o BACEN sobre a transferência.", e);
                }
            }
        } while (retries < maxRetries);

        throw new BacenNotificationException("Número máximo de tentativas excedido ao notificar o BACEN sobre a transferência.");
    }

    public void fallbackNotificarTransferencia(TransferenciaDTO transferencia, Throwable throwable) {
        //um fallback adequado aqui, por exemplo, registrar o erro em log ou executar alguma ação alternativa.
        log.error("Fallback: Não foi possível notificar o BACEN sobre a transferência. Motivo: {}", throwable.getMessage());
    }
}
