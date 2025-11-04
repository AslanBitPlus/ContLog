package com.example.ContLog.controller.tc;
//
// Контроллер Менеджера Транспортной компании
//
import com.example.ContLog.entity.Carrier;
import com.example.ContLog.entity.Containerdrs;
import com.example.ContLog.entity.Driver;
import com.example.ContLog.repository.CarrierRepository;
import com.example.ContLog.repository.DriverRepository;
import com.example.ContLog.repository.TerminalRepository;
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
import java.time.DayOfWeek;
import java.time.temporal.TemporalAdjusters;

@Controller
public class TcController {

    @Autowired
    private CarrierRepository carrierRepository;

    @Autowired
    private TerminalRepository terminalRepository;

    @Autowired
    private DriverRepository driverRepository;

//    @Autowired
//    private ContainerdrsRepository containerdrsRepository;

    @Autowired
    private ContainerdrsService containerdrsService;

    // Чтение Атрибутов из Сессии
    @ModelAttribute
    public void addSessionAttributes(Model model, HttpSession session) {
        String userName = (String) session.getAttribute("userName");
        String userEmail = (String) session.getAttribute("userEmail");
        String userCod = (String) session.getAttribute("userCod");
        String carrierName = carrierRepository.findById(Long.parseLong(userCod, 10))
                .map(Carrier::getName)
                .orElse("Транспортная компания не найдена!");

        model.addAttribute("userName", userName);
        model.addAttribute("userEmail", userEmail);
        model.addAttribute("userCod", userCod);
        model.addAttribute("today", LocalDate.now());
        model.addAttribute("carrierName", carrierName);
    }

    // Определение начала недели (понедельник) по текущей дате
    public static LocalDate getStartOfWeek(LocalDate date) {
        return date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    }

    // Определение конца недели (воскресенье) по текущей дате
    public static LocalDate getEndOfWeek(LocalDate date) {
        return date.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
    }

    // Чтение всех DRS Заявок Перевозчика
    @GetMapping("/tc_contdrses")
    public String listTcContDrses(Model model, @ModelAttribute("userCod") String userCod,
                                @ModelAttribute("carrierName") String carrierName) {
        // Для вывода в отчет всех заявок
        String reportType = "all";
        model.addAttribute("reportType", reportType);
        // Определяем Водителей Перевозчика
        model.addAttribute("drivers",
                driverRepository.findByCarrierId(Long.parseLong(userCod, 10)));
        // Определяем Заявки DRS Перевозчика
//        model.addAttribute("contdrses",
//                containerdrsRepository.findByCarrierId(Long.parseLong(userCod, 10)));
        model.addAttribute("contdrses",
                  containerdrsService.getByCarrierDrs(Long.parseLong(userCod, 10)));
        return "07_tc_manager/tc_contdrs";
    }

    // Чтение DRS Заявок Перевозчика на Сегодня
    @GetMapping("/tc_contdrses/today")
    public String listTcContDrsesToday(Model model, @ModelAttribute("userCod") String userCod,
                                  @ModelAttribute("today") LocalDate today) {
        // Для вывода в отчет заявок на Сегодня
        String reportType = "today";
        model.addAttribute("reportType", reportType);
        // Определяем Водителей Перевозчика
        model.addAttribute("drivers",
                driverRepository.findByCarrierId(Long.parseLong(userCod, 10)));
        // Определяем Заявки DRS Перевозчика на Сегодня
        model.addAttribute("contdrses",
        containerdrsService.getByCarrierIdAndSaleDate(Long.parseLong(userCod, 10), today));
        return "07_tc_manager/tc_contdrs";
    }

    // Чтение DRS Заявок Перевозчика за Неделю
    @GetMapping("/tc_contdrses/week")
    public String listTcContDrsesWeek(Model model, @ModelAttribute("userCod") String userCod,
                                       @ModelAttribute("today") LocalDate today) {
        // Определение дат начала и конца недели
        LocalDate startOfWeek = getStartOfWeek(today);
        LocalDate endOfWeek = getEndOfWeek(today);
        // Для вывода в отчет заявок за Месяц
        String reportType = "week";
        model.addAttribute("reportType", reportType);
        // Определяем Водителей Перевозчика
        model.addAttribute("drivers",
                driverRepository.findByCarrierId(Long.parseLong(userCod, 10)));
        // Определяем Заявки DRS Перевозчика за Неделю
        model.addAttribute("contdrses",
                containerdrsService.getByCarrierIdAndCurrentWeek(Long.parseLong(userCod, 10),
                        startOfWeek, endOfWeek));
        return "07_tc_manager/tc_contdrs";
    }

