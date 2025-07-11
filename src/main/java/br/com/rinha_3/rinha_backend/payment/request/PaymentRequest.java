package br.com.rinha_3.rinha_backend.payment.request;
import br.com.rinha_3.rinha_backend.payment.entity.Payment;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record PaymentRequest(@NotNull UUID correlationId, @NotNull BigDecimal amount) {

    public Payment toModel() {
        return new Payment(correlationId,amount);
    }
}
