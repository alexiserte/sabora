package com.sabora.server.Models;

import com.sabora.server.Services.Implementation.ClienteService;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.hibernate.annotations.Check;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "respuesta_unica")
@PrimaryKeyJoinColumn(name = "id_pregunta")
public class UniqueAnswerQuestion extends Question{

    @Builder
    public UniqueAnswerQuestion(int id, String value, Form form, List<QuestionOption> options) {
        super(id, value, form, options);
    }

}