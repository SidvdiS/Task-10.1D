package com.example.task61d.util;

public class TruckUtil {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "truckDB";
    public static final String TABLE_NAME = "trucks";
    public static final String TRUCK_ID = "truckID";
    public static final String DRIVER_NAME = "driverName";
    public static final String PHONE_NO = "phoneNumber";
    public static final String TRUCK_TYPE = "truckType";
    public static final String TRUCK_INFO = "truckInfo";

    // Truck Types
    public static final String TYPE_VAN = "Van";
    public static final String TYPE_BOX = "Box Truck";
    public static final String TYPE_MINI = "Mini Truck";
    public static final String TYPE_TANKER = "Tanker Truck";
    public static final String TYPE_TOW = "Tow Truck";
    public static final String TYPE_LOG = "Logging Truck";
    public static final String TYPE_FLATBED = "Flatbed Truck";
    public static final String TYPE_REFRIGERATED = "Refrigerated Truck";

    // Truck Types Info
    public static final String INFO_BOX = "Transporting packages, furniture, or other goods.";
    public static final String INFO_VAN = "Small-scale deliveries or transporting smaller packages.";
    public static final String INFO_MINI = "Small-scale deliveries, urban logistics, and transportation of lightweight goods.";
    public static final String INFO_TANKER = "Delivering fuel, chemicals, or other hazardous materials.";
    public static final String INFO_TOW = "Transporting disabled or improperly parked vehicles.";
    public static final String INFO_LOG = "Transporting logs and timber from forested areas to sawmills or other processing facilities.";
    public static final String INFO_FLATBED = "Transporting large or bulky items.";
    public static final String INFO_REFRIGERATED = "Transporting perishable goods or items that require specific temperature control.";
}
