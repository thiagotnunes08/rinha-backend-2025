package br.com.rinha_3.rinha_backend.payment.controller;

import br.com.rinha_3.rinha_backend.payment.dto.DefaultProcessorDTO;
import br.com.rinha_3.rinha_backend.payment.dto.FallbackProcessorDTO;
import br.com.rinha_3.rinha_backend.payment.entity.Processor;
import br.com.rinha_3.rinha_backend.payment.entity.Status;
import br.com.rinha_3.rinha_backend.payment.repository.PaymentRepository;
import br.com.rinha_3.rinha_backend.payment.response.PaymentSummaryResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@RestController
public class PaymentSummaryController {

    private final PaymentRepository paymentRepository;

    public PaymentSummaryController(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @GetMapping("payments-summary")
    public PaymentSummaryResponse summary(@RequestParam(required = false) Instant from,
                                          @RequestParam(required = false) Instant to) {

        if (from != null && to == null) {
            to = Instant.now();
        }
        else if (from == null && to != null) {
            from = Instant.now().minus(1, ChronoUnit.DAYS);
        }

        var payments = paymentRepository.getPaymentSummaryBetween(Status.PAID, from, to);

        return switch (payments.size()) {
            case 0 -> new PaymentSummaryResponse(
                    new DefaultProcessorDTO(0L, BigDecimal.ZERO),
                    new FallbackProcessorDTO(0L, BigDecimal.ZERO)
            );

            case 1 -> {
                var p = payments.getFirst();
                yield switch (p.processorType()) {
                    case Processor.DEFAULT -> new PaymentSummaryResponse(
                            new DefaultProcessorDTO(p.totalRequests(), p.totalAmount()),
                            new FallbackProcessorDTO(0L, BigDecimal.ZERO)
                    );
                    case Processor.FALLBACK -> new PaymentSummaryResponse(
                            new DefaultProcessorDTO(0L, BigDecimal.ZERO),
                            new FallbackProcessorDTO(p.totalRequests(), p.totalAmount())
                    );
                };
            }

            default -> {
                var defaultt = payments.get(0);
                var fallback = payments.get(1);
                yield new PaymentSummaryResponse(
                        new DefaultProcessorDTO(defaultt.totalRequests(), defaultt.totalAmount()),
                        new FallbackProcessorDTO(fallback.totalRequests(), fallback.totalAmount())
                );
            }
        };
    }
}
