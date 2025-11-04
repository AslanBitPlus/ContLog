package com.example.ContLog.repository;

import com.example.ContLog.entity.Contowner;
import com.example.ContLog.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {

}
