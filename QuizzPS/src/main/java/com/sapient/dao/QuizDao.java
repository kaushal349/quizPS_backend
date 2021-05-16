package com.sapient.dao;

import com.sapient.entity.Quiz;
import com.sapient.utils.DbUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class QuizDao {

    public List<Quiz> getAllQuizzesForUser() throws Exception{
        String sql = "SELECT ID, title, creationTimeStamp FROM quizzes";

        List<Quiz> quizzes = new ArrayList<>();

        try(
                Connection conn = DbUtil.createConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();
        ) {

            while(rs.next()) {
                Quiz quiz = new Quiz();
                quiz.setQuizID(rs.getInt(1));
                quiz.setQuizName(rs.getString(2));
                quiz.setCreationTimeStamp(rs.getTimestamp(3));
                quizzes.add(quiz);
            }

        } catch(Exception ex) {
            ex.printStackTrace();
        }

        return quizzes;
    }


    public List<Quiz> getAllQuizzesByAdminID(int adminID) throws Exception{
        String sql = "SELECT ID, title, creationTimeStamp FROM quizzes WHERE admin = ?";

        List<Quiz> quizzes = new ArrayList<>();

        try(
                Connection conn = DbUtil.createConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
        ) {
            stmt.setInt(1, adminID);
            try(
                    ResultSet rs = stmt.executeQuery();
                    ) {
                while (rs.next()) {
                    Quiz quiz = new Quiz();
                    quiz.setQuizID(rs.getInt(1));
                    quiz.setQuizName(rs.getString(2));
                    quiz.setCreationTimeStamp(rs.getTimestamp(3));
                    quizzes.add(quiz);
                }
            }

        } catch(Exception ex) {
            ex.printStackTrace();
        }

        return quizzes;
    }


    public Quiz getQuizByID(int quizID) throws Exception{
        String sql = "SELECT title, creationTimeStamp, description, duration FROM quizzes WHERE ID = ?";

        Quiz quiz = new Quiz();

        try(
                Connection conn = DbUtil.createConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
        ) {
            stmt.setInt(1, quizID);
            try(
                    ResultSet rs = stmt.executeQuery();
            ) {
                while (rs.next()) {
                    quiz.setQuizName(rs.getString(1));
                    quiz.setCreationTimeStamp(rs.getTimestamp(2));
                    quiz.setDescription(rs.getString(3));
                    quiz.setDuration(rs.getInt(4));
                }
            }

        } catch(Exception ex) {
            ex.printStackTrace();
        }

        return quiz;
    }

    public boolean checkIfAdminCreatedQuiz(int quizID, int adminID) throws Exception {
        String sql = "SELECT count(*) from quizzes WHERE ID = ? AND admin = ?";

        int count = 0;

        try(
                Connection conn = DbUtil.createConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ) {

            stmt.setInt(1, quizID);
            stmt.setInt(2, adminID);

            try(
                    ResultSet rs = stmt.executeQuery();
                    ) {
                if(rs.next()) {
                    count = rs.getInt(1);
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return count > 0;
    }
}
