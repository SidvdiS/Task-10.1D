package com.example.task61d;

import android.content.ContentValues;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.task61d.model.Truck;

import java.util.ArrayList;

public class RecyclerTrucksAdapter extends RecyclerView.Adapter<RecyclerTrucksAdapter.TrucksViewHolder> {

    Context context;
    ArrayList<Truck> trucks;

    public RecyclerTrucksAdapter(Context context, ArrayList<Truck> trucks){
        this.context = context;
        this.trucks = trucks;
    }

    @NonNull
    @Override
    public TrucksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.truck_row,parent,false);
        TrucksViewHolder trucksViewHolder = new TrucksViewHolder(view);
        return trucksViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TrucksViewHolder holder, int position) {
        holder.truckType.setText(trucks.get(position).getTruckType());
        holder.truckInfo.setText(trucks.get(position).getTruckInfo());
        holder.driverName.setText(trucks.get(position).getDriverName());
        holder.drivePhoneNo.setText(trucks.get(position).getPhoneNumber());
        holder.truckImg.setImageResource(trucks.get(position).getTruckImg());
    }

    @Override
    public int getItemCount() {
        return trucks.size();
    }


    public class TrucksViewHolder extends RecyclerView.ViewHolder{

        TextView truckType, truckInfo, driverName, drivePhoneNo;
        ImageView truckImg;

        public TrucksViewHolder(@NonNull View itemView) {
            super(itemView);
            truckType = itemView.findViewById(R.id.truck_type);
            truckInfo = itemView.findViewById(R.id.truck_info);
            driverName = itemView.findViewById(R.id.driver_name);
            drivePhoneNo = itemView.findViewById(R.id.driver_phone_no);
            truckImg = itemView.findViewById(R.id.truck_img);
        }
    }
}