    // Чтение DRS Заявок Перевозчика за Месяц
    @GetMapping("/tc_contdrses/month")
    public String listTcContDrsesMonth(Model model, @ModelAttribute("userCod") String userCod,
                                       @ModelAttribute("today") LocalDate today) {
        // Для вывода в отчет заявок за Месяц
        String reportType = "month";
        model.addAttribute("reportType", reportType);
        // Определяем Водителей Перевозчика
        model.addAttribute("drivers",
                driverRepository.findByCarrierId(Long.parseLong(userCod, 10)));
        // Определяем Заявки DRS Перевозчика за Месяц
        model.addAttribute("contdrses",
                containerdrsService.getByCarrierIdAndCurrentMonth(Long.parseLong(userCod, 10)));
        return "07_tc_manager/tc_contdrs";
    }

    // =========================================================================

    // Обновление заявки (Get)
    @GetMapping("/tc_contdrses/update/{id}")
    public String updateCarrierContDrsPage(@PathVariable Long id, Model model,
                                           @ModelAttribute("userCod") String userCod) {
        // Определяем Водителей Перевозчика
        model.addAttribute("drivers",
                driverRepository.findByCarrierId(Long.parseLong(userCod, 10)));
        // Находим DRS для обновления
        Containerdrs containerdrs = containerdrsService.getContainerDrs(id);
        // Определяем Терминалы для КТК
        model.addAttribute("fTerminals", terminalRepository.findAll());
        // Определяем Терминалы для получения КТК
        model.addAttribute("tTerminals", terminalRepository.findAll());

        String dateFrom = containerdrs.dateFromToStr();
        String dateTo = containerdrs.dateToToStr();

        model.addAttribute("contdrs", containerdrs);
        model.addAttribute("isUpdate", true);
        model.addAttribute("dateFrom", dateFrom);
        model.addAttribute("dateTo", dateTo);

        return "07_tc_manager/tc_contdrs_crud";
    }

    // Обновление заявки (Post)
    @PostMapping("/tc_contdrses/update/{id}")
    public String updateCarrierContDrs(@ModelAttribute("contdrs") Containerdrs containerdrs,
                                       @PathVariable Long id) {
        // containerdrsRepository.save(containerdrs);
        containerdrsService.updateContainerDrs(containerdrs, id);

        return "redirect:/tc_contdrses";
    }

    // =========================================================================

    // Чтение всех КТК ContOwner-а
    @GetMapping("/tc_drivers")
    public String listTcDrivers(Model model, @ModelAttribute("userCod") String userCod,
                                @ModelAttribute("carrierName") String carrierName) {
        // Определяем КТК ContOwner-а
        model.addAttribute("drivers",
                driverRepository.findByCarrierId(Long.parseLong(userCod, 10)));
        return "07_tc_manager/tc_driver"; // ??
    }

    // Новый водитель для Транспортной компании
    @GetMapping("/tc_drivers/create")
    public String createTcDriverPage(Model model, @ModelAttribute("userCod") String userCod,
                                     @ModelAttribute("carrierName") String carrierName) {
        Driver driver = new Driver();
        //
        model.addAttribute("driver", driver);
        model.addAttribute("isUpdate", false);
        return "07_tc_manager/tc_driver_crud"; // ??
    }

    @PostMapping("/tc_drivers/create")
    public String createTcDriver(@ModelAttribute("driver") Driver driver,
                                 @ModelAttribute("userCod") String userCod,
                                 @ModelAttribute("carrierName") String carrierName, Model model) {

        Carrier carrier = carrierRepository.findByName(carrierName);
        driver.setCarrier(carrier);
        System.out.println("==========================================");
        System.out.println(carrier.getName());
        System.out.println("==========================================");
        // Обновляем строку таблицы Водителей
        driverRepository.save(driver);
        return "redirect:/tc_drivers";
    }

    @GetMapping("/tc_drivers/update/{id}")
    public String updateTcDriverPage(@PathVariable Long id,
                                     @ModelAttribute("carrierName") String carrierName, Model model) {
        Driver driver = driverRepository.getReferenceById(id);
        //
        model.addAttribute("driver", driver);
        model.addAttribute("isUpdate", true);
        //
        return "07_tc_manager/tc_driver_crud";
    }

