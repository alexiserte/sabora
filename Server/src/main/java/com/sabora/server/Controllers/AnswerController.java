package com.sabora.server.Controllers;

import com.sabora.server.DTOs.FormAnswerDTO;
import com.sabora.server.Services.FormAnswerServices;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
public class AnswerController {

    private FormAnswerServices formAnswerServices;
    private static final Logger log = Logger.getLogger(AnswerController.class.getName());

    public AnswerController(FormAnswerServices formAnswerServices) {
        this.formAnswerServices = formAnswerServices;
    }

    @PostMapping("/form/{id}/answer")
    public ResponseEntity<?> answerForm(@PathVariable String id, @RequestBody FormAnswerDTO formAnswerDTO) {
        try {
            log.info("Answering form with id: " + id);
            formAnswerServices.answerForm(formAnswerDTO);
            return ResponseEntity.ok("Respuestas almacenadas correctamente!");
        } catch (Exception e) {
            log.severe("Error answering form with id: " + id);
            log.severe(e.getMessage());
            return new ResponseEntity<>(e.getStackTrace(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/form/{id}/answers")
    public ResponseEntity<?> getFormAnswers(@PathVariable String id) {
        try{
            log.info("Getting answers to form with id: " + id);
            return ResponseEntity.ok(formAnswerServices.getFormAnswers(Integer.parseInt(id)));
        } catch (Exception e) {
            log.severe("Error getting answers to form with id: " + id);
            log.severe(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/form/{id}/question/{questionId}/answers")
    public ResponseEntity<?> getAnswersToAQuestion(@PathVariable String id, @PathVariable String questionId) {
        try{
            log.info("Getting answers to question with id: " + questionId + " from form with id: " + id);
            return ResponseEntity.ok(formAnswerServices.getAnswersToAQuestion(Integer.parseInt(questionId)));
        } catch (Exception e) {
            log.severe("Error getting answers to question with id: " + questionId + " from form with id: " + id);
            log.severe(e.getMessage());
            return ResponseEntity.internalServerError().build();

        }
    }
}
