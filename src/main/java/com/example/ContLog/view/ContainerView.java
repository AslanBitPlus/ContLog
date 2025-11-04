package com.example.ContLog.view;

public class ContainerView {
    private Long id;
    private String contowner;
    private String code;
    private String number;
    private String type;
    private int weighttare;
    private int maxweight;
    private String place;
    private String placeDisplay;

    // Конструктор пустой
    public ContainerView() {
    }

    // Конструктор
    public ContainerView(Long id, String contowner, String code, String number,
                         String type, int weighttare, int maxweight,
                         String place, String placeDisplay) {
        this.id = id;
        this.contowner = contowner;
        this.code = code;
        this.number = number;
        this.type = type;
        this.weighttare = weighttare;
        this.maxweight = maxweight;
        this.place = place;
        this.placeDisplay = placeDisplay;
    }

    public Long getId() {
        return id;
    }

    public String getContowner() {
        return contowner;
    }

    public String getCode() {
        return code;
    }

    public String getNumber() {
        return number;
    }

    public String getType() {
        return type;
    }

    public int getWeighttare() {
        return weighttare;
    }

    public int getMaxweight() {
        return maxweight;
    }

    public String getPlace() {
        return place;
    }

    public String getPlaceDisplay() {
        return placeDisplay;
    }
}
