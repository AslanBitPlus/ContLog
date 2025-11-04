package com.example.ContLog.controller;

import com.example.ContLog.entity.Contowner;
import com.example.ContLog.service.ContownerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class ContownerController {

    @Autowired
    private ContownerService contownerService;

//    @Autowired
//    private ContainerService containerService;

    @GetMapping("/contowners")
    public String listContowners(Model model) {
        model.addAttribute("contowners", contownerService.getAllContowners());
        return "contowner/list";
    }

    @GetMapping("/contowners/create")
    public String createContownerPage(Model model) {
        Contowner contowner = new Contowner();
        model.addAttribute("contowner", contowner);
        model.addAttribute("isUpdate", false);
        return "contowner/crud"; // ??
    }

    @GetMapping("/contowners/update/{id}")
    public String updateContownerPage(@PathVariable Long id, Model model) {
        Contowner contowner = contownerService.getContowner(id);
        model.addAttribute("contowner", contowner);
        model.addAttribute("isUpdate", true);
        return "contowner/crud"; // ??
    }

    @PostMapping("/contowners/update/{id}")
    public String createContowner(@ModelAttribute("contowner") Contowner contowner, @PathVariable Long id) {
        contownerService.updateContowner(contowner, id);
        return "redirect:/contowners";
    }

    @PostMapping("/contowners/create")
    public String createContowner(@ModelAttribute("contowner") Contowner contowner) {
        contownerService.createContowner(contowner);
        return "redirect:/contowners";
    }

    @GetMapping("/contowners/delete/{id}")
    public String deleteContowner(@PathVariable Long id) {
        contownerService.deleteContowner(id);
        return "redirect:/contowners";
    }
}
