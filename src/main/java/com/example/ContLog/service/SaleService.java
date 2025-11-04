package com.example.ContLog.service;

import com.example.ContLog.entity.Sale;
import com.example.ContLog.repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SaleService {

    @Autowired
    private SaleRepository saleRepository;

    // Поиск всех заявок
    public List<Sale> getAllSales() {
        return saleRepository.findAll();
    }

    // Поиск Заявки по Id
    public Sale getSale(Long id) {
        return saleRepository.findById(id).orElse(null);
    }

    // Сохранение заявки
    public void createSale(Sale sale) {
        saleRepository.save(sale);
    }

    // Обновление данных заявки в БД
    public void updateSale(Sale sale, Long id) {
        Sale sl = saleRepository.getReferenceById(id);
        sl.setContowner(sale.getContowner());
        sl.setContainer(sale.getContainer());
        sl.setFromDate(sale.getFromDate());
        sl.setFromTime(sale.getFromTime());
        sl.setFromAddress(sale.getFromAddress());
        sl.setToDate(sale.getToDate());
        sl.setToTime(sale.getToTime());
        sl.setToAddress(sale.getToAddress());
        sl.setManager(sale.getManager());
        sl.setStarted(sale.isStarted());
        sl.setFinished(sale.isFinished());
//        sl.setContainerdrss(sale.getContainerdrss());
        saleRepository.save(sl);
    }

    // Поиск заявок по дате
    public List<Sale> getTodaySales() {
        LocalDate today = LocalDate.now();
        return saleRepository.findAll().stream()
                .filter(sale -> today.equals(sale.getFromDate()))
                .collect(Collectors.toList());
    }

    // Удаление Заявки
    public void deleteSale(Long id) {
        saleRepository.deleteById(id);
    }


}
