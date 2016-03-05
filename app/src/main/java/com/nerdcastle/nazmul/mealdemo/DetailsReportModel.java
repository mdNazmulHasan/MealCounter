package com.nerdcastle.nazmul.mealdemo;

/**
 * Created by Nazmul on 3/5/2016.
 */
public class DetailsReportModel {
    String date;
    String amount;

    public DetailsReportModel(String date, String amount) {
        this.date = date;
        this.amount = amount;
    }

    public DetailsReportModel() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
