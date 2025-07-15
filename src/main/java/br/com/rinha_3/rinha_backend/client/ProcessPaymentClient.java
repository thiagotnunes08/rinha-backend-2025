package br.com.rinha_3.rinha_backend.client;

import br.com.rinha_3.rinha_backend.payment.entity.Payment;
import br.com.rinha_3.rinha_backend.payment.entity.Processor;
import br.com.rinha_3.rinha_backend.payment.entity.Status;
import br.com.rinha_3.rinha_backend.payment.repository.PaymentRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
public class ProcessPaymentClient {

    private PaymentRepository paymentRepository;
    private final ObjectMapper objectMapper;

    public ProcessPaymentClient(PaymentRepository paymentRepository, ObjectMapper objectMapper) {
        this.paymentRepository = paymentRepository;
        this.objectMapper = objectMapper;
    }

    public void send(Payment payment) {

        var restClient = RestClient.builder().requestFactory(getClientHttpRequestFactory()).build();

        try {

            restClient
                    .post()
                    .uri("http://localhost:8001/payments")
                    .body(toJson(payment))
                    .contentType(APPLICATION_JSON)
                    .retrieve()
                    .toBodilessEntity();

            paymentRepository.update(Status.PAID, Processor.DEFAULT, payment.getId());


        } catch (Exception e) {

            try {
                restClient
                        .post()
                        .uri("http://localhost:8002/payments")
                        .body(toJson(payment))
                        .contentType(APPLICATION_JSON)
                        .retrieve().toBodilessEntity();

                paymentRepository.update(Status.PAID, Processor.FALLBACK, payment.getId());

            } catch (Exception e2) {

                System.out.println("deu ruim até no fallback!");
            }
        }
    }

    private String toJson(Payment payment) {
        try {

            return objectMapper.writeValueAsString(
                    new PaymentRequestProcessor(payment));

        } catch (JsonProcessingException e) {

            System.out.println("err to convert class in json");

        }
        return null;
    }

    private ClientHttpRequestFactory getClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout(5000);
        factory.setConnectTimeout(5000);
        return factory;
    }
}