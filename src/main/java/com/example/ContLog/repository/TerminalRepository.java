package com.example.ContLog.repository;

import com.example.ContLog.entity.Carrier;
import com.example.ContLog.entity.Terminal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TerminalRepository extends JpaRepository<Terminal, Long> {
    // Поиск Терминала по Короткому имени
    Terminal findByShortName(String shortName);
}
