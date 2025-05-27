package com.sabora.server.DTOs;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class SimpleDTO {
    private String message;
}
