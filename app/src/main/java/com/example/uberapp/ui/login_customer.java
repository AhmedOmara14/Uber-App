package com.example.uberapp.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.uberapp.R;
import com.example.uberapp.databinding.ActivityLoginCustomerBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class login_customer extends AppCompatActivity {
    ActivityLoginCustomerBinding binding;
    DatabaseReference reference;
    FirebaseAuth auth;
    ProgressDialog progressDialog;
    FirebaseUser firebaseUser;
    String email, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login_customer);
        intialiaztion();

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login_customer.this, customerordriver.class);
                startActivity(intent);
            }
        });
        binding.btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login_customer.this, customer_signup.class);
                startActivity(intent);
            }
        });
        binding.btnCustomer1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(login_customer.this, "We", Toast.LENGTH_SHORT).show();
                email = binding.emailCustomer.getText().toString();
                pass = binding.passCustomer.getText().toString();

                 progressDialog.setMax(10);
                  progressDialog.create();
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                      progressDialog.show();
                if (email.isEmpty()) {
                    Toast.makeText(login_customer.this, "Enter email", Toast.LENGTH_SHORT).show();
                } else if (pass.isEmpty()) {
                    Toast.makeText(login_customer.this, "Enter Password", Toast.LENGTH_SHORT).show();
                } else {

                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                String email1 = dataSnapshot1.child("email").getValue().toString();
                                String phone = dataSnapshot1.child("phone").getValue().toString();
                                String type = dataSnapshot1.child("type").getValue().toString();

                                if (type.equals("customer")) {
                                    if (email1.equals(email)) {
                                        Toast.makeText(login_customer.this, phone, Toast.LENGTH_SHORT).show();
                                             Intent intent = new Intent(login_customer.this, driver_map.class);
                                          intent.putExtra("name", email);
                                          intent.putExtra("phone", phone);
                                          startActivity(intent);
                                    } else {
                                        Toast.makeText(login_customer.this, "error", Toast.LENGTH_SHORT).show();
                                    }

                                } else {
                                    Toast.makeText(login_customer.this, "error", Toast.LENGTH_SHORT).show();
                                }
                                   progressDialog.dismiss();

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

            }});
                }

    public void intialiaztion() {
        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference().child("Customers");
        progressDialog = new ProgressDialog(login_customer.this);
        progressDialog.setMessage("Please Wait....");
        firebaseUser = auth.getCurrentUser();

    }
}
