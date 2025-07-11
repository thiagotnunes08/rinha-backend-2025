package br.com.rinha_3.rinha_backend.payment.dto;

import br.com.rinha_3.rinha_backend.payment.entity.Processor;

import java.math.BigDecimal;

public record PaymentSummaryDTO(Long totalRequests, BigDecimal totalAmount, Processor processorType) {

}