    @PostMapping("/tc_drivers/update/{id}")
    public String createDriver(@ModelAttribute("driver") Driver driver,
                               @ModelAttribute("carrierName") String carrierName,
                               @PathVariable Long id,
                               Model model) {
        // Поиск Перевозчика по имени
        Carrier carrier = carrierRepository.findByName(carrierName);
        driver.setCarrier(carrier);
        // Обновляем строку таблицы Водителей
        driverRepository.save(driver);
        return "redirect:/tc_drivers";
    }

    @GetMapping("/tc_drivers/delete/{id}")
    public String deleteTcDriver(@PathVariable Long id) {
        driverRepository.deleteById(id);
        return "redirect:/tc_drivers";
    }

    // Страница Монитор
    @GetMapping("/tc_monitor")
    public String tcMonitorPage(Model model, @ModelAttribute("userCod") String userCod,
                                @ModelAttribute("carrierName") String carrierName) {

        return "07_tc_manager/tc_monitor";
    }

    // Страница Сервис
    @GetMapping("/tc_service")
    public String tcServicePage(Model model, @ModelAttribute("userCod") String userCod,
                                @ModelAttribute("carrierName") String carrierName) {

        return "07_tc_manager/tc_service";
    }

    // TC_REPORT ==================================================================
    // Печать всех DRS Заявок Перевозчика
    @GetMapping("/tc_contdrses/print")
    public ResponseEntity<byte[]> downloadTcContDrsAllReport(Model model,
                                  @ModelAttribute("carrierName") String carrierName,
                                  @ModelAttribute("userCod") String userCod) {
        try {
            byte[] pdfBytes = containerdrsService.generateTcContDrsAllReport(carrierName,
                    Long.parseLong(userCod, 10));

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=tc_contDrsAllReport.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfBytes);

        } catch (Exception e) {
            return ResponseEntity
                    .internalServerError()
                    .body(("Ошибка генерации отчета: " + e.getMessage()).getBytes());
        }
    }

    // Печать DRS Заявок Перевозчика на Сегодня
    @GetMapping("/tc_contdrses/today/print")
    public ResponseEntity<byte[]> downloadTcContDrsTodayReport(Model model,
                                  @ModelAttribute("carrierName") String carrierName,
                                  @ModelAttribute("userCod") String userCod,
                                  @ModelAttribute("today") LocalDate today) {
        try {
            byte[] pdfBytes = containerdrsService.generateTcContDrsTodayReport(carrierName,
                    Long.parseLong(userCod, 10), today);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=tc_contDrsAllReport.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfBytes);

        } catch (Exception e) {
            return ResponseEntity
                    .internalServerError()
                    .body(("Ошибка генерации отчета: " + e.getMessage()).getBytes());
        }
    }

    // Печать DRS Заявок Перевозчика на Неделю
    @GetMapping("/tc_contdrses/week/print")
    public ResponseEntity<byte[]> downloadTrContDrsWeekReport(Model model,
                                                              @ModelAttribute("carrierName") String carrierName,
                                                              @ModelAttribute("userCod") String userCod,
                                                              @ModelAttribute("today") LocalDate today) {
        // Определение дат начала и конца недели
        LocalDate startOfWeek = getStartOfWeek(today);
        LocalDate endOfWeek = getEndOfWeek(today);
        //
        try {
            byte[] pdfBytes = containerdrsService.generateTcContDrsWeekReport(
                    carrierName, Long.parseLong(userCod, 10),
                    startOfWeek, endOfWeek);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=tc_contDrsAllReport.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfBytes);

        } catch (Exception e) {
            return ResponseEntity
                    .internalServerError()
                    .body(("Ошибка генерации отчета: " + e.getMessage()).getBytes());
        }
    }

    // Печать DRS Заявок Перевозчика за Месяц
    @GetMapping("/tc_contdrses/month/print")
    public ResponseEntity<byte[]> downloadTcContDrsMonthReport(Model model,
                                                             @ModelAttribute("carrierName") String carrierName,
                                                             @ModelAttribute("userCod") String userCod,
                                                             @ModelAttribute("today") LocalDate today) {
        //
        try {
            byte[] pdfBytes = containerdrsService.generateTcContDrsMonthReport(
                    carrierName,
                    Long.parseLong(userCod, 10));
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=tc_contDrsAllReport.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfBytes);

        } catch (Exception e) {
            return ResponseEntity
                    .internalServerError()
                    .body(("Ошибка генерации отчета: " + e.getMessage()).getBytes());
        }
    }


    // TC_REPORT ==================================================================

}
