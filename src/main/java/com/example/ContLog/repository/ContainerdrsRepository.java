package com.example.ContLog.repository;

import com.example.ContLog.entity.Container;
import com.example.ContLog.entity.Containerdrs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ContainerdrsRepository extends JpaRepository<Containerdrs, Long> {

    // Для (SO) ==================================================================
    // Для (SO) ==================================================================

    // Для (SO) ==================================================================
    // Для (SO) ==================================================================



    // Для Carrier (TC) ==========================================================
    // Для Carrier (TC) ==========================================================
    // Найти все заявки по ID Транспортной компании
    List<Containerdrs> findByCarrier_Id(Long carrierId);

    // Заявки перевозчика за сегодня
    @Query("SELECT c FROM Containerdrs c " +
            "JOIN c.carrier carrier " +
            "JOIN c.sale sale " +
            "WHERE carrier.id = :carrierId " +
            "AND sale.fromDate = :date")
    List<Containerdrs> findByCarrierIdAndSaleDate(
            @Param("carrierId") Long carrierId,
            @Param("date") LocalDate date);

    // Заявки перевозчика за неделю
    @Query("SELECT c FROM Containerdrs c " +
            "JOIN c.carrier carrier " +
            "JOIN c.sale sale " +
            "WHERE carrier.id = :carrierId " +
            "AND sale.fromDate BETWEEN :startOfWeek AND :endOfWeek")
    List<Containerdrs> findByCarrierIdAndCurrentWeek(
            @Param("carrierId") Long carrierId,
            @Param("startOfWeek") LocalDate startOfWeek,
            @Param("endOfWeek") LocalDate endOfWeek);

    // Заявки перевозчика за текущий месяц
    @Query("SELECT c FROM Containerdrs c " +
            "JOIN c.carrier carrier " +
            "JOIN c.sale sale " +
            "WHERE carrier.id = :carrierId " +
            "AND YEAR(sale.fromDate) = YEAR(CURRENT_DATE) " +
            "AND MONTH(sale.fromDate) = MONTH(CURRENT_DATE)")
    List<Containerdrs> findByCarrierIdAndCurrentMonth(
            @Param("carrierId") Long carrierId);

    // Заявки перевозчика за сегодня
    // Этот метод тоже работает
    // =========================================
    List<Containerdrs> findByCarrier_IdAndSale_FromDate(Long carrierId, LocalDate date);
    // =========================================
    // Для Carrier (TC) ==========================================================
    // Для Carrier (TC) ==========================================================



    // Для ContOwner (CO) ========================================================
    // Для ContOwner (CO) ========================================================
    @Query("SELECT c FROM Containerdrs c WHERE c.sale.contowner.name = :contOwnerName ")
    List<Containerdrs> findContDrsByContOwnerName(
            @Param("contOwnerName") String contOwnerName);

    // Заявки Владельца КТК на сегодня
    @Query("SELECT c FROM Containerdrs c " +
            "WHERE c.sale.contowner.name = :contOwnerName " +
            "AND c.sale.fromDate = :date")
    List<Containerdrs> findContDrsByContOwnerNameAndSaleDate(
            @Param("contOwnerName") String contOwnerName,
            @Param("date") LocalDate date);

    // Заявки Владельца КТК за текущую Неделю
    @Query("SELECT c FROM Containerdrs c " +
            "WHERE c.sale.contowner.name = :contOwnerName " +
            "AND sale.fromDate BETWEEN :startOfWeek AND :endOfWeek")
    List<Containerdrs> findContDrsByContOwnerNameAndCurrentWeek(
            @Param("contOwnerName") String contOwnerName,
            @Param("startOfWeek") LocalDate startOfWeek,
            @Param("endOfWeek") LocalDate endOfWeek);

    // Заявки Владельца КТК за текущий месяц
    @Query("SELECT c FROM Containerdrs c " +
            "WHERE c.sale.contowner.name = :contOwnerName " +
            "AND YEAR(c.sale.fromDate) = YEAR(CURRENT_DATE) " +
            "AND MONTH(c.sale.fromDate) = MONTH(CURRENT_DATE)")
    List<Containerdrs> findContDrsByContOwnerNameAndCurrentMonth(
            @Param("contOwnerName") String contOwnerName);

    // Заявки Владельца КТК с.. по..
    @Query("SELECT c FROM Containerdrs c " +
            "WHERE c.sale.contowner.name = :contOwnerName " +
            "AND YEAR(c.sale.fromDate) = YEAR(CURRENT_DATE) " +
            "AND MONTH(c.sale.fromDate) = MONTH(CURRENT_DATE)")
    List<Containerdrs> findContDrsByContOwnerNameAndSaleDateFromTo(
            @Param("contOwnerName") String contOwnerName,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate);
    // Для ContOwner (CO) ========================================================
    // Для ContOwner (CO) ========================================================



    // Для TerminalManager (TR) ==================================================
    // Для TerminalManager (TR) ==================================================

    // Найти все DRS заявки по ID Терминала Выдачи Все
    List<Containerdrs> findByFromTerminal_Id(Long terminalId);

    // Найти все DRS заявки по ID Терминала Выдачи на Сегодня
    // с учетом промежутка дат на выдачу КТК (dateFrom-dateTo)
    @Query("SELECT c FROM Containerdrs c " +
            "WHERE c.fromTerminal.id = :terminalId " +
            "AND :today BETWEEN c.dateFrom AND c.dateTo")
    List<Containerdrs> findByFromTerminalIdToday(
            @Param("terminalId") Long terminalId,
            @Param("today") LocalDate today);

    // Найти все DRS заявки по ID Терминала Сдачи
    List<Containerdrs> findByToTerminal_Id(Long terminalId);

    // Найти все DRS заявки по ID Терминала Сдачи на Сегодня
    // с учетом промежутка дат на выдачу КТК (dateFrom-dateTo)
    @Query("SELECT c FROM Containerdrs c " +
            "WHERE c.toTerminal.id = :terminalId " +
            "AND :today BETWEEN c.dateFrom AND c.dateTo")
    List<Containerdrs> findByToTerminalIdToday(
            @Param("terminalId") Long terminalId,
            @Param("today") LocalDate today);

    // DRS Заявки терминала (Выдача) за сегодня
    @Query("SELECT c FROM Containerdrs c " +
            "JOIN c.carrier carrier " +
            "JOIN c.sale sale " +
            "WHERE fromTerminal.id = :terminalId " +
            "AND sale.fromDate = :date")
    List<Containerdrs> findByTerminalIdAndSaleDate(
            @Param("terminalId") Long terminalId,
            @Param("date") LocalDate date);

    // Для TerminalManager (TR) ==================================================
    // Для TerminalManager (TR) ==================================================



    // Для Driver (DR)============================================================
    // Для Driver (DR)============================================================
    // DRS Заявки Водителя (Все)
    @Query("SELECT c FROM Containerdrs c " +
            "WHERE c.driver.id = :driverId")
    List<Containerdrs> findContDrsByDriverAll(
            @Param("driverId") Long driverId);

    // DRS Заявки Водителя (на Сегодня)
    @Query("SELECT c FROM Containerdrs c " +
            "WHERE c.driver.id = :driverId " +
            "AND :today BETWEEN c.dateFrom AND c.dateTo")
    List<Containerdrs> findContDrsByDriverToday(
            @Param("driverId") Long driverId,
            @Param("today") LocalDate today);

    // DRS Заявки Водителя (за Неделю)
    @Query("SELECT c FROM Containerdrs c " +
            "WHERE c.driver.id = :driverId " +
            "AND c.dateFrom BETWEEN :startOfWeek AND :endOfWeek")
    List<Containerdrs> findContDrsByDriverWeek(
            @Param("driverId") Long driverId,
            @Param("startOfWeek") LocalDate startOfWeek,
            @Param("endOfWeek") LocalDate endOfWeek);

    // DRS Заявки Водителя (за Месяц)
    @Query("SELECT c FROM Containerdrs c " +
            "WHERE c.driver.id = :driverId " +
            "AND YEAR(c.dateFrom) = YEAR(CURRENT_DATE) " +
            "AND MONTH(c.dateFrom) = MONTH(CURRENT_DATE)")
    List<Containerdrs> findContDrsByDriverMonth(
            @Param("driverId") Long driverId);

    // Для Driver (DR)============================================================
    // Для Driver (DR)============================================================
}
