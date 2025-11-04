package com.example.ContLog.controller;

import com.example.ContLog.entity.Carrier;
import com.example.ContLog.entity.Driver;
import com.example.ContLog.repository.CarrierRepository;
import com.example.ContLog.service.CarrierService;
import com.example.ContLog.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

// @PreAuthorize("hasRole('SUPERADMIN', 'TC_MANAGER')")
@Controller
public class DriverController {

    @Autowired
    private DriverService driverService;

    @Autowired
    private CarrierService carrierService;

    @Autowired
    private CarrierRepository carrierRepository;

    @GetMapping("/drivers")
    public String listDrivers(Model model) {
        model.addAttribute("drivers", driverService.getAllDrivers());
//        model.addAttribute("drivers", driverService.getCarrierDrivers(1));
        return "driver/list";
    }

    @GetMapping("/drivers/create")
    public String createDriverPage(Model model) {
        Driver driver = new Driver();
        Carrier carrier = new Carrier();
        model.addAttribute("driver", driver);
        model.addAttribute("isUpdate", false);
        model.addAttribute("carriers", carrierService.getAllCarriers());
        return "driver/crud"; // ??
    }

    @GetMapping("/drivers/update/{id}")
    public String updateDriverPage(@PathVariable Long id, Model model) {
        Driver driver = driverService.getDriver(id);
        // Определяем имя Владельца Авто
        // Для отображения в поле по умолчанию
        String carr = driver.getCarrier().getName();

        model.addAttribute("driver", driver);
        model.addAttribute("isUpdate", true);

        // Отправляем на crud.http имя Владельца Авто
        // Для отображения в поле по умолчанию
        model.addAttribute("carr", carr);

        model.addAttribute("carriers", carrierService.getAllCarriers());
        return "driver/crud"; // ??
    }

    @PostMapping("/drivers/update/{id}")
    public String createDriver(@ModelAttribute("driver") Driver driver,
                               @RequestParam("carrier.name") String carr,
                               @PathVariable Long id,
                               Model model) {
        // Находим Владельца по имени полученному из crud.html carr
        // и записываем его как Владельца Авто в поле carrier
        Carrier carrier = carrierRepository.findByName(carr);
        driver.setCarrier(carrier);
        // Обновляем строку таблицы Водителей
        driverService.updateDriver(driver, id);
        return "redirect:/drivers";
    }

    @PostMapping("/drivers/create")
    public String createDriver(@ModelAttribute("driver") Driver driver,
                               @RequestParam("carrier.name") String carr,
                               Model model) {
        Carrier carrier = carrierRepository.findByName(carr);
        driver.setCarrier(carrier);
        // Обновляем строку таблицы Водителей
        driverService.createDriver(driver);
        return "redirect:/drivers";
    }

//    public DriverService getDriverService() {
//        return driverService;
//    }

    // === ???????
    public String addDriver(Model model) {
        model.addAttribute("driver", new Driver());
        return "driver/form";
    }

    // === ???????
    @PostMapping("/drivers/save")
    public String saveDriver(@ModelAttribute Driver driver) {
        driverService.createDriver(driver);
        return "redirect:/drivers";
    }

    @GetMapping("/drivers/delete/{id}")
    public String deleteDriver(@PathVariable Long id) {
        driverService.deleteDriver(id);
        return "redirect:/drivers";
    }


}
