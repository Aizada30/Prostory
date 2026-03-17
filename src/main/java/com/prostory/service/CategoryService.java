package com.prostory.service;

import com.prostory.dto.request.CategoryRequest;
import com.prostory.dto.response.CategoryResponse;
import com.prostory.entity.Category;

import java.util.List;

public interface CategoryService {
    CategoryResponse create(CategoryRequest request);
    CategoryResponse getById(Long id);
    List<CategoryResponse> getAll();
    CategoryResponse update(Long id, CategoryRequest request);
    void delete(Long id);
}
