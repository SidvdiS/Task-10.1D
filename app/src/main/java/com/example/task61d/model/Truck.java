package com.example.task61d.model;

public class Truck {

    private String driverName, phoneNumber, truckType, truckInfo;
    private int truckImg;

    public Truck(String driverName, String phoneNumber, String truckType, String truckInfo, int truckImg) {
        this.driverName = driverName;
        this.phoneNumber = phoneNumber;
        this.truckType = truckType;
        this.truckInfo = truckInfo;
        this.truckImg = truckImg;
    }

    public Truck(String driverName, String phoneNumber, String truckType, String truckInfo) {
        this.driverName = driverName;
        this.phoneNumber = phoneNumber;
        this.truckType = truckType;
        this.truckInfo = truckInfo;
    }

    public Truck(){

    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getTruckType() {
        return truckType;
    }

    public void setTruckType(String truckType) {
        this.truckType = truckType;
    }

    public String getTruckInfo() {
        return truckInfo;
    }

    public void setTruckInfo(String truckInfo) {
        this.truckInfo = truckInfo;
    }

    public int getTruckImg() {
        return truckImg;
    }

    public void setTruckImg(int truckImg) {
        this.truckImg = truckImg;
    }
}
