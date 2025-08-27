package com.sabora.FormStatisticsService.Models.excel;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScenarioResponse {
    private String scenarioName;
    private Integer responseCount;
}
