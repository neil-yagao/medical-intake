package com.neil.medical.pojo;

/**
 * Created by nhu on 6/24/2017.
 */
public class PrescriptionChangeItem {

    private String medical;
    private String incOrDesc;
    private Double amount;
    private String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public PrescriptionChangeItem(String medical, String incOrDesc, Double amount) {
        this.medical = medical;
        this.incOrDesc = incOrDesc;
        this.amount = amount;
    }

    public PrescriptionChangeItem() {
    }

    public String getMedical() {
        return medical;
    }

    public void setMedical(String medical) {
        this.medical = medical;
    }

    public String getIncOrDesc() {
        return incOrDesc;
    }

    public void setIncOrDesc(String incOrDesc) {
        this.incOrDesc = incOrDesc;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
