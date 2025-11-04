package com.example.ContLog.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Container {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="CONTOWNER_ID", nullable = false)
    @JsonIgnore
    private Contowner contowner;

    @Column
    private String code;

    @Column
    private String number;

    @Column
    private String type;

    @Column(nullable = false)
    private int weighttare=3700;

    @Column(nullable = false)
    private int maxweight=32000;

    // КТК движение  (d-Авто, r-РЖД, s-Кробль, t-Терминал, b-Неисправен, o-Продается)
    @Column(length = 1)// (nullable = false)
    private String place;

    // КТК движение Id (Водителя-при {d},ЖД состава при {r},
    // Кробля при {s}, Терминала при {t, b, o}, Государства при {x})
    @Column(length = 1)// (nullable = false)
    private Long placeId;

    // КТК груж/пор (1-Груженый, 0-Порожний)
    @Column(nullable = false)
    private int w = 0;

    @OneToMany(mappedBy = "container",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    List<Containerdrs> containerdrss = new ArrayList<>();

    @Transient
    private String placeName;

    // Конструктор пустой
    public Container() {
    }

    // Конструктор
    public Container(Long id, Contowner contowner, String code, String number,
                     String type, int weighttare, int maxweight, String place,
                     Long placeId, int w, List<Containerdrs> containerdrss,
                     String placeName) {
        this.id = id;
        this.contowner = contowner;
        this.code = code;
        this.number = number;
        this.type = type;
        this.weighttare = weighttare;
        this.maxweight = maxweight;
        this.place = place;
        this.placeId = placeId;
        this.w = w;
        this.containerdrss = containerdrss;
        this.placeName = placeName;
    }

    // Геттеры и Сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Contowner getContowner() {
        return contowner;
    }

    public void setContowner(Contowner contowner) {
        this.contowner = contowner;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getWeighttare() {
        return weighttare;
    }

    public void setWeighttare(int weighttare) {
        this.weighttare = weighttare;
    }

    public int getMaxweight() {
        return maxweight;
    }

    public void setMaxweight(int maxweight) {
        this.maxweight = maxweight;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Long getPlaceId() {
        return placeId;
    }

    public void setPlaceId(Long placeId) {
        this.placeId = placeId;
    }

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public List<Containerdrs> getContainerdrss() {
        return containerdrss;
    }

    public void setContainerdrss(List<Containerdrs> containerdrss) {
        this.containerdrss = containerdrss;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    @Override
    public String toString() {
        return "Container{" +
                "id=" + id +
                ", contowner=" + contowner +
                ", code='" + code +
                ", number='" + number +
                ", type='" + type +
                ", weighttare=" + weighttare +
                ", maxweight=" + maxweight +
                ", place=" + place +
                ", placeId=" + placeId +
                ", w=" + w +
                ", containerdrss=" + containerdrss +
                ", placeName=" + placeName +
                '}';
    }


}
