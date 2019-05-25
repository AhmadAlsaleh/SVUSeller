package com.svu_test.svusellerapp;

public class SaleModel {

    private String id, sellerID, year, month, region;
    private int amount;

    public SaleModel(String id, String sellerID, String year, String month, int amount, String region) {
        this.id = id;
        this.sellerID = sellerID;
        this.year = year;
        this.month = month;
        this.amount = amount;
        this.region = region;
    }

    public String getId() {
        return id;
    }

    public String getSellerID() {
        return sellerID;
    }

    public String getYear() {
        return year;
    }

    public String getMonth() {
        return month;
    }

    public int getAmount() {
        return amount;
    }

    public String getRegion() {
        return region;
    }
}
