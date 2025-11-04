package com.example.ContLog.controller.so;

import com.example.ContLog.entity.*;
import com.example.ContLog.repository.*;
import com.example.ContLog.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class SoController {

    @Autowired
//    private CarrierService carrierService;
    private CarrierRepository carrierRepository;

    @Autowired
//    private SaleService saleService;
    private SaleRepository saleRepository;

    @Autowired
    private ContainerRepository containerRepository;

    @Autowired
//    private TerminalService terminalService;
    private TerminalRepository terminalRepository;

    @Autowired
    private ContainerdrsService containerdrsService;
//    private ContainerdrsRepository containerdrsRepository;

    // Чтение всех заявок
    @GetMapping("/so_contdrses")
    public String soSalePage(Model model) {
        model.addAttribute("contdrses", containerdrsService.getAllContainerDrs());
        return "02_so_manager/so_contdrs";
    }

    // =================================================================

    // Новое Распределение (Get)
    @GetMapping("/so_contdrses/create")
    public String createContainerDrsPage(Model model) {
        Containerdrs contdrs = new Containerdrs();
        String dateReg = contdrs.dateRegToStr();
        model.addAttribute("contdrs", contdrs);
        model.addAttribute("sales", saleRepository.findAll());
        model.addAttribute("carriers", carrierRepository.findAll());
        model.addAttribute("containers", containerRepository.findAll());
        model.addAttribute("isUpdate", false);
        model.addAttribute("dateReg", dateReg);
        return "02_so_manager/so_contdrs_crud";
    }


    // Новое Распределение (Post)
    @PostMapping("/so_contdrses/create")
    public String createContainerDrs(@ModelAttribute("contdrs") Containerdrs containerdrs) {
        // Проверка записи новой DRS Заявки
        System.out.println("Сохранение ======================================================");
        containerdrs.printDrs(containerdrs);
        System.out.println("Сохранение ======================================================");

        containerdrsService.createContainerDrs(containerdrs);

        return "redirect:/so_contdrses";
    }

    // Обновление заявки (Get)
    @GetMapping("/so_contdrses/update/{id}")
    public String updateContainerDrsPage(@PathVariable Long id, Model model) {
        Containerdrs containerdrs = containerdrsService.getContainerDrs(id);
        String dateFrom = containerdrs.dateFromToStr();
        String dateTo = containerdrs.dateToToStr();
        model.addAttribute("contdrs", containerdrs);
        model.addAttribute("carriers", carrierRepository.findAll());
        model.addAttribute("isUpdate", true);
        model.addAttribute("dateFrom", dateFrom);
        model.addAttribute("dateTo", dateTo);

        return "02_so_manager/so_contdrs_crud"; // ??
    }

    // Обновление заявки (Post)
    @PostMapping("/so_contdrses/update/{id}")
    public String updateContainerDrs(@ModelAttribute("contdrs") Containerdrs containerdrs,
                             @PathVariable Long id) {
        // Проверка записи обновленной DRS Заявки
        System.out.println("Сохранение записи ContainerDrs ==================");
        containerdrs.printDrs(containerdrs);
        System.out.println("=================================================");
        //
        containerdrsService.updateContainerdrsSo(containerdrs, id);

        return "redirect:/so_contdrses";
    }

    // Удаление DRS заявки
    @GetMapping("/so_contdrses/delete/{id}")
    public String deleteContainerDrs(@PathVariable Long id) {
        containerdrsService.deleteContainerdrs(id);
        return "redirect:/so_contdrses";
    }

    @GetMapping("/so_carrier")
    public String soCarrierPage(Model model) {
        model.addAttribute("carriers", carrierRepository.findAll());
        return "02_so_manager/so_carrier"; // ??
    }

    // Сервис - Заявки на сегодня
    @GetMapping("/so_contdrses_today")
    public String listSiSalesToday(Model model) {
        model.addAttribute("contdrses", containerdrsService.getTodayContainerDrs());
        return "02_so_manager/so_contdrses_today";
    }


}
