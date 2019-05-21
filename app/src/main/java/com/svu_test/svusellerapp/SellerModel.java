package com.svu_test.svusellerapp;

import android.graphics.Bitmap;

public class SellerModel {

    private String id;
    private String name, region, number;
    private Bitmap image;

    public SellerModel(String id, String name, String region, String number, Bitmap image) {
        this.id = id;
        this.name = name;
        this.region = region;
        this.number = number;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRegion() {
        return region;
    }

    public String getNumber() {
        return number;
    }

    public Bitmap getImage() {
        return image;
    }
}
