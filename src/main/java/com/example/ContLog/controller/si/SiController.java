package com.example.ContLog.controller.si;

import com.example.ContLog.entity.Sale;
import com.example.ContLog.repository.ContownerRepository;
import com.example.ContLog.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
public class SiController {

    @Autowired
    private SaleService saleService;

    @Autowired
    private ContownerRepository contownerRepository;

    // Чтение всех заявок
    @GetMapping("/si_sales")
    public String listSiSales(Model model) {
        model.addAttribute("sales", saleService.getAllSales());
        model.addAttribute("today", LocalDate.now());
        return "01_si_manager/si_sale"; // ??
    }

    // Новая заявка (Get)
    @GetMapping("/si_sales/create")
    public String createSalePage(Model model) {
        Sale sale = new Sale();
        String creationDateTime = sale.creationDateTimeToStr();
        model.addAttribute("sale", sale);
        model.addAttribute("contowners", contownerRepository.findAll());;
        model.addAttribute("isUpdate", false);
        model.addAttribute("creationDateTime", creationDateTime);
        return "01_si_manager/si_sale_crud";
    }

    // Новая заявка (Post)
    @PostMapping("/si_sales/create")
    public String createSale(@ModelAttribute("sale") Sale sale) {
        saleService.createSale(sale);
        return "redirect:/si_sales";
    }

    // Обновление заявки (Get)
    @GetMapping("/si_sales/update/{id}")
    public String updateSalePage(@PathVariable Long id, Model model) {
        Sale sale = saleService.getSale(id);
        String fromDate = sale.fromDateToStr();
        String toDate = sale.toDateToStr();
        model.addAttribute("sale", sale);
        model.addAttribute("contowners", contownerRepository.findAll());;
        model.addAttribute("isUpdate", true);
        model.addAttribute("fromDate", fromDate);
        model.addAttribute("toDate", toDate);
        return "01_si_manager/si_sale_crud"; // ??
    }

    // Обновление заявки (Post)
    @PostMapping("/si_sales/update/{id}")
    public String updateSale(@ModelAttribute("sale") Sale sale,
                                @PathVariable Long id) {
        saleService.updateSale(sale, id);
        return "redirect:/si_sales";
    }

    // Удаление заявки
    @GetMapping("/si_sales/delete/{id}")
    public String deleteSale(@PathVariable Long id) {
        saleService.deleteSale(id);
        return "redirect:/si_sales";
    }

    // Сервис - Заявки на сегодня
    @GetMapping("/si_sales_today")
    public String listSiSalesToday(Model model) {
        model.addAttribute("sales", saleService.getTodaySales());
        return "01_si_manager/si_sale_today"; // ??
    }


}
