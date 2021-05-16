package com.sapient.controllers;

import com.sapient.dao.QuestionDao;
import com.sapient.dao.QuizDao;
import com.sapient.dao.UsersDao;
import com.sapient.entity.Quiz;
import com.sapient.entity.User;
import com.sapient.utils.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class QuizController {

    @GetMapping("/api/quizzes")
    public ResponseEntity<?> getAllQuizzes(@RequestHeader(name = "Authorization", required = false) String authHeader) {

        if(authHeader == null) {
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("error", "Authorization token is missing.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMap);
        }

        try {
            String token = authHeader.split(" ")[1];

            int userID = JwtUtil.verify(token);

            UsersDao usersDao = new UsersDao();
            User registeredUser = usersDao.getUserByID(userID);

            QuizDao quizDao = new QuizDao();

            Map<String, Object> map = new HashMap<>();

            if(registeredUser.isAdmin()) {
                List<Quiz> quizzes = quizDao.getAllQuizzesByAdminID(userID);
                map.put("success", true);
                map.put("quizzes", quizzes);
            } else {
                List<Quiz> quizzes = quizDao.getAllQuizzesForUser();
                map.put("success", true);
                map.put("quizzes", quizzes);
            }

            return ResponseEntity.ok(map);

        } catch (Exception ex) {
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("error", "Authorization token is invalid or " + ex.getMessage());

            ex.printStackTrace();

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMap);
        }
    }

    @GetMapping("/api/quizzes/{quizID}")
    public ResponseEntity<?> getQuizByID(@RequestHeader(name = "Authorization", required = false) String authHeader, @PathVariable int quizID) {

        if(authHeader == null) {
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("error", "Authorization token is missing.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMap);
        }

        try {
            String token = authHeader.split(" ")[1];

            int userID = JwtUtil.verify(token);

            UsersDao usersDao = new UsersDao();
            User registeredUser = usersDao.getUserByID(userID);

            QuizDao quizDao = new QuizDao();

            Map<String, Object> map = new HashMap<>();

            if(registeredUser.isAdmin()) {
                if(quizDao.checkIfAdminCreatedQuiz(quizID, userID)) {
                    Quiz quiz = quizDao.getQuizByID(quizID);

                    QuestionDao questionDao = new QuestionDao();
                    quiz.setQuestions(questionDao.getAllQuestionsByQuizID(quizID));

                    map.put("success", true);
                    map.put("quizName", quiz.getQuizName());
                    map.put("creationTimeStamp", quiz.getCreationTimeStamp());
                    map.put("description", quiz.getDescription());
                    map.put("duration", quiz.getDuration());
                    map.put("questions", quiz.getQuestions());
                }
                else {
                    Map<String, Object> errorMap = new HashMap<>();
                    errorMap.put("error", "Not authorized to access this quiz.");
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMap);
                }

            } else {
                Quiz quiz = quizDao.getQuizByID(quizID);

                map.put("success", true);
                map.put("quizName", quiz.getQuizName());
                map.put("creationTimeStamp", quiz.getCreationTimeStamp());
                map.put("description", quiz.getDescription());
                map.put("duration", quiz.getDuration());
            }

            return ResponseEntity.ok(map);

        } catch (Exception ex) {
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("error", "Authorization token is invalid or " + ex.getMessage());

            ex.printStackTrace();

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMap);
        }
    }

}
