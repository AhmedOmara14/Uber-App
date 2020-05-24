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
import com.example.uberapp.databinding.ActivityLoginDriverBinding;
import com.example.uberapp.databinding.ActivityRegisterDriverBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class login_driver extends AppCompatActivity {
    private DatabaseReference reference;
    private FirebaseAuth auth;
    String email_login, pass_login;
    private ProgressDialog progressDialog;
    FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityLoginDriverBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_login_driver);
        intialiaztion();

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login_driver.this, customerordriver.class);
                startActivity(intent);
            }
        });
        binding.btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login_driver.this, Register_driver.class);
                startActivity(intent);
            }
        });

        binding.btnCustomer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                email_login = binding.editText.getText().toString();
                pass_login = binding.editText2.getText().toString();

                progressDialog.setMax(10);
                progressDialog.create();
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();
                if (email_login.isEmpty()) {
                    Toast.makeText(login_driver.this, "Enter email", Toast.LENGTH_SHORT).show();
                } else if (pass_login.isEmpty()) {
                    Toast.makeText(login_driver.this, "Enter Password", Toast.LENGTH_SHORT).show();
                } else {
                    auth.signInWithEmailAndPassword(email_login, pass_login).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            reference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                        String email = dataSnapshot1.child("email").getValue().toString();
                                        String phone = dataSnapshot1.child("phone").getValue().toString();
                                        String type = dataSnapshot1.child("type").getValue().toString();
                                        if (type.equals("driver")) {
                                            if (email_login.equals(email)) {
                                                Toast.makeText(login_driver.this, phone, Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(login_driver.this, driver_map.class);
                                                startActivity(intent);
                                            }
                                        }
                                        progressDialog.dismiss();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    });
                }
            }
        });
    }


    public void intialiaztion() {
        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference().child("Drivers");
        progressDialog = new ProgressDialog(login_driver.this);
        progressDialog.setMessage("Please Wait....");
        firebaseUser = auth.getCurrentUser();

    }
}
