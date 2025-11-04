package com.example.ContLog.service;

import com.example.ContLog.entity.Container;
import com.example.ContLog.entity.Containerdrs;
import com.example.ContLog.entity.Sale;
import com.example.ContLog.repository.ContainerdrsRepository;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ContainerdrsService {

    @Autowired
    private ContainerdrsRepository containerdrsRepository;

    // Поиск всех DRS-Заявок
    public List<Containerdrs> getAllContainerDrs() {
        return containerdrsRepository.findAll();
    }

    // Поиск DRS-Заявки по Id
//    public Containerdrs getContainerDrs(Long id) {
//        return containerdrsRepository.findById(id).orElse(null);
//    }
    public Containerdrs getContainerDrs(Long id) {
        return containerdrsRepository.getReferenceById(id);
    }

    // Для TerminalManager (TC) ================================================
    // Для TerminalManager (TC) ================================================

    // Поиск DRS заявки по ID Терминала Выдачи Все
    public List<Containerdrs> getByFromTerminalDrs(Long terminalId) {
        return containerdrsRepository.findByFromTerminal_Id(terminalId);
    }

    // Поиск DRS заявки по ID Терминала Выдачи на Сегодня
    public List<Containerdrs> getByFromTerminalDrsToday(Long terminalId,
                              LocalDate today) {
        return containerdrsRepository.findByFromTerminalIdToday(terminalId,
                today);
    }

    // Поиск DRS заявки по ID Терминала Сдачи Все
    public List<Containerdrs> getByToTerminalDrs(Long terminalId) {
        return containerdrsRepository.findByToTerminal_Id(terminalId);
    }

    // Поиск DRS заявки по ID Терминала Сдачи Сегодня
    public List<Containerdrs> getByToTerminalDrsToday(Long terminalId,
                              LocalDate today) {
        return containerdrsRepository.findByToTerminalIdToday(terminalId,
                today);
    }

    // Поиск DRS-Заявок Terminala за Сегодня
    public List<Containerdrs> getByTerminalIdAndSaleDate(Long terminalId, LocalDate fromDate) {
        return containerdrsRepository.findByTerminalIdAndSaleDate(terminalId, fromDate);
    }

    // Для TerminalManager (TC) ================================================
    // Для TerminalManager (TC) ================================================
    // Поиск DRS-Заявок Перевозчика



    // Для Carrier (TR) ========================================================
    // Для Carrier (TR) ========================================================




    // Для Carrier (TR) ========================================================
    // Для Carrier (TR) ========================================================
    // Поиск DRS-Заявок Перевозчика
    public List<Containerdrs> getByCarrierDrs(Long carrierId) {
        return containerdrsRepository.findByCarrier_Id(carrierId);
    }

    // Поиск DRS-Заявок Перевозчика за  Сегодня
    public List<Containerdrs> getByCarrierIdAndSaleDate(Long carrierId, LocalDate fromDate) {
        return containerdrsRepository.findByCarrierIdAndSaleDate(carrierId, fromDate);
    }

    // Поиск DRS-Заявок Перевозчика за  Сегодня
    // Этот метод тоже работает
    // =========================================
    public List<Containerdrs> getByCarrier_IdAndSale_FromDate(Long carrierId, LocalDate fromDate) {
        return containerdrsRepository.findByCarrier_IdAndSale_FromDate(carrierId, fromDate);
    }
    // =========================================

    // Поиск DRS-Заявок Перевозчика за текущую Неделю
    public List<Containerdrs> getByCarrierIdAndCurrentWeek(Long carrierId,
                              LocalDate startOfWeek,
                              LocalDate endOfWeek) {
        return containerdrsRepository.findByCarrierIdAndCurrentWeek(carrierId,
                startOfWeek, endOfWeek);
    }

    // Поиск DRS-Заявок Перевозчика за текущий месяц
    public List<Containerdrs> getByCarrierIdAndCurrentMonth(Long carrierId) {
        return containerdrsRepository.findByCarrierIdAndCurrentMonth(carrierId);
    }

    // =========================================================================
    // CARRIER REPORTS =========================================================
    // Печать всех DRS Заявок Перевозчика
    public byte[] generateTcContDrsAllReport(String carrierName,
                                           Long carrierId) throws JRException {
        // Загружаем JRXML (НЕ .jasper)
        InputStream reportStream = getClass().getResourceAsStream("/reports/tc_contDrsAllReport.jrxml");
        //
        if (reportStream == null) {
            throw new IllegalStateException("Файл отчета не найден: tc_contDrsAllReport.jrxml");
        }
        // Компиляция JRXML в JasperReport
        JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

        // Получаем все DRS Заявки Владельца
        List<Containerdrs> contDrs = getByCarrierDrs(carrierId);
        // Определяем кол-во КТК в списке
        int contDrsCount = contDrs.size();
        String PHeaderLeft = "Перевозчик: " +
                carrierName + " - [" +
                Integer.toString(contDrsCount) + "]";
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(contDrs);
        // Параметры (если нужно — можно прокидывать в отчет)
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "Система");
        parameters.put("Title1", "Заявки Перевозчика");
        parameters.put("Title2", "(все заявки)");
        parameters.put("PHeaderLeft", PHeaderLeft);
//        parameters.put("contDrsCount", contDrsCount);
        // Заполнение отчета
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        jasperPrint.setProperty(
                "net.sf.jasperreports.export.pdf.metadata.title",
                "Все Заявки"
        );
        // Экспорт в PDF (в виде массива байт, можно вернуть из контроллера)
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    // Печать DRS Заявок Перевозчика на Сегодня
    public byte[] generateTcContDrsTodayReport(String carrierName,
                                           Long carrierId, LocalDate today) throws JRException {
        // Загружаем JRXML (НЕ .jasper)
        InputStream reportStream = getClass().getResourceAsStream("/reports/tc_contDrsAllReport.jrxml");
        //
        if (reportStream == null) {
            throw new IllegalStateException("Файл отчета не найден: tc_contDrsAllReport.jrxml");
        }
        // Компиляция JRXML в JasperReport
        JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

        // Получаем все DRS Заявки Владельца на Сегодня
        List<Containerdrs> contDrs = getByCarrierIdAndSaleDate(carrierId, today);
        // Определяем кол-во КТК в списке
        int contDrsCount = contDrs.size();
        String PHeaderLeft = "Перевозчик: " +
                carrierName + " - [" +
                Integer.toString(contDrsCount) + "]";
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(contDrs);
        // Параметры (если нужно — можно прокидывать в отчет)
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "Система");
        parameters.put("Title1", "Заявки Перевозчика");
        parameters.put("Title2", "(заявки на Сегодня)");
        parameters.put("PHeaderLeft", PHeaderLeft);
//        parameters.put("contDrsCount", contDrsCount);
        // Заполнение отчета
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        jasperPrint.setProperty(
                "net.sf.jasperreports.export.pdf.metadata.title",
                "Заявки на Сегодня"
        );
        // Экспорт в PDF (в виде массива байт, можно вернуть из контроллера)
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    // Печать DRS Заявок Перевозчика за Неделю
    public byte[] generateTcContDrsWeekReport(String carrierName,
                                             Long carrierId,
                                            LocalDate startOfWeek,
                                            LocalDate endOfWeek) throws JRException {
        // Загружаем JRXML (НЕ .jasper)
        InputStream reportStream = getClass().getResourceAsStream("/reports/tc_contDrsAllReport.jrxml");
        //
        if (reportStream == null) {
            throw new IllegalStateException("Файл отчета не найден: tc_contDrsAllReport.jrxml");
        }
        // Компиляция JRXML в JasperReport
        JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

        // Получаем все DRS Заявки Владельца на Сегодня
        List<Containerdrs> contDrs = getByCarrierIdAndCurrentWeek(carrierId,
                startOfWeek, endOfWeek);
        // Определяем кол-во КТК в списке
        int contDrsCount = contDrs.size();
        String PHeaderLeft = "Перевозчик: " +
                carrierName + " - [" +
                Integer.toString(contDrsCount) + "]";
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(contDrs);
        // Параметры (если нужно — можно прокидывать в отчет)
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "Система");
        parameters.put("Title1", "Заявки Перевозчика");
        parameters.put("Title2", "(заявки за Неделю)");
        parameters.put("PHeaderLeft", PHeaderLeft);
//        parameters.put("contDrsCount", contDrsCount);
        // Заполнение отчета
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        jasperPrint.setProperty(
                "net.sf.jasperreports.export.pdf.metadata.title",
                "Заявки за Неделю"
        );
        // Экспорт в PDF (в виде массива байт, можно вернуть из контроллера)
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    // Печать DRS Заявок Перевозчика за Месяц
    public byte[] generateTcContDrsMonthReport(String carrierName,
                                            Long carrierId) throws JRException {
        // Загружаем JRXML (НЕ .jasper)
        InputStream reportStream = getClass().getResourceAsStream("/reports/tc_contDrsAllReport.jrxml");
        //
        if (reportStream == null) {
            throw new IllegalStateException("Файл отчета не найден: tc_contDrsAllReport.jrxml");
        }
        // Компиляция JRXML в JasperReport
        JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

        // Получаем все DRS Заявки Владельца на Сегодня
        List<Containerdrs> contDrs = getByCarrierIdAndCurrentMonth(carrierId);
        // Определяем кол-во КТК в списке
        int contDrsCount = contDrs.size();
        String PHeaderLeft = "Перевозчик: " +
                carrierName + " - [" +
                Integer.toString(contDrsCount) + "]";
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(contDrs);
        // Параметры (если нужно — можно прокидывать в отчет)
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "Система");
        parameters.put("Title1", "Заявки Перевозчика");
        parameters.put("Title2", "(заявки за Месяц)");
        parameters.put("PHeaderLeft", PHeaderLeft);
//        parameters.put("contDrsCount", contDrsCount);
        // Заполнение отчета
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        jasperPrint.setProperty(
                "net.sf.jasperreports.export.pdf.metadata.title",
                "Заявки за Месяц"
        );
        // Экспорт в PDF (в виде массива байт, можно вернуть из контроллера)
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }
    // CARRIER REPORTS =========================================================
    // =========================================================================

    // Для Carrier (TR) ========================================================
    // Для Carrier (TR) ========================================================


    // Сохранение DRS-Заявки
    public Containerdrs createContainerDrs(Containerdrs containerdrs) {
        return containerdrsRepository.save(containerdrs);
    }

    // Обновление данных DRS-Заявки в БД (Для роли SI)
    public void updateContainerdrsSo(Containerdrs containerdrs, Long id) {
        Containerdrs cntdrs = containerdrsRepository.getReferenceById(id);
        cntdrs.setDateFrom(containerdrs.getDateFrom());
        cntdrs.setDateTo(containerdrs.getDateTo());
        cntdrs.setCarrier(containerdrs.getCarrier());
//        cntdrs.setDateReg(containerdrs.getDateReg());
        cntdrs.setIo(containerdrs.getIo());
        cntdrs.setW(containerdrs.getW());
        cntdrs.setWeightT(containerdrs.getWeightT());
        cntdrs.setWeightN(containerdrs.getWeightN());
        cntdrs.setWeightB(containerdrs.getWeightB());
        cntdrs.setTypeDrs(containerdrs.getTypeDrs());
        cntdrs.setDrs(containerdrs.getDrs());
//        cntdrs.setRelis(containerdrs.getRelis());
//        cntdrs.setSale(containerdrs.getSale());
//        cntdrs.setDriver(containerdrs.getDriver());
//        cntdrs.setContainer(containerdrs.getContainer());
//        cntdrs.setFromTerminal(containerdrs.getFromTerminal());
//        cntdrs.setToTerminal(containerdrs.getToTerminal());

        containerdrsRepository.save(cntdrs);
    }

    // Обновление данных DRS-Заявки в БД (Для всех ролей)
    public void updateContainerDrs(Containerdrs containerdrs, Long id) {
        // Поиск обновляемой записи, хранимая в таблице
        Containerdrs contdrs = containerdrsRepository.getReferenceById(id);
        // Обновление всех полей не равных (null)
//        if (containerdrs.getDateReg() != null) contdrs.setDateReg(containerdrs.getDateReg());
        if (containerdrs.getDateFrom() != null) contdrs.setDateFrom(containerdrs.getDateFrom());
        if (containerdrs.getDateTo() != null) contdrs.setDateTo(containerdrs.getDateTo());
        if (containerdrs.getIo() != 0) contdrs.setIo(containerdrs.getIo());
        if (containerdrs.getW() != 0)  contdrs.setW(containerdrs.getW());
        if (containerdrs.getWeightT() != 0) contdrs.setWeightT(containerdrs.getWeightT());
        if (containerdrs.getWeightN() != 0) contdrs.setWeightN(containerdrs.getWeightN());
        if (containerdrs.getWeightB() != 0) contdrs.setWeightB(containerdrs.getWeightB());
        if (containerdrs.getTypeDrs() != 0) contdrs.setTypeDrs(containerdrs.getTypeDrs());
        if (containerdrs.getDrs() != null) contdrs.setDrs(containerdrs.getDrs());
        if (containerdrs.getRelis() != null) contdrs.setRelis(containerdrs.getRelis());
                else contdrs.setRelis(null);
        // Обновление полей связанных с другими таблицами
        if (containerdrs.getSale() != null) contdrs.setSale(containerdrs.getSale());
        if (containerdrs.getCarrier() != null ) contdrs.setCarrier(containerdrs.getCarrier());
        if (containerdrs.getDriver() != null) contdrs.setDriver(containerdrs.getDriver());

        if (containerdrs.getSale() != null && containerdrs.getSale().getContainer() != null)
            contdrs.setContainer(containerdrs.getContainer());

        if (containerdrs.getFromTerminal() != null) contdrs.setFromTerminal(containerdrs.getFromTerminal());
                // else contdrs.setFromTerminal(null);
        if (containerdrs.getToTerminal() != null) contdrs.setToTerminal(containerdrs.getToTerminal());
                // else contdrs.setToTerminal(null);
//        if (containerdrs.isStarted()) contdrs.setStarted(containerdrs.isStarted());
//        if (containerdrs.isFinished()) contdrs.setFinished(containerdrs.isFinished());
        contdrs.setStarted(containerdrs.isStarted());
        contdrs.setFinished(containerdrs.isFinished());
        // Проверка записи обновленной DRS Заявки
        System.out.println("Сохранение записи ContainerDrs ==================");
        containerdrs.printDrs(containerdrs);
        containerdrs.printDrs(contdrs);
        System.out.println("=================================================");
        // Сохранение записи в таблице
        containerdrsRepository.save(contdrs);
    }

    // Удаление DRS-Заявки в БД
    public void deleteContainerdrs(Long id) {
        containerdrsRepository.deleteById(id);
    }

    // Поиск DRS Заявок по дате
    public List<Containerdrs> getTodayContainerDrs() {
        LocalDate today = LocalDate.now();
        return containerdrsRepository.findAll().stream()
                .filter(contdrs -> today.equals(contdrs.getSale().getFromDate()))
                .collect(Collectors.toList());
    }

    // Для ContOwner =============================================================
    // Для ContOwner =============================================================
    // Поиск DRS-Заявок Владельца КТК по имени Владельца
    public List<Containerdrs> getContDrsByContOwnerName(String contOwnerName) {
        return containerdrsRepository.findContDrsByContOwnerName(contOwnerName);
    }

    // Поиск DRS-Заявок Владельца КТК по дате Заявки
    public List<Containerdrs> getContDrsByContOwnerNameAndSaleDate(String contOwnerName,
                                                                   LocalDate fromDate) {
        return containerdrsRepository.findContDrsByContOwnerNameAndSaleDate(contOwnerName,
                fromDate);
    }

    // Поиск DRS-Заявок Владельца КТК на текущий месяц
    public List<Containerdrs> getContDrsByContOwnerNameAndCurrentMonth(String contOwnerName) {
        return containerdrsRepository.findContDrsByContOwnerNameAndCurrentMonth(contOwnerName);
    }

    // Поиск DRS-Заявок Владельца КТК с.. по..
    public List<Containerdrs> getContDrsByContOwnerNameAndSaleDateFromTo(String contOwnerName,
                                                LocalDate fromDate, LocalDate toDate) {
        return containerdrsRepository.findContDrsByContOwnerNameAndSaleDateFromTo(contOwnerName,
                fromDate, toDate);
    }
    // Для ContOwner =============================================================
    // Для ContOwner =============================================================



    // Для Driver ================================================================
    // Для Driver ================================================================
    // DRS Заявки Водителя (Все)
    public List<Containerdrs> getByDriverDrsAll(Long id) {
        return containerdrsRepository.findContDrsByDriverAll(id);
    }

    // DRS Заявки Водителя (на Сегодня)
    public List<Containerdrs> getByDriverDrsToday(Long id, LocalDate today) {
        return containerdrsRepository.findContDrsByDriverToday(id, today);
    }

    // DRS Заявки Водителя (за Неделю)
    public List<Containerdrs> getByDriverDrsWeek(Long id,
                                                 LocalDate startOfWeek,
                                                 LocalDate endOfWeek) {
        return containerdrsRepository.findContDrsByDriverWeek(id,
                startOfWeek, endOfWeek);
    }

    // DRS Заявки Водителя (за Месяц)
    public List<Containerdrs> getByDriverDrsMonth(Long id) {
        return containerdrsRepository.findContDrsByDriverMonth(id);
    }


    // =========================================================================
    // DRIVER REPORTS ==========================================================
    // Печать всех DRS Заявок Водителя
    public byte[] generateDrContDrsAllReport(String driverName,
                                             Long driverId) throws JRException {
        // Загружаем JRXML (НЕ .jasper)
        InputStream reportStream = getClass().getResourceAsStream("/reports/tc_contDrsAllReport.jrxml");
        //
        if (reportStream == null) {
            throw new IllegalStateException("Файл отчета не найден: tc_contDrsAllReport.jrxml");
        }
        // Компиляция JRXML в JasperReport
        JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

        // Получаем все DRS Заявки Владельца
        List<Containerdrs> contDrs = getByDriverDrsAll(driverId);
        // Определяем кол-во КТК в списке
        int contDrsCount = contDrs.size();
        String PHeaderLeft = "Водитель: " +
                driverName + " - [" +
                Integer.toString(contDrsCount) + "]";
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(contDrs);
        // Параметры (если нужно — можно прокидывать в отчет)
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "Система");
        parameters.put("Title1", "Заявки Водителя");
        parameters.put("Title2", "(все заявки)");
        parameters.put("PHeaderLeft", PHeaderLeft);
