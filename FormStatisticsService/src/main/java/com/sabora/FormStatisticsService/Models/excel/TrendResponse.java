package com.sabora.FormStatisticsService.Models.excel;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrendResponse {
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer responseCount;
}
