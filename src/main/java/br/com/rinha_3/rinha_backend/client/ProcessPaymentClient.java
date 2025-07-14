package br.com.rinha_3.rinha_backend.client;

import br.com.rinha_3.rinha_backend.payment.entity.Payment;
import br.com.rinha_3.rinha_backend.payment.entity.Processor;
import br.com.rinha_3.rinha_backend.payment.entity.Status;
import br.com.rinha_3.rinha_backend.payment.repository.PaymentRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
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

    @Transactional
    public void send(Payment payment) {

        var restClient = RestClient.create();

        try {

            var responseDefault = restClient
                    .post()
                    .uri("http://localhost:8001/payments")
                    .body(toJson(payment))
                    .contentType(APPLICATION_JSON)
                    .retrieve()
                    .toBodilessEntity();

            if (responseDefault.getStatusCode().is2xxSuccessful()) {
                payment.updateStatus(Status.PAID);
                payment.setProcessor(Processor.DEFAULT);
            }

        } catch (Exception e) {

            try {
                var responseFallback = restClient
                        .post()
                        .uri("http://localhost:8002/payments")
                        .body(toJson(payment))
                        .contentType(APPLICATION_JSON)
                        .retrieve().toBodilessEntity();

                if (responseFallback.getStatusCode().is2xxSuccessful()) {

                    payment.updateStatus(Status.PAID);
                    payment.setProcessor(Processor.FALLBACK);
                }

            } catch (Exception e2) {

                System.out.println("deu ruim até no fallback! msg:"+e.getMessage()+"pagamento:"+payment.getCorrelationId());
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
}