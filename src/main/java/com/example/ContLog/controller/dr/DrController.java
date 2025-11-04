package com.example.ContLog.controller.dr;
//
// Контроллер Водителя
//
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.time.LocalDate;
import java.time.DayOfWeek;
import java.time.temporal.TemporalAdjusters;

@Controller
public class DrController {

    @Autowired
    private CarrierRepository carrierRepository;

    @Autowired
    private TerminalRepository terminalRepository;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private ContainerdrsService containerdrsService;

    // Чтение Атрибутов из Сессии
    @ModelAttribute
    public void addSessionAttributes(Model model, HttpSession session) {
        String userName = (String) session.getAttribute("userName");
        String userEmail = (String) session.getAttribute("userEmail");
        String userCod = (String) session.getAttribute("userCod");

        // Текущий Водитель
        Driver currentDriver = driverRepository.getReferenceById(Long.parseLong(userCod, 10));

        // Определение данных Водителя
        String driverName = driverRepository.findById(Long.parseLong(userCod, 10))
                .map(Driver::getSurename)
                .orElse("Фамилия") + " "
                + driverRepository.findById(Long.parseLong(userCod, 10))
                .map(Driver::getName)
                .orElse("Имя") + " "
                + driverRepository.findById(Long.parseLong(userCod, 10))
                .map(Driver::getPatronymic)
                .orElse("Фамилия");

        // Определяем имя Транспортной организации Водителя
        String carrierName = currentDriver.getCarrier().getName();

        model.addAttribute("userName", userName);
        model.addAttribute("userEmail", userEmail);
        model.addAttribute("userCod", userCod);
        model.addAttribute("today", LocalDate.now());
        model.addAttribute("driverName", driverName);
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

    // Чтение всех DRS Заявок Водителя
    @GetMapping("/dr_contdrses")
    public String listDcContDrses(Model model, @ModelAttribute("userCod") String userCod,
                                  @ModelAttribute("carrierName") String carrierName,
                                  @ModelAttribute("driverName") String driverName) {
        // Для вывода в отчет всех заявок
        String reportType = "all";
        model.addAttribute("reportType", reportType);

        // Определяем Заявки DRS Водителя
        model.addAttribute("contdrses",
                containerdrsService.getByDriverDrsAll(Long.parseLong(userCod, 10)));
        return "05_driver/dr_contdrs";
    }

    // Чтение DRS Заявок Водителя на Сегодня
    @GetMapping("/dr_contdrses/today")
    public String listDcContDrsesToday(Model model, @ModelAttribute("userCod") String userCod,
                                  @ModelAttribute("carrierName") String carrierName,
                                  @ModelAttribute("driverName") String driverName,
                                  @ModelAttribute("today") LocalDate today) {
        // Для вывода в отчет всех заявок
        String reportType = "today";
        model.addAttribute("reportType", reportType);

        // Определяем Заявки DRS Водителя
        model.addAttribute("contdrses",
                containerdrsService.getByDriverDrsToday(Long.parseLong(userCod, 10), today));
        return "05_driver/dr_contdrs";
    }

    // Чтение DRS Заявок Водителя за Неделю
    @GetMapping("/dr_contdrses/week")
    public String listDcContDrsesWeek(Model model, @ModelAttribute("userCod") String userCod,
                                       @ModelAttribute("carrierName") String carrierName,
                                       @ModelAttribute("driverName") String driverName,
                                       @ModelAttribute("today") LocalDate today) {
        // Определение дат начала и конца недели
        LocalDate startOfWeek = getStartOfWeek(today);
        LocalDate endOfWeek = getEndOfWeek(today);
        // Для вывода в отчет всех заявок
        String reportType = "week";
        model.addAttribute("reportType", reportType);

        // Определяем Заявки DRS Водителя
        model.addAttribute("contdrses",
                containerdrsService.getByDriverDrsWeek(Long.parseLong(userCod, 10),
                startOfWeek, endOfWeek));
        return "05_driver/dr_contdrs";
    }

    // Чтение DRS Заявок Водителя за Месяц
    @GetMapping("/dr_contdrses/month")
    public String listDcContDrsesMonth(Model model, @ModelAttribute("userCod") String userCod,
                                      @ModelAttribute("carrierName") String carrierName,
                                      @ModelAttribute("driverName") String driverName) {
        // Для вывода в отчет всех заявок
        String reportType = "month";
        model.addAttribute("reportType", reportType);

        // Определяем Заявки DRS Водителя
        model.addAttribute("contdrses",
                containerdrsService.getByDriverDrsMonth(Long.parseLong(userCod, 10)));
        return "05_driver/dr_contdrs";
    }


    // DR_REPORT ==================================================================
    // Печать всех DRS Заявок Водителя
    @GetMapping("/dr_contdrses/print")
    public ResponseEntity<byte[]> downloadDrContDrsAllReport(Model model,
                                                             @ModelAttribute("driverName") String driverName,
                                                             @ModelAttribute("userCod") String userCod) {
        try {
            byte[] pdfBytes = containerdrsService.generateDrContDrsAllReport(driverName,
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

    // Печать DRS Заявок Водителя на Сегодня
    @GetMapping("/dr_contdrses/today/print")
    public ResponseEntity<byte[]> downloadDrContDrsTodayReport(Model model,
                                                               @ModelAttribute("driverName") String driverName,
                                                               @ModelAttribute("userCod") String userCod,
                                                               @ModelAttribute("today") LocalDate today) {
        try {
            byte[] pdfBytes = containerdrsService.generateDrContDrsTodayReport(driverName,
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

    // Печать DRS Заявок Водителя за Неделю
    @GetMapping("/dr_contdrses/week/print")
    public ResponseEntity<byte[]> generateDrContDrsWeekReport(Model model,
                                                              @ModelAttribute("driverName") String driverName,
                                                              @ModelAttribute("userCod") String userCod,
                                                              @ModelAttribute("today") LocalDate today) {
        // Определение дат начала и конца недели
        LocalDate startOfWeek = getStartOfWeek(today);
        LocalDate endOfWeek = getEndOfWeek(today);
        //
        try {
            byte[] pdfBytes = containerdrsService.generateDrContDrsWeekReport(
                    driverName, Long.parseLong(userCod, 10),
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

    // Печать DRS Заявок Водителя за Месяц
    @GetMapping("/dr_contdrses/month/print")
    public ResponseEntity<byte[]> downloadDrContDrsMonthReport(Model model,
                                                               @ModelAttribute("driverName") String driverName,
                                                               @ModelAttribute("userCod") String userCod,
                                                               @ModelAttribute("today") LocalDate today) {
        //
        try {
            byte[] pdfBytes = containerdrsService.generateDrContDrsMonthReport(
                    driverName,
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

    // DR_REPORT ==================================================================


}
