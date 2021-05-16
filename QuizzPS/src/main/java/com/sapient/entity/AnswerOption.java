package com.sapient.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;


@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class AnswerOption {
    private Integer answerOptionID;
    private String optionText;
    private Boolean isCorrect;
    private Integer questionID;
}
