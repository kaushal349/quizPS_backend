package com.sapient.dao;

import com.sapient.entity.AttemptsQuiz;
import com.sapient.utils.DbUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class AttemptsQuizDao {
    public List<AttemptsQuiz> getUserPerformanceByUserID(int userID) throws Exception {
        String sql = "SELECT quizID, quizzes.title, score, attemptTimeStamp from attemptsQuiz JOIN quizzes ON attemptsQuiz.quizID = quizzes.ID WHERE user = ?";
        List<AttemptsQuiz> performance = new ArrayList<>();

        try(
                Connection conn = DbUtil.createConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
        ) {
            stmt.setInt(1, userID);
            try(ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    AttemptsQuiz attemptsQuiz = new AttemptsQuiz();
                    attemptsQuiz.setQuizID(rs.getInt(1));
                    attemptsQuiz.setQuizName(rs.getString(2));
                    attemptsQuiz.setScore(rs.getInt(3));
                    attemptsQuiz.setAttemptsTimeStamp(rs.getTimestamp(4));
                    performance.add(attemptsQuiz);
                }

            }

        } catch(Exception ex) {
            ex.printStackTrace();
        }

        return performance;

    }
}