//        parameters.put("contDrsCount", contDrsCount);
        // Заполнение отчета
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        jasperPrint.setProperty(
                "net.sf.jasperreports.export.pdf.metadata.title",
                "Все заявки"
        );
        // Экспорт в PDF (в виде массива байт, можно вернуть из контроллера)
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    // Печать DRS Заявок Перевозчика на Сегодня
    public byte[] generateDrContDrsTodayReport(String driverName,
                                               Long driverId, LocalDate today) throws JRException {
        // Загружаем JRXML (НЕ .jasper)
        InputStream reportStream = getClass().getResourceAsStream("/reports/tc_contDrsAllReport.jrxml");
        //
        if (reportStream == null) {
            throw new IllegalStateException("Файл отчета не найден: tc_contDrsAllReport.jrxml");
        }
        // Компиляция JRXML в JasperReport
        JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

        // Получаем все DRS Заявки Владельца на Сегодня
        List<Containerdrs> contDrs = getByDriverDrsToday(driverId, today);
        // Определяем кол-во КТК в списке
        int contDrsCount = contDrs.size();
        String PHeaderLeft = "Водитель: " +
                driverName + " - [" +
                Integer.toString(contDrsCount) + "]";
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(contDrs);
        // Параметры (если нужно — можно прокидывать в отчет)
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "Система");
        parameters.put("Title1", "Заявки Водителя");
        parameters.put("Title2", "(заявки на Сегодня)");
        parameters.put("PHeaderLeft", PHeaderLeft);
