package com.example.task61d.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.task61d.R;
import com.example.task61d.model.Truck;
import com.example.task61d.util.TruckUtil;

import java.util.ArrayList;

public class TruckDatabaseHelper extends SQLiteOpenHelper {

    public TruckDatabaseHelper(Context context) {
        super(context, TruckUtil.DATABASE_NAME, null, TruckUtil.DATABASE_VERSION);
    }

    private static final String CREATE_DATABASE_TABLE = "CREATE TABLE " + TruckUtil.TABLE_NAME +"("
                                                    + TruckUtil.TRUCK_ID +" INTEGER PRIMARY KEY AUTOINCREMENT , "
                                                    + TruckUtil.DRIVER_NAME + " TEXT , " + TruckUtil.PHONE_NO + " TEXT , "
                                                    + TruckUtil.TRUCK_TYPE + " TEXT ," + TruckUtil.TRUCK_INFO + " TEXT)";
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(CREATE_DATABASE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TruckUtil.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public long insertTruck(Truck truck){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(TruckUtil.DRIVER_NAME, truck.getDriverName());
        contentValues.put(TruckUtil.PHONE_NO, truck.getPhoneNumber());
        contentValues.put(TruckUtil.TRUCK_TYPE, truck.getTruckType());
        contentValues.put(TruckUtil.TRUCK_INFO, truck.getTruckInfo());

        long insert = db.insert(TruckUtil.TABLE_NAME, null,contentValues);
        return insert;
    }

    public int trucksAvail(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+ TruckUtil.TABLE_NAME,null);
        return cursor.getCount();
    }

    public ArrayList<Truck> getAllTrucks(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+ TruckUtil.TABLE_NAME,null);
        ArrayList<Truck> trucks = new ArrayList<>();
        while(cursor.moveToNext()){
            Truck truck = new Truck();
            truck.setDriverName(cursor.getString(1));
            truck.setPhoneNumber(cursor.getString(2));
            truck.setTruckType(cursor.getString(3));
            String type = cursor.getString(3);
            switch (type){
                case TruckUtil.TYPE_VAN:
                    truck.setTruckImg(R.drawable.van);
                    break;
                case TruckUtil.TYPE_BOX:
                    truck.setTruckImg(R.drawable.box_truck);
                    break;
                case TruckUtil.TYPE_FLATBED:
                    truck.setTruckImg(R.drawable.flat_bed_truck);
                    break;
                case TruckUtil.TYPE_LOG:
                    truck.setTruckImg(R.drawable.log_truck);
                    break;
                case TruckUtil.TYPE_REFRIGERATED:
                    truck.setTruckImg(R.drawable.refrigerated_truck);
                    break;
                case TruckUtil.TYPE_TANKER:
                    truck.setTruckImg(R.drawable.tanker_truck);
                    break;
                case TruckUtil.TYPE_TOW:
                    truck.setTruckImg(R.drawable.tow_truck);
                    break;
                case TruckUtil.TYPE_MINI:
                default:
                    truck.setTruckImg(R.drawable.mini_truck);
            }
            truck.setTruckInfo(cursor.getString(4));
            trucks.add(truck);
        }
        cursor.close();
        return trucks;
    }

    public Truck getTruckByType(String truckType){
        SQLiteDatabase db= this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+TruckUtil.TABLE_NAME+ " WHERE "+ TruckUtil.TRUCK_TYPE+"=?",new String[]{truckType});
        Truck truck = new Truck();
        while(cursor.moveToNext()){
            truck.setDriverName(cursor.getString(1));
            truck.setPhoneNumber(cursor.getString(2));
            truck.setTruckType(cursor.getString(3));
            truck.setTruckInfo(cursor.getString(4));
        }
        return truck;
    }
}
