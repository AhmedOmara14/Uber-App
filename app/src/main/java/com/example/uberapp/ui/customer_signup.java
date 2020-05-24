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
import com.example.uberapp.databinding.ActivityCustomerSignupBinding;
import com.example.uberapp.databinding.ActivityLoginCustomerBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class customer_signup extends AppCompatActivity {
    ActivityCustomerSignupBinding binding;
    DatabaseReference reference;
    FirebaseAuth auth;
    ProgressDialog progressDialog;
    FirebaseUser firebaseUser;
    String email,pass,phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding= DataBindingUtil.setContentView(this,R.layout.activity_customer_signup);
       intialiaztion();
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(customer_signup.this,customerordriver.class);
                startActivity(intent);
            }
        });
        binding.btnCusSignIn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent =new Intent(customer_signup.this,login_customer.class);
                startActivity(intent);
            }
        });
        binding.btnCustomerSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email=binding.emailCustomerReg.getText().toString();
                pass=binding.phoneCustomerReg.getText().toString();
                phone=binding.phoneCustomerReg.getText().toString();


                progressDialog.setMax(10);
                progressDialog.create();
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();
                if (email.isEmpty()){
                    Toast.makeText(customer_signup.this, "Enter Email", Toast.LENGTH_SHORT).show();
                }
                else  if (pass.isEmpty()){
                    Toast.makeText(customer_signup.this, "Enter password", Toast.LENGTH_SHORT).show();
                }
                else {
                    auth.createUserWithEmailAndPassword(email,pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Toast.makeText(customer_signup.this, "Welcome", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            HashMap<String,Object> hashMap=new HashMap<>();
                            hashMap.put("email",email);
                            hashMap.put("phone",phone);
                            hashMap.put("type","customer");
                            reference.child(auth.getCurrentUser().getUid()).updateChildren(hashMap);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(customer_signup.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    public void intialiaztion(){
        auth=FirebaseAuth.getInstance();
        reference= FirebaseDatabase.getInstance().getReference().child("Customers");
        progressDialog = new ProgressDialog(customer_signup.this);
        progressDialog.setMessage("Please Wait....");

    }
}
