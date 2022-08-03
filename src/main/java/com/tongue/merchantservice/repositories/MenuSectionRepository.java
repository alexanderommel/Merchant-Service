package com.tongue.merchantservice.repositories;

import com.tongue.merchantservice.domain.MenuSection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuSectionRepository extends JpaRepository<MenuSection,Long> {
}
