package com.sapient.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sapient.dao.QuestionDao;
import com.sapient.dao.UsersDao;
import com.sapient.entity.Question;
import com.sapient.entity.User;
import com.sapient.utils.JwtUtil;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QuestionController {

    @PostMapping("/api/question")
    public ResponseEntity<?> question(@RequestHeader(name = "Authorization", required = false) String jwt_token, @RequestBody Question question) {
        if(jwt_token == null){
            Map<String, String> mp = new HashMap<>();
            mp.put("message", "UNAUTHORIZED ACCESS denied");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(mp);
        }
        
        try{
            String token = jwt_token.split(" ")[1];
            QuestionDao questionDao = new QuestionDao();
            UsersDao usersDao = new UsersDao();
            int id = JwtUtil.verify(token);
            User user = usersDao.getUserByID(id);
            question.setAdmin(user);
            int questionId = questionDao.addQuestion(question);

            Map<String, String> mp = new HashMap<>();
            mp.put("success", "true");
            mp.put("questionId", String.valueOf(questionId));
            return ResponseEntity.ok(mp);
        }
        catch(Exception e){
            Map<String, String> mp = new HashMap<>();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(mp);
        }
    }

    @GetMapping("/api/questions/{categoryID}")
    public ResponseEntity<?> categoryQuestion(@RequestHeader(name = "Authorization", required = false) String jwt_token, @PathVariable("categoryID") int categoryID) {
        if(jwt_token == null){
            Map<String, String> mp = new HashMap<>();
            mp.put("message", "UNAUTHORIZED ACCESS denied");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(mp);
        }

        try{
            String token = jwt_token.split(" ")[1];
            JwtUtil.verify(token);
            QuestionDao questionDao = new QuestionDao();
            List<Question> questions = questionDao.getQuestionsByCategory(categoryID);
            return ResponseEntity.ok(questions);
        }
        catch(Exception e){
            Map<String, String> mp = new HashMap<>();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(mp);
        }
    }
}
