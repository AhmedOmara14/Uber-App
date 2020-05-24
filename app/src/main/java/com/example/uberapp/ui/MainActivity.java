package com.example.uberapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;

import com.example.uberapp.R;
import com.example.uberapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding= DataBindingUtil.setContentView(this,R.layout.activity_main);
        SystemClock.sleep(3000);

        Intent intent =new Intent(MainActivity.this,customerordriver.class);
        startActivity(intent);
    }
}
