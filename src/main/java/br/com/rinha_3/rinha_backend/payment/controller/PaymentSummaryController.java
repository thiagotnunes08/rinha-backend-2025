package br.com.rinha_3.rinha_backend.payment.controller;

import br.com.rinha_3.rinha_backend.payment.dto.DefaultProcessorDTO;
import br.com.rinha_3.rinha_backend.payment.dto.FallbackProcessorDTO;
import br.com.rinha_3.rinha_backend.payment.entity.Status;
import br.com.rinha_3.rinha_backend.payment.repository.PaymentRepository;
import br.com.rinha_3.rinha_backend.payment.response.PaymentSummaryResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.Instant;

@RestController
public class PaymentSummaryController {
//GET /payments-summary?from=2020-07-10T12:34:56.000Z&to=2020-07-10T12:35:56.000Z

    private final PaymentRepository paymentRepository;

    public PaymentSummaryController(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @GetMapping("payments-summary")
    public PaymentSummaryResponse summary(@RequestParam(required = false) Instant from,
                                          @RequestParam(required = false) Instant to) {

        if (to == null) {

            to = Instant.now();

        }

        if (from == null) {

            from = Instant.now();

        }

        var payments = paymentRepository.getPaymentSummaryBetween(Status.PAID, from, to);

        if (payments.isEmpty()) {

            return new PaymentSummaryResponse(
                    new DefaultProcessorDTO(0L, BigDecimal.ZERO),
                    new FallbackProcessorDTO(0L, BigDecimal.ZERO));
        }

        if (payments.size() == 1) {

            var defaultt = payments.get(0);

            return new PaymentSummaryResponse(
                    new DefaultProcessorDTO(defaultt.totalRequests(), defaultt.totalAmount()),
                    new FallbackProcessorDTO(0L, BigDecimal.ZERO));
        }

        var defaultt = payments.get(0);
        var fallback = payments.get(1);

        return new PaymentSummaryResponse(new DefaultProcessorDTO(defaultt.totalRequests(), defaultt.totalAmount()),
                new FallbackProcessorDTO(fallback.totalRequests(), fallback.totalAmount()));
    }
}
