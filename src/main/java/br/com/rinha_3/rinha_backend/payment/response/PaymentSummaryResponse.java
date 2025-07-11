package br.com.rinha_3.rinha_backend.payment.response;


import br.com.rinha_3.rinha_backend.payment.dto.DefaultProcessorDTO;
import br.com.rinha_3.rinha_backend.payment.dto.FallbackProcessorDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

public record PaymentSummaryResponse(@JsonProperty("default") DefaultProcessorDTO defaullt, FallbackProcessorDTO fallback) {

}
