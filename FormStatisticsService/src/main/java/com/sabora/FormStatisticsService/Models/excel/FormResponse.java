package com.sabora.FormStatisticsService.Models.excel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FormResponse {
    private Integer formId;
    private Integer responseCount;
}