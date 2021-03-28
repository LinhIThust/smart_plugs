package com.example.myapplication;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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


    public class DeviceStatusHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView tvDevice,tvStatus, tvTime;
        public ImageView ivStatus;
        public Device dv;
        public DeviceStatusHolder(View itemView) {
            super(itemView);
            tvDevice =itemView.findViewById(R.id.tvDevice);
            tvStatus =itemView.findViewById(R.id.tvStatus);
//            tvTime=itemView.findViewById(R.id.tvTime);
            ivStatus =itemView.findViewById(R.id.ivStatus);
        }
        public void setData(final Device data){
            dv = data;
            tvDevice.setText(data.getNameDevice());
            chooseImage(data.getStatus());
            tvDevice.setOnClickListener(this);
            ivStatus.setOnClickListener(this);
        }
        @SuppressLint("NewApi")
        private void chooseImage(int choose){
            Date currentTime = Calendar.getInstance().getTime();
            if(choose == 0 ) {
                tvStatus.setText("Trạng thái : Bật");
                //tvTime.setText("Hoạt động từ: "+currentTime);
                itemView.setBackgroundResource(R.drawable.customboder_on);
                Picasso.get().load(R.drawable.off).into(ivStatus);
            }
            else {
                tvStatus.setText("Trạng thái : Tắt");
               // tvTime.setText("Tắt từ: "+currentTime);
                itemView.setBackgroundResource(R.drawable.customboder);
                Picasso.get().load(R.drawable.on).into(ivStatus);
            }
        }

        @Override
        public void onClick(View v) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference(LIST_DEVICE).child(PATH);
            switch (v.getId()){
                case R.id.tvDevice:
                    dv.setNameDevice("Quạt");
                    Log.d("zzzA", "onClick: ");
                    myRef.child(dv.getIdDevice()).setValue(new Device(dv.getStatus(), dv.getIdDevice(), dv.getNameDevice()));
                    break;
                case R.id.ivStatus:
                    dv.setStatus(1-dv.getStatus());
                    chooseImage(dv.getStatus());
                    myRef.child(dv.getIdDevice()).setValue(new Device(dv.getStatus(), dv.getIdDevice(), dv.getNameDevice()));
                    break;
            }
        }
    }
}
