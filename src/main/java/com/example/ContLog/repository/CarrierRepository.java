package com.example.ContLog.repository;

import com.example.ContLog.entity.Carrier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarrierRepository extends JpaRepository<Carrier, Long>{
    Carrier findByName(String name);
    Optional<Carrier> findById(Long id);
}
