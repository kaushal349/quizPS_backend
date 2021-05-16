package com.sapient.dao;

import com.sapient.entity.Category;
import com.sapient.utils.DbUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CategoryDao {
    public List<Category> getAll() {
        String sql="SELECT * FROM categories;";

        List<Category> categoryList = new ArrayList<>();

        try(
                Connection conn = DbUtil.createConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();
        ) {

            while(rs.next()) {
                Category category = new Category();
                category.setCategoryID(rs.getInt(1));
                category.setName(rs.getString(2));
                categoryList.add(category);
            }

        } catch(Exception ex) {
            ex.printStackTrace();
        }

        return categoryList;
    }
}
