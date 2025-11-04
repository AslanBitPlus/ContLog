package com.example.ContLog.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
//@Data
public class Terminal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Короткое наименование терминала
    @Column
    private String shortName;

    // Полное наименование терминала
    @Column
    private String fullName;

    // Адрес терминала
    @Column
    private String address;

    // Геолокация терминала
    @Column
    private String navi;

    @OneToMany(mappedBy = "fromTerminal",
            fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<Containerdrs> fromContainerdrss = new ArrayList<>();

    @OneToMany(mappedBy = "toTerminal",
            fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<Containerdrs> toContainerdrss = new ArrayList<>();

    // Пустой конструктор
    public Terminal() {
    }

    // Конструктор
    public Terminal(Long id, String shortName, String fullName, String address,
                    String navi, List<Containerdrs> fromContainerdrss,
                    List<Containerdrs> toContainerdrss) {
        this.id = id;
        this.shortName = shortName;
        this.fullName = fullName;
        this.address = address;
        this.navi = navi;
        this.fromContainerdrss = fromContainerdrss;
        this.toContainerdrss = toContainerdrss;
    }

    // Getterы и Setterы
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNavi() {
        return navi;
    }

    public void setNavi(String navi) {
        this.navi = navi;
    }

    public List<Containerdrs> getFromContainerdrss() {
        return fromContainerdrss;
    }

    public void setFromContainerdrss(List<Containerdrs> fromContainerdrss) {
        this.fromContainerdrss = fromContainerdrss;
    }

    public List<Containerdrs> getToContainerdrss() {
        return toContainerdrss;
    }

    public void setToContainerdrss(List<Containerdrs> toContainerdrss) {
        this.toContainerdrss = toContainerdrss;
    }

    @Override
    public String toString() {
        return "Terminal{" +
                "id=" + id +
                ", shortName='" + shortName + '\'' +
                ", fullName='" + fullName + '\'' +
                ", address='" + address + '\'' +
                ", navi='" + navi + '\'' +
                ", fromContainerdrss=" + fromContainerdrss +
                ", toContainerdrss=" + toContainerdrss +
                '}';
    }
}
