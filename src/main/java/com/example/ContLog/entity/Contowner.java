package com.example.ContLog.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Contowner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "contowner",
              fetch = FetchType.LAZY,
              cascade = CascadeType.ALL)
    List<Container> containers = new ArrayList<>();

    // Пустой конструктор
    public Contowner() {
    }

    // Конструктор
    public Contowner(Long id, String name, List<Container> containers) {
        this.id = id;
        this.name = name;
        this.containers = containers;
    }

    public Contowner(String name) {
    }

    // Добавить КТК
    public void addContainer(Container container) {
        containers.add(container);
        container.setContowner(this);
    }

    // Удалить КТК
    public void removeContainer(Container container) {
        containers.remove(container);
        container.setContowner(null);
    }

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

    public List<Container> getContainers() {
        return containers;
    }

    public void setContainers(List<Container> containers) {
        this.containers = containers;
    }
}
