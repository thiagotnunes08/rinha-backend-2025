package br.com.rinha_3.rinha_backend.client;

import br.com.rinha_3.rinha_backend.payment.entity.Payment;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record PaymentRequestProcessor(UUID correlationId, BigDecimal amount, Instant requestedAt) {

    public PaymentRequestProcessor(Payment payment) {
        this(payment.getCorrelationId(),payment.getAmount(),payment.getRequestedAt());
    }
}
