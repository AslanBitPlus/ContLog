package com.example.ContLog.controller;

import com.example.ContLog.entity.Container;
import com.example.ContLog.entity.Contowner;
import com.example.ContLog.repository.ContownerRepository;
import com.example.ContLog.service.ContainerService;
import com.example.ContLog.service.ContownerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@Controller
public class ContainerController {

    @Autowired
    private ContainerService containerService;

    @Autowired
    private ContownerService contownerService;

    @Autowired
    private ContownerRepository contownerRepository;

    @GetMapping("/containers")
    public String listContainers(Model model) {
        model.addAttribute("containers", containerService.getAllContainers());
        return "container/list";
    }

    @GetMapping("/containers/create")
    public String createContainerPage(Model model) {
        Container container = new Container();
        Contowner contowner = new Contowner();
        model.addAttribute("container", container);
        model.addAttribute("isUpdate", false);
//        model.addAttribute("contowners", contowner);
        model.addAttribute("contowners", contownerService.getAllContowners());
        return "container/crud"; // ??
    }

    @GetMapping("/containers/update/{id}")
    public String updateContainerPage(@PathVariable Long id, Model model) {
        Container container = containerService.getContainer(id);
        // Определяем имя Владельца КТК
        // Для отображения в поле по умолчанию
        String cowner = container.getContowner().getName();

        model.addAttribute("container", container);
        model.addAttribute("isUpdate", true);
        // Отправляем на crud.http имя Владельца КТК
        // Для отображения в поле по умолчанию
        model.addAttribute("cowner", cowner);

//        model.addAttribute("contowners", Collections.singletonList(contownerRepository.findAll()));
        model.addAttribute("contowners", contownerService.getAllContowners());
        return "container/crud"; // ??
    }

    @PostMapping("/containers/update/{id}")
    public String createContainer(@ModelAttribute("container") Container container,
                                  @RequestParam("contowner.name") String cowner,
                                  @PathVariable Long id,
                                  Model model) {
        // Находим Владельца по имени полученному из crud.html cowner
        // и записываем его как Владельца КТК в поле contowner
        Contowner contowner = contownerRepository.findByName(cowner);
        container.setContowner(contowner);
        // Обновляем строку таблицы Контейнеров
        containerService.updateContainer(container, id);
        return "redirect:/containers";
    }

    @PostMapping("/containers/create")
    public String createContainer(@ModelAttribute("container") Container container,
                                  @RequestParam("contowner.name") String cowner,
                                  Model model) {


//        if (contownerRepository.findByName(cowner) == null) {
//            model.addAttribute("errorMessage", "Нет такого Владельца КТК (Стока)");
//            return "/containers/create";
//        }

        Contowner contowner = contownerRepository.findByName(cowner);
        container.setContowner(contowner);
        // Обновляем строку таблицы Контейнеров
        containerService.createContainer(container);
        return "redirect:/containers";
    }

    // === ???????
//    public ContainerService getContainerService() {
//        return containerService;
//    }

    // === ???????
    public String addContainer(Model model) {
        model.addAttribute("container", new Container());
        return "container/form";
    }

    // === ???????
    @PostMapping("/containers/save")
    public String saveContainer(@ModelAttribute Container container) {
        containerService.createContainer(container);
        return "redirect:/containers";
    }

    @GetMapping("/containers/delete/{id}")
    public String deleteContainer(@PathVariable Long id) {
        containerService.deleteContainer(id);
        return "redirect:/containers";
    }
}
