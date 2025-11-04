package com.example.ContLog.service;

import com.example.ContLog.entity.Carrier;
import com.example.ContLog.entity.Driver;
import com.example.ContLog.repository.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DriverService {

    @Autowired
    private DriverRepository driverRepository;

    // Поиск всех водителей
    public List<Driver> getAllDrivers() {
        return driverRepository.findAll();
    }

    // Поиск Водителя по Id
    public Driver getDriver(Long id) {
        return driverRepository.findById(id).orElse(null);
    }

    // Сохранение водителя
    public Driver createDriver(Driver driver) {
        return driverRepository.save(driver);
    }

    // Обновление данных Водителя в БД
    public void updateDriver(Driver driver, Long id) {
        Driver drv = driverRepository.getReferenceById(id);
        drv.setCarrier(driver.getCarrier());
        drv.setSurename(driver.getSurename());
        drv.setName(driver.getName());
        drv.setPatronymic(driver.getPatronymic());
        drv.setCar(driver.getCar());
        drv.setCarnum(driver.getCarnum());
        drv.setTrailer(driver.getTrailer());
        drv.setTrailernum(driver.getTrailernum());
        driverRepository.save(drv);
    }

    // Удаление водителя
    public void deleteDriver(Long id) {
        driverRepository.deleteById(id);
    }

    // Поиск водителей Транспортной Компании по id компании
//    public List<Driver> getOwnerDrivers(int crrid) {
//        return driverRepository.getCarrierDrivers(crrid);
//    }
}
