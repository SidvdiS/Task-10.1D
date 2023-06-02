package com.example.task61d.model;

public class Order {
    private String orderID, receiverName, pickupDate, pickupTime, goodType,
            vehicleType, pickupLatLong, pickupLocName, dropOffLatLong, dropOffLocName;
    private float orderWeight, orderWidth, orderLength, orderHeight;

    public Order(String orderID, String receiverName, String pickupDate, String pickupTime, String goodType,
                 String vehicleType, String pickupLatLong, String pickupLocName, String dropOffLatLong,
                 String dropOffLocName, float orderWeight, float orderWidth, float orderLength, float orderHeight) {
        this.orderID = orderID;
        this.receiverName = receiverName;
        this.pickupDate = pickupDate;
        this.pickupTime = pickupTime;
        this.goodType = goodType;
        this.vehicleType = vehicleType;
        this.pickupLatLong = pickupLatLong;
        this.dropOffLatLong = dropOffLatLong;
        this.orderWeight = orderWeight;
        this.orderWidth = orderWidth;
        this.orderLength = orderLength;
        this.orderHeight = orderHeight;
        this.pickupLocName = pickupLocName;
        this.dropOffLocName = dropOffLocName;

    }

    public Order(String receiverName, String pickupDate, String pickupTime, String goodType, String vehicleType,
                 String pickupLatLong, String pickupLocName, String dropOffLatLong, String dropOffLocName,
                 float orderWeight, float orderWidth, float orderLength, float orderHeight) {
        this.receiverName = receiverName;
        this.pickupDate = pickupDate;
        this.pickupTime = pickupTime;
        this.goodType = goodType;
        this.vehicleType = vehicleType;
        this.pickupLatLong = pickupLatLong;
        this.dropOffLatLong = dropOffLatLong;
        this.orderWeight = orderWeight;
        this.orderWidth = orderWidth;
        this.orderLength = orderLength;
        this.orderHeight = orderHeight;
        this.pickupLocName = pickupLocName;
        this.dropOffLocName = dropOffLocName;
    }

    public Order() {
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getPickupDate() {
        return pickupDate;
    }

    public void setPickupDate(String pickupDate) {
        this.pickupDate = pickupDate;
    }

    public String getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(String pickupTime) {
        this.pickupTime = pickupTime;
    }

    public String getGoodType() {
        return goodType;
    }

    public void setGoodType(String goodType) {
        this.goodType = goodType;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getPickupLatLong() {
        return pickupLatLong;
    }

    public void setPickupLatLong(String pickupLatLong) {
        this.pickupLatLong = pickupLatLong;
    }

    public String getDropOffLatLong() {
        return dropOffLatLong;
    }

    public void setDropOffLatLong(String dropOffLatLong) {
        this.dropOffLatLong = dropOffLatLong;
    }

    public float getOrderWeight() {
        return orderWeight;
    }

    public void setOrderWeight(float orderWeight) {
        this.orderWeight = orderWeight;
    }

    public float getOrderWidth() {
        return orderWidth;
    }

    public void setOrderWidth(float orderWidth) {
        this.orderWidth = orderWidth;
    }

    public float getOrderLength() {
        return orderLength;
    }

    public void setOrderLength(float orderLength) {
        this.orderLength = orderLength;
    }

    public float getOrderHeight() {
        return orderHeight;
    }

    public void setOrderHeight(float orderHeight) {
        this.orderHeight = orderHeight;
    }

    public String getPickupLocName() {
        return pickupLocName;
    }

    public void setPickupLocName(String pickupLocName) {
        this.pickupLocName = pickupLocName;
    }

    public String getDropOffLocName() {
        return dropOffLocName;
    }

    public void setDropOffLocName(String dropOffLocName) {
        this.dropOffLocName = dropOffLocName;
    }

    public void printOrder(){
        System.out.println(this.receiverName+", "+
        this.pickupDate+", "+
        this.pickupTime+", "+ this.pickupLocName+", "+ this.pickupLatLong+", "+
                this.dropOffLocName+", "+this.dropOffLocName+", "+
        this.goodType+", "+
        this.vehicleType+", "+
        this.orderWeight+", "+
        this.orderWidth+", "+
        this.orderLength+", "+
        this.orderHeight);
    }
}
