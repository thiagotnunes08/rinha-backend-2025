package br.com.rinha_3.rinha_backend.payment.dto;

import java.math.BigDecimal;

public record DefaultProcessorDTO(Long totalRequests, BigDecimal totalAmount) {
}
