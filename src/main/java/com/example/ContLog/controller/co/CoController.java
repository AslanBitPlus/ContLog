package com.example.ContLog.controller.co;
//
// Контроллер Менеджера Владельца КТК
//
import com.example.ContLog.entity.*;
import com.example.ContLog.repository.ContownerRepository;
import com.example.ContLog.service.ContainerService;
import com.example.ContLog.service.ContainerdrsService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
public class CoController {

    @Autowired
    private ContownerRepository contownerRepository;

    @Autowired
    private ContainerService containerService;

    @Autowired
    private ContainerdrsService containerdrsService;

    // Чтение Атрибутов из Сессии
    @ModelAttribute
    public void addSessionAttributes(Model model, HttpSession session) {
        String userName = (String) session.getAttribute("userName");
        String userEmail = (String) session.getAttribute("userEmail");
        String userCod = (String) session.getAttribute("userCod");
        String contOwnerName = contownerRepository.findById(Long.parseLong(userCod, 10))
                .map(Contowner::getName)
                .orElse("Владелец КТК не найден!");

        model.addAttribute("userName", userName);
        model.addAttribute("userEmail", userEmail);
        model.addAttribute("userCod", userCod);
        model.addAttribute("today", LocalDate.now());
        model.addAttribute("contOwnerName", contOwnerName);
    }

    // Чтение всех КТК ContOwner-а
    @GetMapping("/co_containers")
    public String listCoContainers(Model model, @ModelAttribute("userCod") String userCod,
                                   @ModelAttribute("contOwnerName") String contOwnerName) {
        // Определяем КТК ContOwner-а и ...
        model.addAttribute("containers", containerService.getByContownerId(Long.parseLong(userCod, 10)));

        return "03_co_manager/co_container"; // ??
    }

    // Новый КТК ContOwner-а (Get)
    @GetMapping("/co_containers/create")
    public String createCoContainerPage(Model model) {
        // Создаем новый Container
        Container container = new Container();

        // Определяем Все Терминалы
        model.addAttribute("terminals", containerService.getAllTerminals());
        model.addAttribute("container", container);
        model.addAttribute("isUpdate", false);

        return "03_co_manager/co_container_crud"; // ??
    }

    // Новый КТК ContOwner-а (Post)
    @PostMapping("/co_containers/create")
    public String createCoContainer(@ModelAttribute("container") Container container,
                                    @ModelAttribute("userCod") String userCod,
                                    Model model) {
        Contowner contowner = contownerRepository.getReferenceById(Long.parseLong(userCod, 10));
        container.setContowner(contowner);
        // Добавляем Новый КТК в таблицу Контейнеров
        containerService.createContainer(container);

        return "redirect:/co_containers";
    }

    @GetMapping("/co_containers/update/{id}")
    public String updateCoContainerPage(@PathVariable Long id,
                                        Model model) {
        //
        Container container = containerService.getContainer(id);
        // Определяем Все Терминалы
        model.addAttribute("terminals", containerService.getAllTerminals());

        model.addAttribute("container", container);
        model.addAttribute("isUpdate", true);
        //
        return "03_co_manager/co_container_crud";
    }

    @PostMapping("/co_containers/update/{id}")
    public String updateCoContainer(@ModelAttribute("container") Container container,
                                    @ModelAttribute("userCod") String userCod,
                               @PathVariable Long id,
                               Model model) {
        // Поиск Владельца КТК по Id по имени
        Contowner contowner = contownerRepository.getReferenceById(Long.parseLong(userCod, 10));
        container.setContowner(contowner);
        // Обновляем строку таблицы Контейнеров
        containerService.updateContainer(container, id);
        return "redirect:/co_containers";
    }


    // Чтение всех КТК ContOwner-а
    @GetMapping("/co_containers/terminal")
    public String listCoContainersFromTerminal(Model model,
                                               @ModelAttribute("userCod") String userCod) {
        // Определяем имя ContOwner-а
        String contOwnerName = contownerRepository.findById(Long.parseLong(userCod, 10))
                .map(Contowner::getName)
                .orElse("Владелец не найден");
        model.addAttribute("contOwnerName", contOwnerName);
        // Определяем КТК ContOwner-а и на терминалах
        model.addAttribute("containers", containerService.getContainersByOwnerAndFromTerminals(contOwnerName));

        return "03_co_manager/co_container_t"; // ??
    }

