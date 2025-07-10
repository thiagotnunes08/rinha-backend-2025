package br.com.rinha_3.rinha_backend.client;

import br.com.rinha_3.rinha_backend.payment.PaymentRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class ProcessPaymentClient {

    private final RestClient client = RestClient.create();

    public void send(PaymentRequest request) {
         client.post()
                .uri("https://api.externa/pagamentos")
                .body(request);
    }
}