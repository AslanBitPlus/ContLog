package com.example.ContLog.repository;

import com.example.ContLog.entity.Carrier;
import com.example.ContLog.entity.Container;
import com.example.ContLog.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {
    // Найти всех водителей по ID Транспортной компании
    List<Driver> findByCarrierId(Long carrierId);
}
