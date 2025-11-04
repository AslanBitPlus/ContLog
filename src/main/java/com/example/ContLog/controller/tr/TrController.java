package com.example.ContLog.controller.tr;
//
// Контроллер Менеджера Терминала
//
import com.example.ContLog.entity.*;
import com.example.ContLog.repository.*;
import com.example.ContLog.service.ContainerdrsService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.DayOfWeek;
import java.time.temporal.TemporalAdjusters;

@Controller
public class TrController {

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private CarrierRepository carrierRepository;

    @Autowired
    private TerminalRepository terminalRepository;

    @Autowired
    private ContainerRepository containerRepository;

    @Autowired
    private ContainerdrsService containerdrsService;

    // Чтение Атрибутов из Сессии
    @ModelAttribute
    public void addSessionAttributes(Model model, HttpSession session) {
        String userName = (String) session.getAttribute("userName");
        String userEmail = (String) session.getAttribute("userEmail");
        String userCod = (String) session.getAttribute("userCod");
        String terminalFullName = terminalRepository.findById(Long.parseLong(userCod, 10))
                .map(Terminal::getFullName)
                .orElse("Транспортная компания не найдена!");
        String terminalShortName = terminalRepository.findById(Long.parseLong(userCod, 10))
                .map(Terminal::getShortName)
                .orElse("Транспортная компания не найдена!");
        String terminalAddress = terminalRepository.findById(Long.parseLong(userCod, 10))
                .map(Terminal::getAddress)
                .orElse("Транспортная компания не найдена!");

        model.addAttribute("userName", userName);
        model.addAttribute("userEmail", userEmail);
        model.addAttribute("userCod", userCod);
        model.addAttribute("today", LocalDate.now());
        model.addAttribute("terminalShortName", terminalShortName);
        model.addAttribute("terminalName", terminalFullName);
        model.addAttribute("terminalAddress", terminalAddress);
    }

    // Определение начала недели (понедельник) по текущей дате
    public static LocalDate getStartOfWeek(LocalDate date) {
        return date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    }

    // Определение конца недели (воскресенье) по текущей дате
    public static LocalDate getEndOfWeek(LocalDate date) {
        return date.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
    }

    // ============================================================
    // Контейнеры Все
    @GetMapping("/tr_containers/all")
    public String containersAll(Model model, @ModelAttribute("userCod") String userCod,
                                 @ModelAttribute("terminalShortName") String terminalShortName) {

        // КТК
        String ktk = "all";
        model.addAttribute("ktk", ktk);

        model.addAttribute("containers", containerRepository
                .findContainersFromTerminal(Long.parseLong(userCod, 10)));
        return "04_tr_manager/tr_container";
    }

    // Контейнеры Порожние
    @GetMapping("/tr_containers/free")
    public String containersFree(Model model, @ModelAttribute("userCod") String userCod,
                                 @ModelAttribute("terminalShortName") String terminalShortName) {

        // КТК
        String ktk = "free";
        model.addAttribute("ktk", ktk);

        model.addAttribute("containers", containerRepository
                .findContainersFromTerminalFree(Long.parseLong(userCod, 10)));
        return "04_tr_manager/tr_container";
    }

    // Контейнеры Груженые
    @GetMapping("/tr_containers/full")
    public String containersFull(Model model, @ModelAttribute("userCod") String userCod,
                                 @ModelAttribute("terminalShortName") String terminalShortName) {

        // КТК
        String ktk = "full";
        model.addAttribute("ktk", ktk);

        model.addAttribute("containers", containerRepository
                .findContainersFromTerminalFull(Long.parseLong(userCod, 10)));
        return "04_tr_manager/tr_container";
    }

    // Контейнеры Ремонтные
    @GetMapping("/tr_containers/bad")
    public String containersBad(Model model, @ModelAttribute("userCod") String userCod,
                                 @ModelAttribute("terminalShortName") String terminalShortName) {

        // КТК
        String ktk = "bad";
        model.addAttribute("ktk", ktk);

        model.addAttribute("containers", containerRepository
                .findContainersFromTerminalBad(Long.parseLong(userCod, 10)));
        return "04_tr_manager/tr_container";
    }

    // Контейнеры на Продажу
    @GetMapping("/tr_containers/out")
    public String containersOut(Model model, @ModelAttribute("userCod") String userCod,
                                @ModelAttribute("terminalShortName") String terminalShortName) {

        // КТК
        String ktk = "out";
        model.addAttribute("ktk", ktk);

        model.addAttribute("containers", containerRepository
                .findContainersFromTerminalOut(Long.parseLong(userCod, 10)));
        return "04_tr_manager/tr_container";
    }
    // ============================================================


    // Выдача КТК
    @GetMapping("/tr_contdrses_out")
    public String listTrContDrsesOut(Model model, @ModelAttribute("userCod") String userCod,
                                  @ModelAttribute("terminalFullName") String terminalFullName,
                                  @ModelAttribute("terminalShortName") String terminalShortName) {

        // Для вывода в отчет всех заявок
        String reportType = "all";
        model.addAttribute("reportType", reportType);
        // Выдача-Прием
        String inOut = "out";
        model.addAttribute("inOut", inOut);
        // 1
        // Определяем все КТК на Терминале по ContOwner-у из Заявки Sale
        // Отправляем в Model

        // Определяем DRS Заявки Терминала
        model.addAttribute("contdrses",
                containerdrsService.getByFromTerminalDrs(Long.parseLong(userCod, 10)));
        return "04_tr_manager/tr_contdrs";
    }

