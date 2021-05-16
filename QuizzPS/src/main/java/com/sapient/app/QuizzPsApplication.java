package com.sapient.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.sapient.controllers.AttemptsQuizController;
import com.sapient.controllers.CategoryController;
import com.sapient.controllers.QuestionController;
import com.sapient.controllers.QuizController;
import com.sapient.controllers.UsersController;

@SpringBootApplication
@ComponentScan(basePackageClasses = UsersController.class)
@ComponentScan(basePackageClasses = QuizController.class)
@ComponentScan(basePackageClasses = AttemptsQuizController.class)
@ComponentScan(basePackageClasses = CategoryController.class)
@ComponentScan(basePackageClasses = QuestionController.class)
public class QuizzPsApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuizzPsApplication.class, args);
	}

}
