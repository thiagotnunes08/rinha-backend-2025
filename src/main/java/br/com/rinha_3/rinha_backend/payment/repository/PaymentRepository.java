package br.com.rinha_3.rinha_backend.payment.repository;

import br.com.rinha_3.rinha_backend.payment.dto.PaymentSummaryDTO;
import br.com.rinha_3.rinha_backend.payment.entity.Payment;
import br.com.rinha_3.rinha_backend.payment.entity.Processor;
import br.com.rinha_3.rinha_backend.payment.entity.Status;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query(value = "select * from Payment p " +
            "where p.status = ?1 limit 50 " +
            "for update skip locked",nativeQuery = true)
    List<Payment> findAllByStatus(Status status);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select new br.com.rinha_3.rinha_backend.payment.dto.PaymentSummaryDTO" +
            "(count(p),sum(p.amount),p.processor) " +
            "from Payment p " +
            "where p.status = ?1 " +
            "and p.requestedAt between ?2 and ?3" +
            " group by p.processor " +
            "order by p.processor asc")
    List<PaymentSummaryDTO> getPaymentSummaryBetween(Status status, Instant from, Instant to);


    @Modifying
    @Transactional
    @Query(value = "UPDATE payment SET status = ?1, processor = ?2 WHERE id = ?3", nativeQuery = true)
    void update(Status status, Processor processor, Long id);

}
