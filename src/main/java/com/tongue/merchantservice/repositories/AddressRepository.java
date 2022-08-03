package com.tongue.merchantservice.repositories;

import com.tongue.merchantservice.domain.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address,Long> {
}
