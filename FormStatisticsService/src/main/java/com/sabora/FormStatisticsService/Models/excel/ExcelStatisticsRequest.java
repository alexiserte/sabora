package com.sabora.FormStatisticsService.Models.excel;

import lombok.*;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExcelStatisticsRequest {
    private List<FormData> data;
    private FormStatistics stats;
}
