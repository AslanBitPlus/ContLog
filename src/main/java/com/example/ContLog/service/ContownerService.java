package com.example.ContLog.service;

import com.example.ContLog.entity.Container;
import com.example.ContLog.entity.Contowner;
import com.example.ContLog.repository.ContainerRepository;
import com.example.ContLog.repository.ContownerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContownerService {

    @Autowired
    private ContownerRepository contownerRepository;

    // Поиск всех владельцев КТК
    public List<Contowner> getAllContowners() {
        return contownerRepository.findAll();
    }

    // Поиск владельца по Id
    public Contowner getContowner(Long id) {
        return contownerRepository.findById(id).orElse(null);
    }

    // Сохранение Владельца
    public Contowner createContowner(Contowner contowner) {
        return contownerRepository.save(contowner);
    }

    // Обновление данных Владельца КТК в БД
    public void updateContowner(Contowner contowner, Long id) {
        Contowner cownr = contownerRepository.getReferenceById(id);
        cownr.setName(contowner.getName());
        contownerRepository.save(cownr);
    }

    // Удаление Владельца КТК
    public void deleteContowner(Long id) {
        contownerRepository.deleteById(id);
    }
}
