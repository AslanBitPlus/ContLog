package com.example.ContLog.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
//@Data
public class Containerdrs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Дата регистрации (для выдачи КТК)
    @Column(nullable = false, updatable = false)
    private LocalDateTime dateReg;

    // Дата с (для выдачи КТК)
    @Column
    private LocalDate dateFrom;

    // Дата по (для выдачи КТК)
    @Column
    private LocalDate dateTo;

    // Прием/Выдача (1-Прием, -1-Выдача, 0-Ввод первоначальных данных)
    @Column // (nullable = false)
    private int io;

    // Груженный/Порожний (1-Груженный, -1-Порожний)
    @Column // (nullable = false)
    private int w;

    // Вес КТК (Тара)
    @Column // (nullable = false)
    private int weightT;

    // Вес КТК (Нетто)
    @Column // (nullable = false)
    private int weightN;

    // Вес КТК (Брутто)
    @Column // (nullable = false)
    private int weightB;

    // Вид Выдачи/Приема  (0-Перемещение, 1-Старт, -1-Финиш)
    @Column // (nullable = false)
    private int typeDrs;

    // КТК движение  (d-Авто, r-РЖД, s-Кробль, t-Терминал)
    @Column(length = 1)// (nullable = false)
    private String drs;

    // Релиз
    @Column // (nullable = false)
    private String relis;

    @Column (nullable = false)
    private boolean started = false;

    @Column (nullable = false)
    private boolean finished = false;

    // Заявка Связь с таблицей  Sale
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "SALE_ID", nullable = false)
//    @JsonIgnore
    private Sale sale;

    // Заявка Связь с таблицей  Carrier
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "CARRIER_ID")
    private Carrier carrier;

    // Водитель Связь с таблицей  Driver
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DRIVER_ID") // nullable = false)
//    @JsonIgnore
    private Driver driver;

    // КТК Связь с таблицей  Container
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CONTAINER_ID") // nullable = false)
//    @JsonIgnore
    private Container container;

    // Терминал выдачи Связь с таблицей  Terminal
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FROMTERMINAL_ID")
//    @JsonIgnore
    private Terminal fromTerminal;

    // Терминал приема Связь с таблицей  Terminal
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TOTERMINAL_ID")
//    @JsonIgnore
    private Terminal toTerminal;

    // Пустой конструктор
    public Containerdrs() {
    }

    // Конструктор
    public Containerdrs(Long id, LocalDateTime dateReg, LocalDate dateFrom, LocalDate dateTo,
                        int io, int w, int weightT, int weightN, int weightB, int typeDrs,
                        String drs, String relis, boolean started, boolean finished,
                        Sale sale, Carrier carrier, Driver driver, Container container,
                        Terminal fromTerminal, Terminal toTerminal) {
        this.id = id;
        this.dateReg = LocalDateTime.now();
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.io = io;
        this.w = w;
        this.weightT = weightT;
        this.weightN = weightN;
        this.weightB = weightB;
        this.typeDrs = typeDrs;
        this.drs = drs;
        this.relis = relis;
        this.started = started;
        this.finished = finished;
        this.sale = sale;
        this.carrier = carrier;
        this.driver = driver;
        this.container = container;
        this.fromTerminal = fromTerminal;
        this.toTerminal = toTerminal;
    }

    // ToString


    // Getter-ы и Setter-ы
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDateReg() {
        return dateReg;
    }

    public void setDateReg(LocalDateTime dateReg) {
        this.dateReg = dateReg;
    }

    public LocalDate getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(LocalDate dateFrom) {
        this.dateFrom = dateFrom;
    }

    public LocalDate getDateTo() {
        return dateTo;
    }

    public void setDateTo(LocalDate dateTo) {
        this.dateTo = dateTo;
    }

    public int getIo() {
        return io;
    }

    public void setIo(int io) {
        this.io = io;
    }

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getWeightT() {
        return weightT;
    }

    public void setWeightT(int weightT) {
        this.weightT = weightT;
    }

    public int getWeightN() {
        return weightN;
    }

    public void setWeightN(int weightN) {
        this.weightN = weightN;
    }

    public int getWeightB() {
        return weightB;
    }

    public void setWeightB(int weightB) {
        this.weightB = weightB;
    }

    public int getTypeDrs() {
        return typeDrs;
    }

    public void setTypeDrs(int typeDrs) {
        this.typeDrs = typeDrs;
    }

    public String getDrs() {
        return drs;
    }

    public void setDrs(String drs) {
        this.drs = drs;
    }

    public String getRelis() {
        return relis;
    }

    public void setRelis(String relis) {
        this.relis = relis;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public Sale getSale() {
        return sale;
    }

    public void setSale(Sale sale) {
        this.sale = sale;
    }

    public Carrier getCarrier() {
        return carrier;
    }

    public void setCarrier(Carrier carrier) {
        this.carrier = carrier;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Container getContainer() {
        return container;
    }

    public void setContainer(Container container) {
        this.container = container;
    }

    public Terminal getFromTerminal() {
        return fromTerminal;
    }

    public void setFromTerminal(Terminal fromTerminal) {
        this.fromTerminal = fromTerminal;
    }

    public Terminal getToTerminal() {
        return toTerminal;
    }

    public void setToTerminal(Terminal toTerminal) {
        this.toTerminal = toTerminal;
    }

    // Преобразование в строку поля creationDateTime
    public String dateRegToStr() {
        return LocalDateTime.now().toString();
    }

    // Преобразование в строку поля dateFrom
    public String dateFromToStr() {
        return dateFrom != null ? dateFrom.toString() : "";
    }

    // Преобразование в строку поля dateTo
    public String dateToToStr() {
        return dateTo != null ? dateTo.toString() : "";
    }

    public void printDrsL(Containerdrs containerdrs) {
        // Печать заявки DRS (в столбик)
        if (containerdrs != null) {
            // Определение Заявки
            String sale = (containerdrs.getSale() != null) ? ("Заявка №        :" + containerdrs.getSale().getId()) :
                    "Заявка №        :" + containerdrs.getSale();
            // Определение Стока
//            String contowner = (containerdrs.getSale().getContowner() != null) ? ("Сток            :" + containerdrs.getSale().getContowner().getId()) :
//                    "Сток            :" + containerdrs.getSale().getContowner();

            // Определение Транспортной компании
            String carrier = (containerdrs.getCarrier() != null) ? ("Тр/компания     :" + containerdrs.getCarrier().getId()) :
                    "Сток            :" + containerdrs.getCarrier();
            //
            System.out.println(sale);
            System.out.println("Номер DRS       :" + containerdrs.getId());
            System.out.println("Регистрация     :" + containerdrs.getDateReg());
//            System.out.println(contowner);
            System.out.println(carrier);
            System.out.println("Дата с...       :" + containerdrs.getDateFrom());
            System.out.println("Дата по...      :" + containerdrs.getDateTo());
            System.out.println("Прие.Выд        :" + containerdrs.getIo());
            System.out.println("Груж/Пор        :" + containerdrs.getW());
            System.out.println("Вес КТК (Тара)  :" + containerdrs.getWeightT());
            System.out.println("Вес КТК (Нетто) :" + containerdrs.getWeightN());
            System.out.println("Вес КТК (Брутто):" + containerdrs.getWeightB());
            System.out.println("Пер/Старт/Финииш:" + containerdrs.getTypeDrs());
            System.out.println("КТК (Сотояние)  :" + containerdrs.getDrs());
            System.out.println("Релиз на пост   :" + containerdrs.getRelis());
            if (containerdrs.getDriver() != null)
                System.out.println("Водитель        :" + containerdrs.getDriver().getName() + " " +
                        containerdrs.getDriver().getSurename());
            else
                System.out.println("Водитель        :" + containerdrs.getDriver());

            if (containerdrs.getContainer() != null)
                System.out.println("КТК...          :" + containerdrs.getContainer().getCode() + " " +
                        containerdrs.getContainer().getNumber());
            else
                System.out.println("КТК...          :" + containerdrs.getContainer());

            if (containerdrs.getFromTerminal() != null)
                System.out.println("Терминал из...  :" + containerdrs.getFromTerminal().getShortName());
            else
                System.out.println("Терминал из...  :" + containerdrs.getFromTerminal());

            if (containerdrs.getToTerminal() != null)
                System.out.println("Терминал в...   :" + containerdrs.getToTerminal().getShortName());
            else
                System.out.println("Терминал в...   :" + containerdrs.getToTerminal());
            //
        } else System.out.println("Заявка DRS = null");
    }


    public void printDrs(Containerdrs containerdrs) {
        // Печать заявки DRS (в строчку)
        if (containerdrs != null) {

            // Определение Заявки
            String sale = (containerdrs.getSale() != null) ? ("Заявка № :" + containerdrs.getSale().getId()) :
                    "Заявка № :" + containerdrs.getSale();
            // Определение Стока
//            String contowner = (containerdrs.getSale() != null && containerdrs.getSale().getContowner() != null) ? ("Сток :" + containerdrs.getSale().getContowner().getName()) :
//                    "Сток :" + containerdrs.getSale().getContowner();
            // Определение Транспортной компании
            String carrier = (containerdrs.getCarrier() != null) ? ("Тр/компания :" + containerdrs.getCarrier().getName()) :
                    "Тр/компания :" + containerdrs.getCarrier();
            // Определение Водителя
            String driver = (containerdrs.getDriver() != null) ? ("Водитель :[" + containerdrs.getDriver().getName() + " " +
                    containerdrs.getDriver().getSurename() + "]") : "Водитель :" + containerdrs.getDriver();
            // Определение КТК
            String cont = (containerdrs.getContainer() != null) ? ("КТК :[" + containerdrs.getContainer().getCode() + " " +
                    containerdrs.getContainer().getNumber() + "]") : "КТК :" + containerdrs.getContainer();
            // Определение Терминала из
            String fromTerm = (containerdrs.getFromTerminal() != null) ? ("Терминал из :[" + containerdrs.getFromTerminal().getShortName() + "]") :
                    "Терминал из :" + containerdrs.getFromTerminal();
            // Определение Терминала в
            String toTerm = (containerdrs.getToTerminal() != null) ? ("Терминал в :[" + containerdrs.getToTerminal().getShortName() + "]") :
                    "Терминал в :" + containerdrs.getToTerminal();

            System.out.println(sale +
                    ", Номер DRS :" + containerdrs.getId() +
                    ", Регистрация :" + containerdrs.getDateReg() +
//                    ", " + contowner +
                    ", " + carrier +
                    ", Дата с :" + containerdrs.getDateFrom() +
                    ", Дата по :" + containerdrs.getDateTo() +
                    ", Прие.Выд :" + containerdrs.getIo() +
                    ", Груж/Пор :" + containerdrs.getW() +
                    ", Вес КТК (Тара) :" + containerdrs.getWeightT() +
                    ", Вес КТК (Нетто) :" + containerdrs.getWeightN() +
                    ", Вес КТК (Брутто) :" + containerdrs.getWeightB() +
                    ", Пер/Старт/Финииш :" + containerdrs.getTypeDrs() +
                    ", КТК (Сотояние) :" + containerdrs.getDrs() +
                    ", Релиз на пост :" + containerdrs.getRelis() +
                    ", " + driver +
                    ", " + cont +
                    ", " + fromTerm +
                    ", " + toTerm +
                    ", " + containerdrs.isStarted() +
                    ", " + containerdrs.isFinished() + "}");
            //

        } else System.out.println("Заявка DRS = null");

    }
}

