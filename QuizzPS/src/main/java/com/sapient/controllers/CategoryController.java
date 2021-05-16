package com.sapient.controllers;

import com.sapient.dao.CategoryDao;
import com.sapient.entity.Category;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CategoryController {

    CategoryDao dao = new CategoryDao();

    @GetMapping("/api/category")
    public List<Category> getAll() throws Exception{
        return dao.getAll();
    }
}
