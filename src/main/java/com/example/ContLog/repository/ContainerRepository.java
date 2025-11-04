package com.example.ContLog.repository;

import com.example.ContLog.entity.Container;
import com.example.ContLog.view.ContainerView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContainerRepository extends JpaRepository<Container, Long>  {
    // Поиск КТК по номеру
    Container findByNumber(String number);

    // Поиск всех контейнеров по ID владельца
    List<Container> findByContownerId(Long contownerId);

    // Поиск всех контейнеров по ID владельца
    @Query("SELECT c FROM Container c WHERE c.contowner.name = :contOwnerName")
    List<Container> findByContOwnerName(
            @Param("contOwnerName") String contOwnerName);

    // Поиск всех контейнеров по ID владельца и рассположенных на терминале
    // в том числе: ремонтные, и выставленные на продажу
    @Query("SELECT c FROM Container c WHERE c.contowner.name = :contOwnerName " +
    "AND c.place = 't' OR c.place = 'b' OR c.place = 'o'")
    List<Container> findContainersByOwnerAndFromTerminals(
            @Param("contOwnerName") String contOwnerName);

    // Поиск всех контейнеров по ID владельца и рассположенных на авто
    @Query("SELECT c FROM Container c WHERE c.contowner.name = :contOwnerName " +
            "AND c.place = 'd'")
    List<Container> findContainersByOwnerAndFromAvto(
            @Param("contOwnerName") String contOwnerName);

    // Поиск КТК для Выдачи с Терминала (по id Терминала,
    // по id Владельца, Порожние - container.w == 0,
    // по container.place == 't')
    @Query("SELECT c FROM Container c WHERE c.contowner.id = :contOwnerId " +
            "AND c.place = 't' " +
            "AND c.placeId = :terminalId " +
            "AND c.w = 0")
    List<Container> findContainersFromOutByContOwnerAndTerminalAndEmpty(
            @Param("terminalId") Long terminalId,
            @Param("contOwnerId") Long contOwnerId);

    // Поиск всех контейнеров на Терминале с id = terminalId
    @Query("SELECT c FROM Container c WHERE c.placeId = :terminalId " +
            "AND c.place = 't'")
    List<Container> findContainersFromTerminal(
            @Param("terminalId") Long terminalId);

    // Поиск всех контейнеров на Терминале с id = terminalId
    // Порожние
    @Query("SELECT c FROM Container c WHERE c.placeId = :terminalId " +
            "AND c.place = 't' " +
            "AND c.w = 0")
    List<Container> findContainersFromTerminalFree(
            @Param("terminalId") Long terminalId);

    // Поиск всех контейнеров на Терминале с id = terminalId
    // Груженые
    @Query("SELECT c FROM Container c WHERE c.placeId = :terminalId " +
            "AND c.place = 't' " +
            "AND c.w = 1")
    List<Container> findContainersFromTerminalFull(
            @Param("terminalId") Long terminalId);

    // Поиск всех контейнеров на Терминале с id = terminalId
    // Ремонтные
    @Query("SELECT c FROM Container c WHERE c.placeId = :terminalId " +
            "AND c.place = 'b'")
    List<Container> findContainersFromTerminalBad(
            @Param("terminalId") Long terminalId);

    // Поиск всех контейнеров на Терминале с id = terminalId
    // На продажу
    @Query("SELECT c FROM Container c WHERE c.placeId = :terminalId " +
            "AND c.place = 'o'")
    List<Container> findContainersFromTerminalOut(
            @Param("terminalId") Long terminalId);

}
