package com.sapient.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;


@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Quiz {
    private Integer quizID;
    private String quizName;
    private String description;
    private Integer duration;
    private Timestamp creationTimeStamp;
    private User admin;

    private List<Question> questions;
}
