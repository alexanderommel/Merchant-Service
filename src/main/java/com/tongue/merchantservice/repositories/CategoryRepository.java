package com.tongue.merchantservice.repositories;

import com.tongue.merchantservice.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    List<Category> findAllByStoreId(Long id);
}