//        parameters.put("contDrsCount", contDrsCount);
        // Заполнение отчета
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        jasperPrint.setProperty(
                "net.sf.jasperreports.export.pdf.metadata.title",
                "Заявки на Сегодня"
        );
        // Экспорт в PDF (в виде массива байт, можно вернуть из контроллера)
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    // Печать DRS Заявок Водителя за Неделю
    public byte[] generateDrContDrsWeekReport(String driverName,
                                              Long driverId,
                                              LocalDate startOfWeek,
                                              LocalDate endOfWeek) throws JRException {
        // Загружаем JRXML (НЕ .jasper)
        InputStream reportStream = getClass().getResourceAsStream("/reports/tc_contDrsAllReport.jrxml");
        //
        if (reportStream == null) {
            throw new IllegalStateException("Файл отчета не найден: tc_contDrsAllReport.jrxml");
        }
        // Компиляция JRXML в JasperReport
        JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

        // Получаем все DRS Заявки Владельца на Сегодня
        List<Containerdrs> contDrs = getByDriverDrsWeek(driverId,
                startOfWeek, endOfWeek);
        // Определяем кол-во КТК в списке
        int contDrsCount = contDrs.size();
        String PHeaderLeft = "Водитель: " +
                driverName + " - [" +
                Integer.toString(contDrsCount) + "]";
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(contDrs);
        // Параметры (если нужно — можно прокидывать в отчет)
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "Система");
        parameters.put("Title1", "Заявки Водителя");
        parameters.put("Title2", "(заявки за Неделю)");
        parameters.put("PHeaderLeft", PHeaderLeft);
