package com.sapient.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;


@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Question {
    private Integer questionID;
    private String questionText;
    private Category category;
    private List<AnswerOption> answerOptions;
    private User admin;
}
