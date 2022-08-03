package com.tongue.merchantservice.repositories;

import com.tongue.merchantservice.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu,Long> {
}