    // Чтение всех КТК ContOwner-а
    @GetMapping("/co_containers/place")
    public String listCoContainersFromPlace(Model model,
                                           @ModelAttribute("userCod") String userCod,
                                           @ModelAttribute("contOwnerName") String contOwnerName) {

        // Определяем КТК ContOwner-а и на автомобилях
        model.addAttribute("containers", containerService.getContOwnerContainersWithPlaceNames(contOwnerName));

        return "03_co_manager/co_container_place"; // ??
    }

    // Чтение всех КТК ContOwner-а
    @GetMapping("/co_containers/avto")
    public String listCoContainersFromAvto(Model model,
                                               @ModelAttribute("userCod") String userCod) {
        // Определяем имя ContOwner-а
        String contOwnerName = contownerRepository.findById(Long.parseLong(userCod, 10))
                .map(Contowner::getName)
                .orElse("Владелец не найден");
        model.addAttribute("contOwnerName", contOwnerName);
        // Определяем КТК ContOwner-а и на терминалах
        model.addAttribute("containers", containerService.getContainersByOwnerAndFromAvto(contOwnerName));

        return "03_co_manager/co_container_a"; // ??
    }


    // Отчет по КТК Владельца по терминалам
    @GetMapping("/co_containers/terminal/print")
    public ResponseEntity<byte[]> downloadBContainerTerminalReport(Model model,
                  @ModelAttribute("contOwnerName") String contOwnerName) {
        try {
            byte[] pdfBytes = containerService.generateContainerTerminalReport(contOwnerName);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=containers_report.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfBytes);

        } catch (Exception e) {
            return ResponseEntity
                    .internalServerError()
                    .body(("Ошибка генерации отчета: " + e.getMessage()).getBytes());
        }
    }

    // Отчет по КТК Владельца по местам нахождения
    @GetMapping("/co_containers/place/print")
    public ResponseEntity<byte[]> downloadContainerPlaceReport(Model model,
                                                               @ModelAttribute("userCod") String userCod,
                                                               @ModelAttribute("contOwnerName") String contOwnerName) {
        try {
            byte[] pdfBytes = containerService.generateContainerPlaceReport(contOwnerName);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=containers_report.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfBytes);

        } catch (Exception e) {
            return ResponseEntity
                    .internalServerError()
                    .body(("Ошибка генерации отчета: " + e.getMessage()).getBytes());
        }
    }

    // Отчет по КТК Владельца по терминалам
    @GetMapping("/co_containers/avto/print")
    public ResponseEntity<byte[]> downloadBContainerAvtoReport(Model model,
                                                                   @ModelAttribute("contOwnerName") String contOwnerName) {
        try {
            byte[] pdfBytes = containerService.generateContainerAvtoReport(contOwnerName);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=containers_report.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfBytes);

        } catch (Exception e) {
            return ResponseEntity
                    .internalServerError()
                    .body(("Ошибка генерации отчета: " + e.getMessage()).getBytes());
        }
    }

    //
    // Чтение всех DRS Заявок ContOwner-а
    @GetMapping("/co_contdrses")
    public String listContDrsByContOwnerName(Model model, @ModelAttribute("userCod") String userCod,
                                   @ModelAttribute("contOwnerName") String contOwnerName) {
        // Определяем все DRS Заявки ContOwner-а
        model.addAttribute("contdrses", containerdrsService.getContDrsByContOwnerName(contOwnerName));

        return "03_co_manager/co_contdrs";
    }

    // Чтение всех DRS Заявок ContOwner-а на Сегодня
    @GetMapping("/co_contdrses/today")
    public String listContDrsByContOwnerNameAndSaleDate(Model model, @ModelAttribute("userCod") String userCod,
                                       @ModelAttribute("contOwnerName") String contOwnerName,
                                       @ModelAttribute("today") LocalDate today) {
        // Определяем DRS Заявки ContOwner-а на Сегодня
        model.addAttribute("contdrses", containerdrsService.getContDrsByContOwnerNameAndSaleDate(contOwnerName,
                today));

        return "03_co_manager/co_contdrs_today";
    }

    // Чтение DRS Заявок ContOwner-а С.. По..
    @GetMapping("/co_contdrses/fromto")
    public String listContDrsByContOwnerNameAndSaleDateFromTo(Model model, @ModelAttribute("userCod") String userCod,
                                                        @ModelAttribute("contOwnerName") String contOwnerName) {
        // Определяем DRS Заявки ContOwner-а С.. По..
        LocalDate fromD = LocalDate.now();
        LocalDate toD = LocalDate.now();
        model.addAttribute("contdrses", containerdrsService
                .getContDrsByContOwnerNameAndSaleDateFromTo(contOwnerName,
                fromD, toD));

        return "03_co_manager/co_contdrs_fromto";
    }


}
