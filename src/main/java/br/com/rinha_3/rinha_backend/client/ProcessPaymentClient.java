package br.com.rinha_3.rinha_backend.client;

import br.com.rinha_3.rinha_backend.payment.entity.Payment;
import br.com.rinha_3.rinha_backend.payment.entity.Processor;
import br.com.rinha_3.rinha_backend.payment.entity.Status;
import br.com.rinha_3.rinha_backend.payment.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

@Component
public class ProcessPaymentClient {

    private PaymentRepository paymentRepository;

    public ProcessPaymentClient(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Transactional
    public void send(Payment payment) {

         try {
             System.out.println("usando o defalt");
             payment.setProcessor(Processor.DEFAULT);
             payment.updateStatus(Status.PAID);
             paymentRepository.save(payment);
         }

         catch (Exception e) {
             System.out.println("usando o fallback");
             payment.setProcessor(Processor.FALLBACK);
             payment.updateStatus(Status.PAID);
             paymentRepository.save(payment);
         }
    }
}