    // Выдача КТК на Сегодня
    @GetMapping("/tr_contdrses_out/today")
    public String listTrContDrsesOutToday(Model model, @ModelAttribute("userCod") String userCod,
                                     @ModelAttribute("terminalFullName") String terminalFullName,
                                     @ModelAttribute("terminalShortName") String terminalShortName,
                                     @ModelAttribute("today") LocalDate today) {

        // Для вывода в отчет всех заявок
        String reportType = "today";
        model.addAttribute("reportType", reportType);
        // Выдача-Прием
        String inOut = "out";
        model.addAttribute("inOut", inOut);
        // 1
        // Определяем все КТК на Терминале по ContOwner-у из Заявки Sale
        // Отправляем в Model

        // Определяем DRS Заявки Терминала на Сегодня
        model.addAttribute("contdrses",
                containerdrsService.getByFromTerminalDrsToday(Long.parseLong(userCod, 10),
                        today));
        return "04_tr_manager/tr_contdrs";
    }

    // Прием КТК
    @GetMapping("/tr_contdrses_in")
    public String listTrContDrsesIn(Model model, @ModelAttribute("userCod") String userCod,
                                  @ModelAttribute("terminalFullName") String terminalFullName,
                                  @ModelAttribute("terminalShortName") String terminalShortName) {

        // Для вывода в отчет всех заявок
        String reportType = "all";
        model.addAttribute("reportType", reportType);
        // Выдача-Прием
        String inOut = "in";
        model.addAttribute("inOut", inOut);
        // 1
        // Определяем все КТК на Терминале по ContOwner-у из Заявки Sale
        // Отправляем в Model

        // Определяем DRS Заявки Терминала
        model.addAttribute("contdrses",
                containerdrsService.getByToTerminalDrs(Long.parseLong(userCod, 10)));
        return "04_tr_manager/tr_contdrs";
    }

    // Прием КТК на Сегодня
    @GetMapping("/tr_contdrses_in/today")
    public String listTrContDrsesInToday(Model model, @ModelAttribute("userCod") String userCod,
                                    @ModelAttribute("terminalFullName") String terminalFullName,
                                    @ModelAttribute("terminalShortName") String terminalShortName,
                                    @ModelAttribute("today") LocalDate today) {

        // Для вывода в отчет всех заявок
        String reportType = "today";
        model.addAttribute("reportType", reportType);
        // Выдача-Прием
        String inOut = "in";
        model.addAttribute("inOut", inOut);
        // 1
        // Определяем все КТК на Терминале по ContOwner-у из Заявки Sale
        // Отправляем в Model

        // Определяем DRS Заявки Терминала
        model.addAttribute("contdrses",
                containerdrsService.getByToTerminalDrsToday(Long.parseLong(userCod, 10),
                        today));
        return "04_tr_manager/tr_contdrs";
    }

    // ПРИЕМ КТК (Get)
    @GetMapping("/tr_contdrses_in/update/{id}")
    public String updateTrContDrsInPage(@PathVariable Long id, Model model,
                  @ModelAttribute("userCod") String userCod,
                  @ModelAttribute("terminalFullName") String terminalFullName,
                  @ModelAttribute("terminalShortName") String terminalShortName) {
        // Выдача-Прием
        String inOut = "in";
        model.addAttribute("inOut", inOut);

        // Находим DRS для обновления
        Containerdrs containerdrs = containerdrsService.getContainerDrs(id);
//         Определяем КТК для Выдачи по contOwner и Terminal
//         container.w == 0 (порожний)
//        Long terminalId = Long.parseLong(userCod);
//        Long contOwnerId = containerdrs.getSale().getContowner().getId();

//        model.addAttribute("containers", containerRepository.
//                findContainersFromOutByContOwnerAndTerminalAndEmpty(terminalId,
//                        contOwnerId));

        String dateFrom = containerdrs.dateFromToStr();
        String dateTo = containerdrs.dateToToStr();

        model.addAttribute("contdrs", containerdrs);
        model.addAttribute("isUpdate", true);
        model.addAttribute("dateFrom", dateFrom);
        model.addAttribute("dateTo", dateTo);

        return "04_tr_manager/tr_contdrs_crud";
    }

    // ПРИЕМ КТК (Post)
    @PostMapping("/tr_contdrses_in/update/{id}")
    public String updateTrContDrsIn(@ModelAttribute("contdrs") Containerdrs containerdrs,
                                       @PathVariable Long id) {
        // Отмечаем Заявку, как завершенную
        containerdrs.setFinished(true);
        // Сохранение обновления в DRS Заявке
        containerdrsService.updateContainerDrs(containerdrs, id);

        return "redirect:/tr_contdrses_in";
    }

