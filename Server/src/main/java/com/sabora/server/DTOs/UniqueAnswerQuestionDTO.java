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
public class UniqueAnswerQuestionDTO extends QuestionDTO {
    private List<String> options;
    public UniqueAnswerQuestionDTO(int id, String question, List<String> options) {
        super(id, question);
        this.options = options;
    }
}