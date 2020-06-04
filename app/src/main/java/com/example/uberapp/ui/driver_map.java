package com.example.uberapp.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uberapp.R;
import com.example.uberapp.data.Repositry;
import com.example.uberapp.databinding.ActivityDriverMapBinding;
import com.example.uberapp.databinding.ActivitySettingsBinding;
import com.example.uberapp.pojo.info;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class driver_map extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    FirebaseUser firebaseUser;
    FirebaseAuth auth=FirebaseAuth.getInstance();
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    ActivityDriverMapBinding binding;
    private Repositry repositry;
    private String currentuserid;
    private View headerView;
    private TextView navUsername, navphone;
    private CircleImageView circleImageView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_driver_map);
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();

        if (firebaseUser ==null) {
            Intent intent = new Intent(driver_map.this, login_driver.class);
            startActivity(intent);
        } else {
            String currentid=auth.getCurrentUser().getUid();
            reference.child("Drivers").child(currentid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if(!dataSnapshot.child("email").exists()) {
                        Intent intent=new Intent(driver_map.this,login_driver.class);
                        startActivity(intent);
                    }
                    else if(!dataSnapshot.child("name").exists()) {
                        Intent intent=new Intent(driver_map.this,settings.class);
                        startActivity(intent);
                    }

                    else if(dataSnapshot.exists()&&dataSnapshot.child("name").exists()&&dataSnapshot.child("phone").exists()){
                        Toast.makeText(driver_map.this, "welcome", Toast.LENGTH_SHORT).show();
                        currentuserid = auth.getCurrentUser().getUid();
                        repositry = ViewModelProviders.of(driver_map.this).get(Repositry.class);
                        Toolbar toolbar = findViewById(R.id.toolbar);
                        setSupportActionBar(toolbar);

                        DrawerLayout drawer = findViewById(R.id.drawer_layout);
                        NavigationView navigationView = findViewById(R.id.nav_view);

                        mAppBarConfiguration = new AppBarConfiguration.Builder(
                                R.id.nav_home)
                                .setDrawerLayout(drawer)
                                .build();
                        NavController navController = Navigation.findNavController(driver_map.this, R.id.nav_host_fragment);
                        NavigationUI.setupActionBarWithNavController(driver_map.this, navController, mAppBarConfiguration);
                        NavigationUI.setupWithNavController(navigationView, navController);

                         headerView = navigationView.getHeaderView(0);
                        circleImageView = headerView.findViewById(R.id.profile_drive);
                        navUsername = (TextView) headerView.findViewById(R.id.name_driver);
                        navphone = (TextView) headerView.findViewById(R.id.phone_driver);
                        retreiveinfo();
                    }

                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }

    public void retreiveinfo() {
        Toast.makeText(this, "ok ", Toast.LENGTH_SHORT).show();
        repositry.getData_driver(currentuserid).observe(this, new Observer<List<info>>() {
            @Override
            public void onChanged(List<info> infos) {
                navUsername.setText(infos.get(0).getName());
                navphone.setText(infos.get(0).getPhone());
                Picasso.get().load(infos.get(0).getImage()).placeholder(R.drawable.profile_image).error(R.drawable.profile_image).into(circleImageView);

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.driver_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.ettings) {
            Intent intent = new Intent(driver_map.this, settings.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.action_settings) {
            auth.signOut();
            Intent intent = new Intent(driver_map.this, login_driver.class);
            startActivity(intent);

        }

        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}


