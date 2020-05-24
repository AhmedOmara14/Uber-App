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
import com.example.uberapp.databinding.ActivityRegisterDriverBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Register_driver extends AppCompatActivity {
    private DatabaseReference reference;
    private FirebaseAuth auth;
    private String email_reg,pass_reg,phone_reg;
    ActivityRegisterDriverBinding binding;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_register_driver);
        intialiaztion();
       binding.back.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent =new Intent(Register_driver.this,login_driver.class);
               startActivity(intent);
           }
       });
        binding.btnSignIn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent =new Intent(Register_driver.this,login_driver.class);
                startActivity(intent);
            }
        });
        binding.btnDriverSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email_reg=binding.emailDriverReg.getText().toString();
                pass_reg=binding.passDriverReg.getText().toString();
                phone_reg=binding.phoneDriverReg.getText().toString();


                progressDialog.setMax(10);
                progressDialog.create();
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();
                if (email_reg.isEmpty()){
                    Toast.makeText(Register_driver.this, "Enter Email", Toast.LENGTH_SHORT).show();
                }
                else  if (pass_reg.isEmpty()){
                    Toast.makeText(Register_driver.this, "Enter password", Toast.LENGTH_SHORT).show();
                }
                else {
                    auth.createUserWithEmailAndPassword(email_reg,pass_reg).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Toast.makeText(Register_driver.this, "Welcome", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            HashMap<String,Object> hashMap=new HashMap<>();
                            hashMap.put("email",email_reg);
                            hashMap.put("phone",phone_reg);
                            hashMap.put("type","driver");
                            reference.child(auth.getCurrentUser().getUid()).updateChildren(hashMap);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Register_driver.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    public void intialiaztion(){
        auth=FirebaseAuth.getInstance();
        reference= FirebaseDatabase.getInstance().getReference().child("Drivers");
         progressDialog = new ProgressDialog(Register_driver.this);
        progressDialog.setMessage("Please Wait....");

    }
}
