package com.sapient.dao;

import com.sapient.entity.AnswerOption;
import com.sapient.entity.Question;
import com.sapient.utils.DbUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class QuestionDao {

    public List<AnswerOption> getAllOptionsByQuestionID(int questionID) throws Exception {
        String sql = "SELECT ID, optionText, isCorrect FROM answerOptions WHERE questionID = ?";

        List<AnswerOption> answerOptions = new ArrayList<>();

        try(
                Connection conn = DbUtil.createConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
        ) {
            stmt.setInt(1, questionID);
            try(
                    ResultSet rs = stmt.executeQuery();
            ) {
                while (rs.next()) {
                    AnswerOption answerOption = new AnswerOption();
                    answerOption.setAnswerOptionID(rs.getInt(1));
                    answerOption.setOptionText(rs.getString(2));
                    answerOption.setIsCorrect(rs.getBoolean(3));
                    answerOptions.add(answerOption);
                }
            }

        } catch(Exception ex) {
            ex.printStackTrace();
        }

        return answerOptions;
    }

    public List<Question> getAllQuestionsByQuizID(int quizID) throws Exception{
        String sql = "SELECT questions.ID, questionText FROM questions JOIN quizQuestions ON questions.ID = quizQuestions.questionID WHERE quizQuestions.quizID = ?";

        List<Question> questions = new ArrayList<>();

        try(
                Connection conn = DbUtil.createConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
        ) {
            stmt.setInt(1, quizID);
            try(
                    ResultSet rs = stmt.executeQuery();
            ) {
                while (rs.next()) {
                    Question question = new Question();
                    question.setQuestionID(rs.getInt(1));
                    question.setQuestionText(rs.getString(2));
                    int questionID = rs.getInt(1);
                    question.setAnswerOptions(this.getAllOptionsByQuestionID(questionID));
                    questions.add(question);
                }
            }

        } catch(Exception ex) {
            ex.printStackTrace();
        }

        return questions;
    }

    public int addQuestion(Question question) {
        String categoryIdSql = "SELECT ID FROM categories where name = ? LIMIT 1";
        String insertQuestionSql = "INSERT INTO questions (questionText, admin, category) VALUES(?, ?, ?)";
        String insertAnswerOptionsSql = "INSERT INTO answerOptions (optionText, isCorrect, questionID) VALUES(?, ?, ?)";
        int categoryId = 0;
        int questionId = 0;

        try(
                Connection conn = DbUtil.createConnection();
                PreparedStatement stmt1 = conn.prepareStatement(categoryIdSql);
                PreparedStatement stmt2 = conn.prepareStatement(insertQuestionSql, Statement.RETURN_GENERATED_KEYS);
                PreparedStatement stmt3 = conn.prepareStatement(insertAnswerOptionsSql);
        ) {
            stmt1.setString(1, question.getCategory().getName());
            
            try(
                    ResultSet rs = stmt1.executeQuery();
            ) {
                while (rs.next()) {
                    categoryId = rs.getInt(1);
                }
            }
            System.out.println(categoryId);

            stmt2.setString(1, question.getQuestionText());
            stmt2.setInt(2, question.getAdmin().getUserID());
            stmt2.setInt(3, categoryId);

            int affectedRows = stmt2.executeUpdate();
            System.out.println(affectedRows);

            try (ResultSet generatedKeys = stmt2.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    questionId = generatedKeys.getInt(1);
                }
            }

            System.out.println(questionId);

            for(AnswerOption answerOption : question.getAnswerOptions()){
                System.out.println(answerOption);
                stmt3.setString(1, answerOption.getOptionText());
                stmt3.setBoolean(2, answerOption.getIsCorrect());
                stmt3.setInt(3, questionId);
                System.out.println(stmt3.executeUpdate());
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }

        
        return questionId;
    }

    public List<Question> getQuestionsByCategory(int categoryId) {
        String sql = "SELECT * FROM questions WHERE category = ?";
        String answerOptionSql = "SELECT * FROM answerOptions WHERE questionID = ?";
        List<Question> questions = new ArrayList<>();

        try(
                Connection conn = DbUtil.createConnection();
                PreparedStatement stmt1 = conn.prepareStatement(sql);
                PreparedStatement stmt2 = conn.prepareStatement(answerOptionSql)
        ) {
            stmt1.setInt(1, categoryId);
            ResultSet rs = stmt1.executeQuery();
            while (rs.next()) {
                Question question = new Question();
                question.setQuestionID(rs.getInt(1));
                question.setQuestionText(rs.getString(2));
                questions.add(question);
            }

            for(int i = 0; i < questions.size(); i++){
                Question currentQuestion = questions.get(i);
                stmt2.setInt(1, currentQuestion.getQuestionID());
                ResultSet rs2 = stmt2.executeQuery();
                List<AnswerOption> answerOptions = new ArrayList<>();
                
                while(rs2.next()) {
                    AnswerOption answerOption = new AnswerOption();
                    answerOption.setAnswerOptionID(rs2.getInt(1));
                    answerOption.setOptionText(rs2.getString(2));
                    answerOption.setIsCorrect(rs2.getBoolean(3));
                    answerOption.setQuestionID(currentQuestion.getQuestionID());
                    answerOptions.add(answerOption);
                }
                
                currentQuestion.setAnswerOptions(answerOptions);
                questions.set(i, currentQuestion);
            }
        } catch(Exception ex) {
            System.out.println("error occured");
            System.out.println(ex.getMessage());
        }

        
        System.out.println(questions);
        return questions;
    }
}
