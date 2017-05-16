package com.glyuk.volunterevents.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ModelEvents extends RealmObject {

    @PrimaryKey
    private int id;
    private String image;
    private String name;
    private String description;
    private String address;
    private int member;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getMember() {
        return member;
    }

    public void setMember(int member) {
        this.member = member;
    }
}
