package com.example.ContLog.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Carrier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @Column
    private String address;

    @OneToMany(mappedBy = "carrier",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    List<Driver> drivers = new ArrayList<>();

    // Пустой конструктор
    public Carrier() {
    }

    // Конструктор
    public Carrier(Long id, String name, String address, List<Driver> drivers) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.drivers = drivers;
    }

    // Добавить Водителя
    public void addDriver(Driver driver) {
        drivers.add(driver);
        driver.setCarrier(this);
    }

    // Удалить Водителя
    public void removeDriver(Driver driver) {
        drivers.remove(driver);
        driver.setCarrier(null);
    }


    // Getterы и Setterы
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Driver> getDrivers() {
        return drivers;
    }

    public void setDrivers(List<Driver> drivers) {
        this.drivers = drivers;
    }
}
