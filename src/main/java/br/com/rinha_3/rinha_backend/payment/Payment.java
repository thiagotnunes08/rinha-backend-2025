package br.com.rinha_3.rinha_backend.payment;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private UUID correlationId;
    private BigDecimal amount;
    private Status status;

    public Payment(UUID correlationId, BigDecimal amount) {
        this.correlationId = correlationId;
        this.amount = amount;
        this.status = Status.PENDING;
    }

    /**
     * @deprecated hibernate only
     */
    @Deprecated
    public Payment() {
    }
}
