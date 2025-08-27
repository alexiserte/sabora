package com.sabora.FormStatisticsService.Models.excel;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FormData {
    private int id;
    private String name;
    private String username;
    private LocalDate creationDate;
    private int numberOfAnswers;
    private String food;
    private String scenario;
    private String sound;
}
