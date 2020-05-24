package com.example.uberapp.data;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.uberapp.pojo.Customer_Request;
import com.example.uberapp.pojo.info;
import com.example.uberapp.pojo.location;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.squareup.okhttp.internal.Internal.instance;

public class Repositry extends ViewModel {
    public static Repositry instance;
    private static final String TAG = "Repositry";
    ArrayList<location> list=new ArrayList<>();
    ArrayList<Customer_Request> list_cus=new ArrayList<>();

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    public MutableLiveData<List<location>> listMutableLiveData=new MutableLiveData<>();
    public MutableLiveData<List<Customer_Request>> listMutableLiveData_cus=new MutableLiveData<>();
    public MutableLiveData<List<info>> listMutableLiveData_drive=new MutableLiveData<>();

    FirebaseAuth auth=FirebaseAuth.getInstance();
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

}
