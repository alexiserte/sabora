package com.sabora.server.Services.Implementation;

import com.sabora.server.DTOs.*;
import com.sabora.server.Models.*;
import com.sabora.server.Repositories.*;
import com.sabora.server.Services.QuestionService;

import java.util.List;
import java.util.stream.Collectors;

public class QuestionServicesImplementation implements QuestionService {

    private QuestionRepository questionRepository;
    private QuestionOptionRepository questionOptionRepository;

    public QuestionServicesImplementation(QuestionRepository questionRepository, QuestionOptionRepository questionOptionRepository) {
        this.questionRepository = questionRepository;
        this.questionOptionRepository = questionOptionRepository;
    }


    @Override
    public Question questionDTOToQuestion(QuestionDTO questionDTO) {
        if (questionDTO instanceof RangeQuestionDTO) {
            RangeQuestionDTO rangeDTO = (RangeQuestionDTO) questionDTO;
            RangeQuestion rangeQuestion = new RangeQuestion();
            rangeQuestion.setTitle(rangeDTO.getQuestion());
            rangeQuestion.setMin(rangeDTO.getMin());
            rangeQuestion.setMax(rangeDTO.getMax());
            return rangeQuestion;

        } else if (questionDTO instanceof MultipleAnswerQuestionDTO) {
            MultipleAnswerQuestionDTO multipleDTO = (MultipleAnswerQuestionDTO) questionDTO;
            MultipleAnswerQuestion multipleQuestion = new MultipleAnswerQuestion();
            multipleQuestion.setTitle(multipleDTO.getQuestion());
            multipleQuestion.setOptions(
                    multipleDTO.getOptions().stream()
                            .map((text) -> {
                                QuestionOption option = new QuestionOption();
                                option.setText(text);
                                option.setQuestion(multipleQuestion);
                                return option;
                            })
                            .collect(Collectors.toList())
            );
            return multipleQuestion;

        } else if (questionDTO instanceof UniqueAnswerQuestionDTO) {
            UniqueAnswerQuestionDTO uniqueDTO = (UniqueAnswerQuestionDTO) questionDTO;
            UniqueAnswerQuestion uniqueQuestion = new UniqueAnswerQuestion();
            uniqueQuestion.setTitle(uniqueDTO.getQuestion());
            uniqueQuestion.setOptions(
                    uniqueDTO.getOptions().stream()
                            .map((text) -> {
                                QuestionOption option = new QuestionOption();
                                option.setText(text);
                                option.setQuestion(uniqueQuestion);
                                return option;
                            })
                            .collect(Collectors.toList())
            );
            return uniqueQuestion;
        } else {
            throw new IllegalArgumentException("Tipo de pregunta desconocido: " + questionDTO.getClass());
        }
    }

    public QuestionDTO mapToQuestionDTO(Question question) {

        int questionId = question.getId();
        String title = question.getTitle();
        List<QuestionOption> questionOptions = questionOptionRepository.findByQuestionId(questionId);

        List<String> options = questionOptions.stream()
                .map(QuestionOption::getText)
                .collect(Collectors.toList());


        if (question instanceof RangeQuestion) {
            RangeQuestion rangeQuestion = (RangeQuestion) question;
            return new RangeQuestionDTO(
                    questionId,
                    title,
                    rangeQuestion.getMin(),
                    rangeQuestion.getMax(),
                    rangeQuestion.getInterval()
            );
        } else if (question instanceof MultipleAnswerQuestion) {
            return new MultipleAnswerQuestionDTO(
                    questionId,
                    title,
                    options
            );
        } else if (question instanceof UniqueAnswerQuestion) {
            return new UniqueAnswerQuestionDTO(
                    questionId,
                    title,
                    options
            );
        }
        else if (question instanceof AnswerWritingQuestion) {
            return new AnswerRedactionQuestionDTO(
                    questionId,
                    title
            );
        }

        throw new IllegalArgumentException("Tipo de pregunta desconocido: " + question.getClass().getName());
    }

}