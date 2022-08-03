package com.tongue.merchantservice.repositories;

import com.tongue.merchantservice.domain.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MerchantRepository extends JpaRepository<Merchant,Long> {
}
