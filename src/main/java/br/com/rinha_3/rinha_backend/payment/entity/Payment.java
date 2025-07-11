package br.com.rinha_3.rinha_backend.payment.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.DynamicUpdate;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@DynamicUpdate
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private UUID correlationId;
    private BigDecimal amount;
    private Status status;
    private Processor processor;
    private Instant requestedAt;

    public Payment(UUID correlationId, BigDecimal amount) {
        this.correlationId = correlationId;
        this.amount = amount;
        this.status = Status.PENDING;
        this.requestedAt = Instant.now();
    }

    /**
     * @deprecated hibernate only
     */
    @Deprecated
    public Payment() {
    }

    public void setProcessor(Processor processor) {
        this.processor = processor;
    }

    public void updateStatus(Status status) {
        this.status = status;
    }
}
