package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static FirebaseAuth mAuth;
    private FirebaseUser user;
    private static final String TAG = "Main_Activity";
    public static final String LIST_DEVICE = "device";
    byte[] receiveData = new byte[1024];
    String data;
    public static final String ID_USER = "123111";
    private GoogleSignInClient mGoogleSignInClient;
    public static String PATH;
    FirebaseDatabase database;
    DatabaseReference myRef;
    DeviceStatusAdapter deviceStatusAdapter;
    RecyclerView recyclerView ;
    ImageView ivEdit, ivAdd, ivLogout;
    EditText edNewDevice;
    TextView tvLable;
    Button btSave;
    ConstraintLayout ctAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        user =mAuth.getCurrentUser();
        if(user==null){
            Intent intent =new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent);
        }

        recyclerView =findViewById(R.id.recyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        ivAdd =findViewById(R.id.ivAdd);
        ivEdit =findViewById(R.id.ivEdit);
        ivLogout =findViewById(R.id.ivLogout);
        edNewDevice =findViewById(R.id.edNewDevice);
        tvLable =findViewById(R.id.tvLable);
        btSave=findViewById(R.id.btSave);
        ctAdd =findViewById(R.id.ctAdd);
        btSave.setOnClickListener(this);
        ivAdd.setOnClickListener(this);
        ivEdit.setOnClickListener(this);
        ivLogout.setOnClickListener(this);
        database = FirebaseDatabase.getInstance();
        PATH=user.getEmail().substring(0,user.getEmail().indexOf('@'));
        myRef =database.getReference(LIST_DEVICE).child(PATH);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Device> listDevice = new ArrayList<>();
                for(DataSnapshot deviceSnapshot:dataSnapshot.getChildren()){
                    Device device =deviceSnapshot.getValue(Device.class);
                    listDevice.add(0,device);
                }
                Log.d(TAG, "onDataChange: "+listDevice.size());
                deviceStatusAdapter = new DeviceStatusAdapter(listDevice);
                recyclerView.setAdapter(deviceStatusAdapter);
            }
            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivAdd:
                ctAdd.setVisibility(View.VISIBLE);
                recyclerView.setAlpha(0.3f);
                break;
            case R.id.ivEdit:
                new CheckStatusTask().execute();
                break;
            case R.id.btSave:
                recyclerView.setAlpha(1f);
                ctAdd.setVisibility(View.INVISIBLE);
                if(edNewDevice.getText().toString().length() !=0){
                    DatabaseReference pushedPostRef = myRef.push();
                    String postId = pushedPostRef.getKey();
                    pushedPostRef.setValue(new Device(1,postId,edNewDevice.getText().toString()));
                    deviceStatusAdapter.notifyDataSetChanged();
                    edNewDevice.setText("");
                }
                break;
            case R.id.ivLogout:
                mAuth.signOut();
                finish();
                break;

        }
    }

    @Override
    public void onBackPressed() {

    }
    private void sendUDPMessage(String msg,String msgOK) {
        try {
            Log.d("TESST",msg);
            DatagramSocket clientSocket = new DatagramSocket(9072);
            clientSocket.setBroadcast(true);
            InetAddress address = InetAddress.getByName("255.255.255.255");
            byte[] sendData;
            sendData = msg.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, 2709);
            clientSocket.send(sendPacket);
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            clientSocket.receive(receivePacket);
            data = new String(receivePacket.getData(), receivePacket.getOffset(), receivePacket.getLength(), "UTF-8");
            Log.d("TESST",data);
            clientSocket.close();
        } catch (Exception e) {
            Log.d("TESST",e.toString());
            e.printStackTrace();
        }

    }
    private class CheckStatusTask extends AsyncTask<Object, Object, Boolean> {
        protected Boolean doInBackground(Object... arg0) {
            sendUDPMessage(PATH +"@","12345RR");
            return true;
        }
        protected void onPostExecute(Boolean flag) {
            Log.d(TAG, "onPostExecute: "+"vao day  "+data);
            for(int i =0;i<2;i++){
                myRef = database.getReference(LIST_DEVICE).child(PATH).child(data +i);
                myRef.setValue(new Device(1,data+i,data+"name"));
                deviceStatusAdapter.notifyDataSetChanged();
                edNewDevice.setText("");
            }

        }
    }
}
