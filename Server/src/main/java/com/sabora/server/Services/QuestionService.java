package com.sabora.server.Services;

import com.sabora.server.DTOs.QuestionDTO;
import com.sabora.server.Models.Question;

public interface QuestionService {
     Question questionDTOToQuestion(QuestionDTO questionDTO);
}