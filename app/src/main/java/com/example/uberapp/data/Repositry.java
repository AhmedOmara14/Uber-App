package com.example.uberapp.data;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.uberapp.pojo.Customer_Request;
import com.example.uberapp.pojo.info;
import com.example.uberapp.pojo.location;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Repositry extends ViewModel {
    public static Repositry instance;
    private static final String TAG = "Repositry";
    ArrayList<location> list=new ArrayList<>();
    ArrayList<Customer_Request> list_cus=new ArrayList<>();
    ArrayList<info> list_info=new ArrayList<>();

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    public MutableLiveData<List<location>> listMutableLiveData=new MutableLiveData<>();
    public MutableLiveData<List<Customer_Request>> listMutableLiveData_cus=new MutableLiveData<>();
    public MutableLiveData<List<info>> listMutableLiveData_drive=new MutableLiveData<>();
    public MutableLiveData<List<info>> listMutableLiveData_customer=new MutableLiveData<>();

    public static Repositry getinstance(){
        if (instance==null){
            instance=new Repositry();
        }
        return instance;
    }

    public MutableLiveData<List<location>> getData() {
        reference.child("Location_drivers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    String type = dataSnapshot1.child("type").getValue().toString();
                    try {
                        location i = dataSnapshot1.getValue(location.class);
                        if (type.equals("Driver")) {
                            list.add(i);
                            listMutableLiveData.setValue(list);
                        }
                    }
                    catch (Exception e){
                        Log.d(TAG, "onDataChange: "+e.getMessage());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return listMutableLiveData;
    }
    public MutableLiveData<List<Customer_Request>> getData_Customer_Request() {
        FirebaseDatabase.getInstance().getReference().child("Customer_Request").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    try {
                        Customer_Request i = dataSnapshot1.getValue(Customer_Request.class);
                                list_cus.add(i);
                                listMutableLiveData_cus.setValue(list_cus);
                    }
                    catch (Exception e){
                        Log.d(TAG, "onDataChange: "+e.getMessage());
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return listMutableLiveData_cus;
    }

    public MutableLiveData<List<info>> getData_driver(String id) {
        Log.d(TAG, "getData_driver: "+id);
        reference.child("Drivers").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.hasChild("image")) {

                    String email = dataSnapshot.child("email").getValue().toString();
                    String name = dataSnapshot.child("name").getValue().toString();
                    String phone = dataSnapshot.child("phone").getValue().toString();
                    String carofdriver = dataSnapshot.child("carofdriver").getValue().toString();
                    String image = dataSnapshot.child("image").getValue().toString();

                    info i = new info(email, phone, name, image, carofdriver);
                    list_info.add(i);
                    listMutableLiveData_drive.setValue(list_info);

                }
                else if (dataSnapshot.exists() && dataSnapshot.hasChild("name")){
                    String email = dataSnapshot.child("email").getValue().toString();
                    String name = dataSnapshot.child("name").getValue().toString();
                    String phone = dataSnapshot.child("phone").getValue().toString();
                    String carofdriver = dataSnapshot.child("carofdriver").getValue().toString();
                    String image = "null";

                    info i = new info(email, phone, name, image, carofdriver);
                    list_info.add(i);
                    listMutableLiveData_drive.setValue(list_info);
                }
                else {
                    Log.d(TAG, "onDataChange: null");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return listMutableLiveData_drive;
    }
    public MutableLiveData<List<info>> getData_customer(String id) {
        Log.d(TAG, "getData_driver: "+id);
        reference.child("Customers").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.hasChild("image")) {

                    String email = dataSnapshot.child("email").getValue().toString();
                    String name = dataSnapshot.child("name").getValue().toString();
                    String phone = dataSnapshot.child("phone").getValue().toString();
                    String image = dataSnapshot.child("image").getValue().toString();

                    info i = new info(email, phone, name, image);
                    list_info.add(i);
                    listMutableLiveData_drive.setValue(list_info);

                }
                else if (dataSnapshot.exists() && dataSnapshot.hasChild("name")){
                    String email = dataSnapshot.child("email").getValue().toString();
                    String name = dataSnapshot.child("name").getValue().toString();
                    String phone = dataSnapshot.child("phone").getValue().toString();
                    String image = "null";

                    info i = new info(email, phone, name, image);
                    list_info.add(i);
                    listMutableLiveData_customer.setValue(list_info);
                }
                else {
                    Log.d(TAG, "onDataChange: null");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return listMutableLiveData_customer;
    }


}
