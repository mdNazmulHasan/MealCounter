package com.nerdcastle.nazmul.mealdemo;

/**
 * Created by Nazmul on 3/1/2016.
 */
public class ReportModel {
    String name;
    String total;
    String selfAmount;
    String officeAmount;

    public ReportModel(String name, String total, String selfAmount, String officeAmount) {
        this.name = name;
        this.total = total;
        this.selfAmount = selfAmount;
        this.officeAmount = officeAmount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getSelfAmount() {
        return selfAmount;
    }

    public void setSelfAmount(String selfAmount) {
        this.selfAmount = selfAmount;
    }

    public String getOfficeAmount() {
        return officeAmount;
    }

    public void setOfficeAmount(String officeAmount) {
        this.officeAmount = officeAmount;
    }
}
