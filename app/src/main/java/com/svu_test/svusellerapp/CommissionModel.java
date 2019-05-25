package com.svu_test.svusellerapp;

public class CommissionModel {

    private String id, sellerID, year, month;
    private int amount;

    public CommissionModel(String id, String sellerID, String year, String month, int amount) {
        this.id = id;
        this.sellerID = sellerID;
        this.year = year;
        this.month = month;
        this.amount = amount;
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
}
