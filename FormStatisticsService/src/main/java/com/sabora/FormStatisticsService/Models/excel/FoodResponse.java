package com.sabora.FormStatisticsService.Models.excel;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FoodResponse {
    private String foodName;
    private Integer responseCount;
}
