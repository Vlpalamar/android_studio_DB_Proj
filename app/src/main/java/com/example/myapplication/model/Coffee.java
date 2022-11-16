package com.example.myapplication.model;

public class Coffee {
    String id;
    int coffeeTypeId;
    String name;
    String description;
    String imgPath;
    float cost;

    public Coffee(String id, int coffeeTypeId, String name, String description, String imgPath, float cost) {
        this.id = id;
        this.coffeeTypeId = coffeeTypeId;
        this.name = name;
        this.description = description;
        this.imgPath = imgPath;
        this.cost = cost;
    }

    public Coffee() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCoffeeTypeId() {
        return coffeeTypeId;
    }

    public void setCoffeeTypeId(int coffeeTypeId) {
        this.coffeeTypeId = coffeeTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }
}
