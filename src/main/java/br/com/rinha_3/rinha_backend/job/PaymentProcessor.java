package br.com.rinha_3.rinha_backend.job;

import br.com.rinha_3.rinha_backend.client.ProcessPaymentClient;
import br.com.rinha_3.rinha_backend.payment.entity.Status;
import br.com.rinha_3.rinha_backend.payment.repository.PaymentRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

@Component
public class PaymentProcessor {

    private final PaymentRepository paymentRepository;

    private final ProcessPaymentClient processPaymentClient;

    public PaymentProcessor(PaymentRepository paymentRepository, ProcessPaymentClient processPaymentClient) {
        this.paymentRepository = paymentRepository;
        this.processPaymentClient = processPaymentClient;
    }

    @Scheduled(fixedRate = 5000)
    public void process() {
        var executor = Executors.newVirtualThreadPerTaskExecutor();

        var tasks = paymentRepository
                .findAllByStatus(Status.PENDING)
                .stream()
                .map(payment ->
                        CompletableFuture
                                .runAsync(() ->
                                        processPaymentClient
                                                .send(payment), executor))
                .toList();

        CompletableFuture.allOf(tasks.toArray(new CompletableFuture[0])).join();
    }
}
