package br.com.rinha_3.rinha_backend.payment;

import br.com.rinha_3.rinha_backend.client.ProcessPaymentClient;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProcessPaymentController {

    private final PaymentRepository paymentRepository;

    public ProcessPaymentController(PaymentRepository paymentRepository, ProcessPaymentClient client) {
        this.paymentRepository = paymentRepository;
    }

    @PostMapping("payments")
    public void process(@RequestBody @Valid PaymentRequest request) {
        var newPayment = request.toModel();
        paymentRepository.save(newPayment);
    }
}
