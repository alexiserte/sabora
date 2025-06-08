package com.sabora.server.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExperienceDTO {
    private int id;
    private Long time;
    private String client;
    private String scenario;
    private String sound;
    private List<String> food;
    private List<String> sounds;
    private Integer userId;

}
