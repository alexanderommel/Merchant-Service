package com.tongue.merchantservice.repositories;

import com.tongue.merchantservice.domain.StoreVariant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreVariantRepository extends JpaRepository<StoreVariant,Long> {
}
