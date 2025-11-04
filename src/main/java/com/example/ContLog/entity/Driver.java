package com.example.ContLog.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Data
public class Driver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="CARRIER_ID", nullable = false)
    @JsonIgnore
    private Carrier carrier;

    @Column(nullable = false)
    private String surename;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String patronymic;

    @Column(nullable = false)
    private String car;

    @Column(nullable = false)
    private String carnum;

    @Column(nullable = false)
    private String trailer;

    @Column(nullable = false)
    private String trailernum;

    @OneToMany(mappedBy = "driver",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    List<Containerdrs> containerdrss = new ArrayList<>();

    // Пустой конструктор
    public Driver() {
    }

    // Конструктор
    public Driver(Long id, Carrier carrier, String surename, String name, String patronymic,
                  String car, String carnum, String trailer, String trailernum) {
        this.id = id;
        this.carrier = carrier;
        this.surename = surename;
        this.name = name;
        this.patronymic = patronymic;
        this.car = car;
        this.carnum = carnum;
        this.trailer = trailer;
        this.trailernum = trailernum;
    }

    // Getterы и Setterы
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Carrier getCarrier() {
        return carrier;
    }

    public void setCarrier(Carrier carrier) {
        this.carrier = carrier;
    }

    public String getSurename() {
        return surename;
    }

    public void setSurename(String surename) {
        this.surename = surename;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car;
    }

    public String getCarnum() {
        return carnum;
    }

    public void setCarnum(String carnum) {
        this.carnum = carnum;
    }

    public String getTrailer() {
        return trailer;
    }

    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }

    public String getTrailernum() {
        return trailernum;
    }

    public void setTrailernum(String trailernum) {
        this.trailernum = trailernum;
    }

    public List<Containerdrs> getContainerdrss() {
        return containerdrss;
    }

    public void setContainerdrss(List<Containerdrs> containerdrss) {
        this.containerdrss = containerdrss;
    }

}

