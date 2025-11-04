package com.example.ContLog.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Дата и время регистрации заявки
    @Column (nullable = false, updatable = false)
    private LocalDateTime creationDateTime;

    // Дата начала выполнения заявки (Загрузка)
    @Column
    private LocalDate fromDate;

    //Время начала выполнения заявки (Загрузка)
    @Column
    private LocalTime fromTime;

    //Адрес точки А - получения груза (Загрузка)
    @Column
    private String fromAddress;

    // Дата завершения выполнения заявки (Выгрузка)
    @Column
    private LocalDate toDate;

    //Время завершения выполнения заявки (Выгрузка)
    @Column
    private LocalTime toTime;

    //Адрес точки Б - сдачи груза (Выгрузка)
    @Column
    private String toAddress;

    // Менеджер принявший заявку
    @Column
    private String manager;

    @Column
    private boolean started;

    @Column
    private boolean finished;

    // Заявка Связь с таблицей  Contowner
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "CONTOWNER_ID")
    private Contowner contowner = null;

    // КТК Связь с таблицей  Container
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CONTAINER_ID") // nullable = false)
    private Container container = null;

    @OneToMany(mappedBy = "sale",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    List<Containerdrs> containerdrss = new ArrayList<>();

    // Пустой конструктор
    public Sale() {
    }

    // Конструктор
    public Sale(Long id, LocalDateTime creationDateTime, LocalDate fromDate,
                LocalTime fromTime, String fromAddress, LocalDate toDate,
                LocalTime toTime, String toAddress, String manager,
                boolean started, boolean finished, Contowner contowner,
                Container container, List<Containerdrs> containerdrss) {
        this.id = id;
        this.creationDateTime = LocalDateTime.now();
        this.fromDate = fromDate;
        this.fromTime = fromTime;
        this.fromAddress = fromAddress;
        this.toDate = toDate;
        this.toTime = toTime;
        this.toAddress = toAddress;
        this.manager = manager;
        this.started = started;
        this.finished = finished;
        this.contowner = contowner;
        this.container = container;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(LocalDateTime creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalTime getFromTime() {
        return fromTime;
    }

    public void setFromTime(LocalTime fromTime) {
        this.fromTime = fromTime;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public LocalTime getToTime() {
        return toTime;
    }

    public void setToTime(LocalTime toTime) {
        this.toTime = toTime;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
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

    public Contowner getContowner() {
        return contowner;
    }

    public void setContowner(Contowner contowner) {
        this.contowner = contowner;
    }

    public Container getContainer() {
        return container;
    }

    public void setContainer(Container container) {
        this.container = container;
    }

    public List<Containerdrs> getContainerdrss() {
        return containerdrss;
    }

    public void setContainerdrss(List<Containerdrs> containerdrss) {
        this.containerdrss = containerdrss;
    }


    // Преобразование в строку поля fromDate
    public String fromDateToStr() {
        return fromDate != null ? fromDate.toString() : "";
    }

    // Преобразование в строку поля toDate
    public String toDateToStr() {
        return toDate != null ? toDate.toString() : "";
    }

    // Преобразование в строку поля creationDateTime
    public String creationDateTimeToStr() {
        return LocalDateTime.now().toString();
    }

    // Установка даты из строки для поля fromDate
    public void setFromDateFromString(String dateString) {
        this.fromDate = dateString != null && !dateString.isEmpty()
                ? LocalDate.parse(dateString)
                : null;
    }

    // Установка даты из строки для поля toDate
    public void setToDateFromString(String dateString) {
        this.toDate = dateString != null && !dateString.isEmpty()
                ? LocalDate.parse(dateString)
                : null;
    }


//    @Override
    public String toString() {
        return "Заявка{" +
                "id=" + id +
                ", Дата оформления заявки=" + creationDateTime +
                ", Дата начала=" + fromDate +
                ", Время начала=" + fromTime +
                ", Адрес пог/выг='" + fromAddress + '\'' +
                ", Дата заверщения=" + toDate +
                ", Время заверщения=" + toTime +
                ", Адрес выг/пог='" + toAddress + '\'' +
                ", Менеджер приема заявки='" + manager + '\'' +
                ", Выполняется=" + started +
                ", Заверщено=" + finished +
                ", Сток=" + contowner +
                ", КТК=" + container +
                ", КТК/DRS=" + containerdrss +
                '}';
    }
}
