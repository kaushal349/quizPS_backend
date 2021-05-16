package com.sapient.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.sql.Timestamp;


@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class AttemptsQuiz {
    private Integer attemptsQuizID;
    private Integer score;
    private Timestamp attemptsTimeStamp;
    private Timestamp submitTimeStamp;
    private Integer quizID;
    private String quizName;
    private User user;
}
