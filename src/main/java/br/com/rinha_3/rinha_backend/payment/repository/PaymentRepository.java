package br.com.rinha_3.rinha_backend.payment.repository;

import br.com.rinha_3.rinha_backend.payment.entity.Status;
import br.com.rinha_3.rinha_backend.payment.dto.PaymentSummaryDTO;
import br.com.rinha_3.rinha_backend.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query(value = "select * from Payment p " +
            "where p.status = ?1 " +
            "for update skip locked",nativeQuery = true)
    List<Payment> findAllByStatus(Status status);


    @Query("select new br.com.rinha_3.rinha_backend.payment.dto.PaymentSummaryDTO" +
            "(count(p),sum(p.amount),p.processor) " +
            "from Payment p " +
            "where p.status = :status " +
            "and (:from is null or p.requestedAt >= :from) " +
            "and (:to is null or p.requestedAt" +
            " <= :to)" +
            " group by p.processor " +
            "order by p.processor asc")
    List<PaymentSummaryDTO> getPaymentSummaryBetween(Status status, Instant from, Instant to);
}