//        parameters.put("contDrsCount", contDrsCount);
        // Заполнение отчета
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        jasperPrint.setProperty(
                "net.sf.jasperreports.export.pdf.metadata.title",
                "Заявки за Неделю"
        );
        // Экспорт в PDF (в виде массива байт, можно вернуть из контроллера)
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    // Печать DRS Заявок Перевозчика за Месяц
    public byte[] generateDrContDrsMonthReport(String driverName,
                                               Long driverId) throws JRException {
        // Загружаем JRXML (НЕ .jasper)
        InputStream reportStream = getClass().getResourceAsStream("/reports/tc_contDrsAllReport.jrxml");
        //
        if (reportStream == null) {
            throw new IllegalStateException("Файл отчета не найден: tc_contDrsAllReport.jrxml");
        }
        // Компиляция JRXML в JasperReport
        JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

        // Получаем все DRS Заявки Владельца на Сегодня
        List<Containerdrs> contDrs = getByDriverDrsMonth(driverId);
        // Определяем кол-во КТК в списке
        int contDrsCount = contDrs.size();
        String PHeaderLeft = "Водитель: " +
                driverName + " - [" +
                Integer.toString(contDrsCount) + "]";
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(contDrs);
        // Параметры (если нужно — можно прокидывать в отчет)
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "Система");
        parameters.put("Title1", "Заявки Водителя");
        parameters.put("Title2", "(заявки за Месяц)");
        parameters.put("PHeaderLeft", PHeaderLeft);
//        parameters.put("contDrsCount", contDrsCount);
        // Заполнение отчета
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        jasperPrint.setProperty(
                "net.sf.jasperreports.export.pdf.metadata.title",
                "Заявки за Месяц"
        );
        // Экспорт в PDF (в виде массива байт, можно вернуть из контроллера)
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }
    // DRIVER REPORTS ==========================================================
    // =========================================================================


    // Для Driver ================================================================
    // Для Driver ================================================================



}
