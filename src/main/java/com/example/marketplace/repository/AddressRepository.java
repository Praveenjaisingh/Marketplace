package com.example.marketplace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.marketplace.entity.Address;

public interface AddressRepository extends JpaRepository<Address, Integer> {

}
