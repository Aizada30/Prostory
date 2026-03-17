package com.prostory.service.serviceImpl;

import com.prostory.dto.request.CategoryRequest;
import com.prostory.dto.response.CategoryResponse;
import com.prostory.entity.Category;
import com.prostory.repository.CategoryRepository;
import com.prostory.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public CategoryResponse create(CategoryRequest request) {
        if (categoryRepository.existsByName(request.name())) {
            throw new RuntimeException("Категория уже существует");
        }
        Category category = Category.builder()
                .name(request.name())
                .build();
        return toResponse(categoryRepository.save(category));
    }

    @Override
    public CategoryResponse getById(Long id) {
        return toResponse(findOrThrow(id));
    }

    @Override
    public List<CategoryResponse> getAll() {
        return categoryRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryResponse update(Long id, CategoryRequest request) {
        Category category = findOrThrow(id);
        category.setName(request.name());
        return toResponse(categoryRepository.save(category));
    }

    @Override
    public void delete(Long id) {
        findOrThrow(id);
        categoryRepository.deleteById(id);
    }

    private Category findOrThrow(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Категория не найдена: " + id));
    }

    private CategoryResponse toResponse(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName()
        );
    }
}
