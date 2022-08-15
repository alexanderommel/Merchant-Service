package com.tongue.merchantservice.repositories;

import com.tongue.merchantservice.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store,Long> {

    Optional<Store> findByRuc(String ruc);
    Optional<Store> findByName(String name);

    Optional<Store> findByMerchantId(Long id);
}
