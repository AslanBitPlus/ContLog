package com.example.ContLog.service;

import com.example.ContLog.entity.Carrier;
import com.example.ContLog.entity.Contowner;
import com.example.ContLog.entity.Driver;
import com.example.ContLog.repository.CarrierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarrierService {

    @Autowired
    private CarrierRepository carrierRepository;

    // Поиск всех владельцев Авто
    public List<Carrier> getAllCarriers() {
        return carrierRepository.findAll();
    }

    // Поиск Авто по Id
    public Carrier getCarrier(Long id) {
        return carrierRepository.findById(id).orElse(null);
    }

    // Поиск Перевозчика по Id по Id
    public Carrier getCarrierById(Long id) {
        return carrierRepository.findById(id).orElse(null);
    }

    // Поиск Перевозчика по Id по Id
    public Carrier getCarrierByName(String name) {
        return carrierRepository.findByName(name);
    }

    // Сохранение Владельца Авто
    public Carrier createCarrier(Carrier carrier) {
        return carrierRepository.save(carrier);
    }

    // Обновление данных Владельца Авто в БД
    public void updateCarrier(Carrier carrier, Long id) {
        Carrier carr = carrierRepository.getReferenceById(id);
        carr.setName(carrier.getName());
        carrierRepository.save(carr);
    }

    // Удаление Владельца Авто
    public void deleteCarrier(Long id) {
        carrierRepository.deleteById(id);
    }
}
