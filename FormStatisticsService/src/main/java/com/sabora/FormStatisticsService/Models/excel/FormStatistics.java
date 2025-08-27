package com.sabora.FormStatisticsService.Models.excel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.util.Pair;

import java.time.LocalDate;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FormStatistics {
    private List<FormResponse> responsesPerForm;
    private List<FoodResponse> responsesPerFood;
    private List<ScenarioResponse> responsesPerScenario;
    private List<TrendResponse> responseTrend;
}
