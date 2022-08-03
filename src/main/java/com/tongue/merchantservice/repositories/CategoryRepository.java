package com.tongue.merchantservice.repositories;

import com.tongue.merchantservice.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Long> {
}
