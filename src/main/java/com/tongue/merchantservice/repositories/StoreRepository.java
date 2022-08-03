package com.tongue.merchantservice.repositories;

import com.tongue.merchantservice.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store,Long> {
}
