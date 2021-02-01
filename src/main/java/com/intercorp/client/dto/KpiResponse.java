package com.intercorp.client.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.intercorp.client.utils.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class KpiResponse {
    Double averageAge;
    Double standardDeviation;

    public static KpiResponse from(Map<String, Double> calculatedKpi) {
        return KpiResponse.builder()
                .averageAge(calculatedKpi.get(Constants.KPI_AVERAGE_KEY))
                .standardDeviation(calculatedKpi.get(Constants.KPI_DEVIATION_KEY))
                .build();
    }

}
