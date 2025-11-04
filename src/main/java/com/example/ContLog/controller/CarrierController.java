package com.example.ContLog.controller;

import com.example.ContLog.entity.Carrier;
import com.example.ContLog.service.CarrierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CarrierController {

    @Autowired
    private CarrierService carrierService;

    @GetMapping("/carriers")
    public String listCarriers(Model model) {
        model.addAttribute("carriers", carrierService.getAllCarriers());
        return "carrier/list";
    }

    @GetMapping("/carriers/create")
    public String createCarrierPage(Model model) {
        Carrier carrier = new Carrier();
        model.addAttribute("carrier", carrier);
        model.addAttribute("isUpdate", false);
        return "carrier/crud"; // ??
    }

    @GetMapping("/carriers/update/{id}")
    public String updateCarrierPage(@PathVariable Long id, Model model) {
        Carrier carrier = carrierService.getCarrier(id);
        model.addAttribute("carrier", carrier);
        model.addAttribute("isUpdate", true);
        return "carrier/crud"; // ??
    }

    @PostMapping("/carriers/update/{id}")
    public String createCarrier(@ModelAttribute("carrier") Carrier carrier,
                                @PathVariable Long id) {
        carrierService.updateCarrier(carrier, id);
        return "redirect:/carriers";
    }

    @PostMapping("/carriers/create")
    public String createCarrier(@ModelAttribute("carrier") Carrier carrier) {
        carrierService.createCarrier(carrier);
        return "redirect:/carriers";
    }

    @GetMapping("/carriers/delete/{id}")
    public String deleteCarrier(@PathVariable Long id) {
        carrierService.deleteCarrier(id);
        return "redirect:/carriers";
    }
}
