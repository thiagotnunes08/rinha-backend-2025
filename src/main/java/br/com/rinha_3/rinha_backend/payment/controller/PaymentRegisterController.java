package br.com.rinha_3.rinha_backend.payment.controller;

import br.com.rinha_3.rinha_backend.payment.repository.PaymentRepository;
import br.com.rinha_3.rinha_backend.payment.request.PaymentRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentRegisterController {

    private final PaymentRepository paymentRepository;

    public PaymentRegisterController(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @PostMapping("payments")
    @Transactional
    public void process(@RequestBody @Valid PaymentRequest request) {
        var newPayment = request.toModel();
        paymentRepository.save(newPayment);
    }
}
