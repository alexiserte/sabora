package com.sabora.FormStatisticsService.Controllers;

import com.sabora.FormStatisticsService.Models.excel.ExcelStatisticsRequest;
import com.sabora.FormStatisticsService.Services.RabbitMQMessageConsumer;
import com.sabora.FormStatisticsService.Services.excel.ExcelGenerationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDate;

@RestController
@AllArgsConstructor
public class ExcelController {

    private ExcelGenerationService excelGenerationService;

    @PostMapping(value = "/resumen", produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public ResponseEntity<byte[]> downloadGeneralSummary(@RequestBody ExcelStatisticsRequest request) {
        try {
            byte[] excelBytes = excelGenerationService.generateGeneralSummaryExcel(request);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.set(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=resumen_general-%s.xlsx", LocalDate.now()));
            headers.setContentLength(excelBytes.length);
            return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
