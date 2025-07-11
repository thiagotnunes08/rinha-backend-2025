package br.com.rinha_3.rinha_backend.client;

import br.com.rinha_3.rinha_backend.payment.entity.Payment;
import br.com.rinha_3.rinha_backend.payment.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
public class ProcessPaymentClient {

    private PaymentRepository paymentRepository;

    public ProcessPaymentClient(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Transactional
    public void send(Payment payment) {

        var restClient = RestClient.create();

        var response = restClient
                .post()
                .uri("http://localhost:8002/payments")
                .body(new PaymentRequestProcessor(payment))
                .contentType(APPLICATION_JSON)
                .retrieve()
                .body(Object.class);

        System.out.println(response);
    }
}