    // ВЫДАЧА КТК (Get)
    @GetMapping("/tr_contdrses_out/update/{id}")
    public String updateTrContDrsOutPage(@PathVariable Long id, Model model,
                                        @ModelAttribute("userCod") String userCod,
                                        @ModelAttribute("terminalFullName") String terminalFullName,
                                        @ModelAttribute("terminalShortName") String terminalShortName) {
        // Выдача-Прием
        String inOut = "out";
        model.addAttribute("inOut", inOut);

        // Находим DRS для обновления
        Containerdrs containerdrs = containerdrsService.getContainerDrs(id);
//         Определяем КТК для Выдачи по contOwner и Terminal
//         container.w == 0 (порожний)
        Long terminalId = Long.parseLong(userCod);
        Long contOwnerId = containerdrs.getSale().getContowner().getId();

        model.addAttribute("containers", containerRepository.
                findContainersFromOutByContOwnerAndTerminalAndEmpty(terminalId,
                        contOwnerId));

        String dateFrom = containerdrs.dateFromToStr();
        String dateTo = containerdrs.dateToToStr();

        model.addAttribute("contdrs", containerdrs);
        model.addAttribute("isUpdate", true);
        model.addAttribute("dateFrom", dateFrom);
        model.addAttribute("dateTo", dateTo);

        return "04_tr_manager/tr_contdrs_crud";
    }

    // ВЫДАЧА КТК (Post)
    @PostMapping("/tr_contdrses_out/update/{id}")
    public String updateTrContDrsOut(@ModelAttribute("contdrs") Containerdrs containerdrs,
                                     @RequestParam("sale.container.id") Long containerId,
                                     @PathVariable Long id) {
        // Определяем текущую заявку
        Sale currentSale = containerdrsService.getContainerDrs(
                containerdrs.getId()).getSale();
        Driver currentDriver = containerdrsService
                .getContainerDrs(containerdrs.getId())
                .getDriver();

        Container currentContainer = new Container();
        Container oldContainer = containerdrsService
                .getContainerDrs(id)
                .getSale()
                .getContainer();

        if (containerId != 0 && currentSale != null) {
            currentContainer = containerRepository.getReferenceById(containerId);
            currentSale.setContainer(currentContainer);
            currentSale.setStarted(true);
            // =============
            containerdrs.setStarted(true);
            // =============
            currentContainer.setPlace("d");
            currentContainer.setPlaceId(currentDriver.getId());
            containerdrs.setDrs("d");
        } else {
            currentContainer = null;
            currentSale.setContainer(currentContainer);
            currentSale.setStarted(false);
            // =============
            containerdrs.setStarted(false);
            // =============
            containerdrs.setDrs("");
        }

        // Отмечаем Drs Заявку и Заявку, как начатые, если водитель
        // получает порожний КТК и заявка не является перемещением
        if (currentContainer != null) {
            if (currentContainer.getW() == 0 && containerdrs.getTypeDrs() != 0) {
                currentContainer.setPlace("d");
                containerdrs.setStarted(true);
                containerdrs.setFinished(false);
                currentSale.setStarted(true);
                currentSale.setFinished(false);
                containerRepository.save(currentContainer);
            }

            // Отмечаем Drs Заявку и Заявку, как завершенные, если водитель
            // сдает груженый КТК и заявка не является перемещением
            if (currentContainer.getW() != 0 && containerdrs.getTypeDrs() != 0) {
                containerdrs.setFinished(true);
                currentSale.setFinished(true);
            }

            // Проверка
//            System.out.println("=============================================================");
//            System.out.println("Новый КТК №:   " + currentContainer.getCode() + " " +
//                    currentContainer.getNumber());
//            System.out.println("Водитель:      " + currentDriver.getSurename() + " " +
//                    currentDriver.getName() + " " +
//                    currentDriver.getPatronymic());
//            System.out.println("Заявка:        " + currentSale.getId() +
//                    " - от " + currentSale.getFromDate());
//            System.out.println("Полная Заявка: " + currentSale.toString());
//            System.out.println("=============================================================");

        } else {
//            System.out.println("=============================================================");
//            System.out.println("Выдача КТК:    null" );
//            System.out.println("Заявка: " + currentSale.getId() +
//                    " - от " + currentSale.getFromDate());
//            System.out.println("Полная Заявка: " + currentSale.toString());
//            System.out.println("=============================================================");
        }

        if (oldContainer != null && oldContainer != currentContainer) {
            oldContainer.setPlace("t");

            oldContainer.setPlaceId(containerdrsService
                    .getContainerDrs(id)
                    .getFromTerminal()
                    .getId());

            containerRepository.save(oldContainer);
//            System.out.println("=============================================================");
//            System.out.println("Новый КТК №:   " + oldContainer.getCode() + " " +
//                    oldContainer.getNumber());
//            System.out.println("=============================================================");
        }

        // Сохранение обновления в DRS Заявке
        currentSale.setContainer(currentContainer);
        containerdrs.setSale(currentSale);

        containerdrsService.updateContainerDrs(containerdrs, id);
        saleRepository.save(currentSale);


        return "redirect:/tr_contdrses_out";
    }

}