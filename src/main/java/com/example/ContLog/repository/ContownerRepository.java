package com.example.ContLog.repository;

import com.example.ContLog.entity.Contowner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContownerRepository extends JpaRepository<Contowner, Long> {
    Contowner findByName(String name);
    Optional<Contowner> findById(Long id);
}
