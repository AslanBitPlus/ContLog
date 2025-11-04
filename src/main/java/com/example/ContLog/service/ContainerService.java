package com.example.ContLog.service;

import com.example.ContLog.entity.Container;
import com.example.ContLog.entity.Containerdrs;
import com.example.ContLog.entity.Terminal;
import com.example.ContLog.repository.ContainerRepository;
import com.example.ContLog.repository.ContainerdrsRepository;
import com.example.ContLog.repository.DriverRepository;
import com.example.ContLog.repository.TerminalRepository;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// JasperReports импорты
import net.sf.jasperreports.engine.*;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ContainerService {

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private TerminalRepository terminalRepository;

    @Autowired
    private ContainerRepository containerRepository;

    @Autowired
    private ContainerdrsRepository containerdrsRepository;

    // Поиск всех DRS-Заявок
    public List<Containerdrs> getAllContainerDrs() {
        return containerdrsRepository.findAll();
    }

    // Поиск всех КТК
    public List<Container> getAllContainers() {
        return containerRepository.findAll();
    }

    // Поиск всех Терминалов
    public List<Terminal> getAllTerminals() {
        return terminalRepository.findAll();
    }

    // Поиск КТК по Id
    public Container getContainer(Long id) {
        return containerRepository.findById(id).orElse(null);
    }

    // Сохранение КТК
    public Container createContainer(Container container) {
        return containerRepository.save(container);
    }

    // Обновление данных КТК в БД
    public void updateContainer(Container container, Long id) {
        Container ctr = containerRepository.getReferenceById(id);
        ctr.setContowner(container.getContowner());
        ctr.setCode(container.getCode());
        ctr.setNumber(container.getNumber());
        ctr.setType(container.getType());
        ctr.setWeighttare(container.getWeighttare());
        ctr.setMaxweight(container.getMaxweight());
        ctr.setPlace(container.getPlace());
        ctr.setPlaceId(container.getPlaceId());
        ctr.setW(container.getW());
        containerRepository.save(ctr);
    }

    // Удаление КТК
    public void deleteContainer(Long id) {
        containerRepository.deleteById(id);
    }

    public List<Container> getByContownerId(Long contOwnerId) {
        return containerRepository.findByContownerId(contOwnerId);
    }

    public List<Container> getByContOwnerName(String contOwnerName) {
        return containerRepository.findByContOwnerName(contOwnerName);
    }

    // Поиск КТК по Владельцу КТК и по терминалам
    public List<Container> getContainersByOwnerAndFromTerminals(String contOwnerName) {
        List<Container> containers = containerRepository.findContainersByOwnerAndFromTerminals(contOwnerName);
        // Заполняем поле типа Transient - container.placeName
        // Метод restoreContainersWithPlaceName(containers);
        return restoreContainersWithPlaceName(containers);
    }

    // Поиск КТК по Владельцу КТК и по авто
    public List<Container> getContainersByOwnerAndFromAvto(String contOwnerName) {
        List<Container> containers = containerRepository.findContainersByOwnerAndFromAvto(contOwnerName);
        // Заполняем поле типа Transient - container.placeName
        // Метод restoreContainersWithPlaceName(containers);
        return restoreContainersWithPlaceName(containers);
    }

    // Поиск КТК по Владельцу КТК с местом нахождения
    public List<Container> getContOwnerContainersWithPlaceNames(String contOwnerName) {
        List<Container> containers = getByContOwnerName(contOwnerName);
        // Заполняем поле типа Transient - container.placeName
        // Метод restoreContainersWithPlaceName(containers);
        return restoreContainersWithPlaceName(containers);
    }

    // REPORTS ================================================================
    // Демо отчет - Печать бланка Акта получения/сдачи КТК на Терминал
    public byte[] generateBlankReport() throws JRException {
        // Загружаем jrxml-файл из ресурсов
        InputStream reportStream = getClass().getResourceAsStream("/reports/Test_1.jrxml");
        //
        if (reportStream == null) {
            throw new IllegalStateException("Не найден файл отчета: Test_1.jrxml");
        }
        // Компиляция jrxml в jasper
        JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

//        // Загружаем .jasper файл из ресурсов
//        InputStream reportStream = getClass().getResourceAsStream("/reports/Test_1.jasper");
//
//        if (reportStream == null) {
//            throw new IllegalStateException("Не найден файл отчета: Test_1.jasper");
//        }
//
//        // Загружаем готовый компилированный отчет
//        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(reportStream);

        // Параметры (если нужны, можно убрать или оставить пустыми)
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "Система"); // пример
        // Пустой источник данных — т.к. отчет-бланк
        JRDataSource dataSource = new JREmptyDataSource();
        // Заполнение отчета
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        // Экспорт в PDF файл
        JasperExportManager.exportReportToPdfFile(jasperPrint, "Test_1.pdf");
        // Экспорт в PDF
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    // Печать Списка КТК по местам нахождения
    public byte[] generateContainerPlaceReport(String contOwnerName) throws JRException {
        // Загружаем JRXML (НЕ .jasper)
        InputStream reportStream = getClass().getResourceAsStream("/reports/containers_report.jrxml");
        //
        if (reportStream == null) {
            throw new IllegalStateException("Файл отчета не найден: containers_report.jrxml");
        }
        // Компиляция JRXML в JasperReport
        JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

        // Получаем все контейнеры Владельца из БД по местам нахождения
        List<Container> containers = getContOwnerContainersWithPlaceNames(contOwnerName);
        // Определяем кол-во КТК в списке
        int contCount = containers.size();
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(containers);
        // Параметры (если нужно — можно прокидывать в отчет)
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "Система");
        parameters.put("Title1", "Список контейнеров");
        parameters.put("Title2", "по местам нахождения");
        parameters.put("contOwnerName", contOwnerName);
        parameters.put("contCount", contCount);
        // Заполнение отчета
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        jasperPrint.setProperty(
                "net.sf.jasperreports.export.pdf.metadata.title",
                "КТК по Местам"
        );
        // Экспорт в PDF (в виде массива байт, можно вернуть из контроллера)
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    // Печать Списка КТК по терминалам
    public byte[] generateContainerTerminalReport(String contOwnerName) throws JRException {
        // Загружаем JRXML (НЕ .jasper)
        InputStream reportStream = getClass().getResourceAsStream("/reports/containers_report.jrxml");
        //
        if (reportStream == null) {
            throw new IllegalStateException("Файл отчета не найден: containers_report.jrxml");
        }
        // Компиляция JRXML в JasperReport
        JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

        // Получаем все контейнеры Владельца из БД по терминалам
        List<Container> containers = getContainersByOwnerAndFromTerminals(contOwnerName);
        // Определяем кол-во КТК в списке
        int contCount = containers.size();
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(containers);
        // Параметры (если нужно — можно прокидывать в отчет)
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "Система");
        parameters.put("Title1", "Список контейнеров");
        parameters.put("Title2", "по терминалам");
        parameters.put("contOwnerName", contOwnerName);
        parameters.put("contCount", contCount);
        // Заполнение отчета
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        jasperPrint.setProperty(
                "net.sf.jasperreports.export.pdf.metadata.title",
                "КТК по Терминалам"
        );
        // Экспорт в PDF (в виде массива байт, можно вернуть из контроллера)
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    // Печать Списка КТК по терминалам
    public byte[] generateContainerAvtoReport(String contOwnerName) throws JRException {
        // Загружаем JRXML (НЕ .jasper)
        InputStream reportStream = getClass().getResourceAsStream("/reports/containers_report.jrxml");
        //
        if (reportStream == null) {
            throw new IllegalStateException("Файл отчета не найден: containers_report.jrxml");
        }
        // Компиляция JRXML в JasperReport
        JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

        // Получаем все контейнеры Владельца из БД по терминалам
        List<Container> containers = getContainersByOwnerAndFromAvto(contOwnerName);
        // Определяем кол-во КТК в списке
        int contCount = containers.size();
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(containers);
        // Параметры (если нужно — можно прокидывать в отчет)
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "Система");
        parameters.put("Title1", "Список контейнеров");
        parameters.put("Title2", "по Авто");
        parameters.put("contOwnerName", contOwnerName);
        parameters.put("contCount", contCount);
        // Заполнение отчета
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        jasperPrint.setProperty(
                "net.sf.jasperreports.export.pdf.metadata.title",
                "КТК по Авто"
        );
        // Экспорт в PDF (в виде массива байт, можно вернуть из контроллера)
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    // REPORTS ================================================================

    public List<Container> restoreContainersWithPlaceName(List<Container> containers) {
        for (Container container : containers) {
            String place = container.getPlace();
            Long placeId = container.getPlaceId();

            switch (place) {
                case "d":
                    driverRepository.findById(placeId).ifPresent(driver ->
                            container.setPlaceName("Водитель: " + driver.getSurename() + " " +
                                    driver.getName())
                    );
                    break;
                case "r":
                    container.setPlaceName("Ж/Д     : ");
                    break;
                case "s":
                    container.setPlaceName("Вод/тр  : ");
                    break;
                case "t":
                    terminalRepository.findById(placeId).ifPresent(terminal ->
                            container.setPlaceName("Терминал: " + terminal.getShortName())
                    );
                    break;
                case "b":
                    terminalRepository.findById(placeId).ifPresent(terminal ->
                            container.setPlaceName("НА РЕМОНТЕ - Терминал: " + terminal.getShortName())
                    );
                    break;
                case "o":
                    terminalRepository.findById(placeId).ifPresent(terminal ->
                            container.setPlaceName("ПРОДАЕТСЯ - Терминал: " + terminal.getShortName())
                    );
                    break;
                case "x":
                    container.setPlaceName("ЗА ГРАНИЦЕЙ: ");
                    break;
                default:
                    container.setPlaceName("Не определено");
            }
        }
        return containers;
    }


}
