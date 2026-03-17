package com.prostory.repository;

import com.prostory.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("SELECT COUNT(c) > 0 FROM Category c WHERE c.name = :name")
    boolean existsByName(@Param("name") String name);
}