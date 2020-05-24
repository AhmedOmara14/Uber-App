package com.example.uberapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.uberapp.R;
import com.example.uberapp.databinding.ActivityCustomerordriverBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class customerordriver extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCustomerordriverBinding binding= DataBindingUtil.setContentView(this,R.layout.activity_customerordriver);

        binding.btnCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(customerordriver.this,cus_map.class);
                startActivity(intent);
            }
        });
        binding.btnDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(customerordriver.this,driver_map.class);
                startActivity(intent);
            }
        });
    }
}
