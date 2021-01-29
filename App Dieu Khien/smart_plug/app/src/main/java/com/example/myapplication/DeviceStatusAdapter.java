package com.example.myapplication;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.example.myapplication.MainActivity.LIST_DEVICE;
import static com.example.myapplication.MainActivity.PATH;
public class DeviceStatusAdapter extends RecyclerView.Adapter<DeviceStatusAdapter.DeviceStatusHolder> {
    List<Device> deviceList = new ArrayList<>();

    public DeviceStatusAdapter(List<Device> deviceList) {
        this.deviceList = deviceList;
    }

    @NonNull
    @Override
    public DeviceStatusHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater =LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_button,parent,false);
        return new DeviceStatusHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceStatusHolder holder, int position) {
        holder.setData(deviceList.get(position));
    }

    @Override
    public int getItemCount() {
        return deviceList.size();
    }


    public class DeviceStatusHolder extends RecyclerView.ViewHolder{
        public TextView tvDevice,tvStatus, tvTime;
        public ImageView ivStatus;
        public DeviceStatusHolder(View itemView) {
            super(itemView);
            tvDevice =itemView.findViewById(R.id.tvDevice);
            tvStatus =itemView.findViewById(R.id.tvStatus);
            tvTime=itemView.findViewById(R.id.tvTime);
            ivStatus =itemView.findViewById(R.id.ivStatus);
        }
        public void setData(final Device data){
            tvDevice.setText(data.getNameDevice());
            chooseImage(data.getStatus());
            ivStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    data.setStatus(1-data.getStatus());
                    chooseImage(data.getStatus());
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference(LIST_DEVICE).child(PATH);
                    myRef.child(data.getIdDevice()).setValue(new Device(data.getStatus(), data.getIdDevice(), data.getNameDevice()));

                }
            });


        }
        @SuppressLint("NewApi")
        private void chooseImage(int choose){
            Date currentTime = Calendar.getInstance().getTime();
            if(choose == 0 ) {
                tvStatus.setText("Trạng thái : Bật");
                //tvTime.setText("Hoạt động từ: "+currentTime);
                itemView.setBackgroundResource(R.drawable.customboder_on);
                Picasso.get().load(R.drawable.turn_off).into(ivStatus);
            }
            else {
                tvStatus.setText("Trạng thái : Tắt");
               // tvTime.setText("Tắt từ: "+currentTime);
                itemView.setBackgroundResource(R.drawable.customboder);
                Picasso.get().load(R.drawable.turn_on).into(ivStatus);
            }
        }
    }
}
