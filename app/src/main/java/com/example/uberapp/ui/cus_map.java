package com.example.uberapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uberapp.R;
import com.example.uberapp.data.Repositry;
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

public class cus_map extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration2;
    FirebaseAuth auth=FirebaseAuth.getInstance();
    FirebaseUser firebaseUser=auth.getCurrentUser();
    DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
    Repositry repositry ;
    String currentuserid;
    CircleImageView circleImageView;
    TextView navUsername,navphone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cus_map);
        Toolbar toolbar = findViewById(R.id.toolbar_l);
        setSupportActionBar(toolbar);

        if (firebaseUser == null) {
            Intent intent = new Intent(cus_map.this, login_customer.class);
            startActivity(intent);
        } else {
            String currentid=auth.getCurrentUser().getUid();
            reference.child("Customers").child(currentid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(!dataSnapshot.child("email").exists()) {
                        Intent intent=new Intent(cus_map.this,login_customer.class);
                        startActivity(intent);
                    }
                    else if(!dataSnapshot.child("name").exists()) {
                        Intent intent=new Intent(cus_map.this,setting_customer.class);
                        startActivity(intent);
                    }

                    else if(dataSnapshot.exists()&&dataSnapshot.child("name").exists()&&dataSnapshot.child("phone").exists()){
                        Toast.makeText(cus_map.this, "welcome", Toast.LENGTH_SHORT).show();
                        currentuserid = auth.getCurrentUser().getUid();
                        repositry = ViewModelProviders.of(cus_map.this).get(Repositry.class);
                        Toolbar toolbar = findViewById(R.id.toolbar_l);
                        setSupportActionBar(toolbar);


                        DrawerLayout drawer = findViewById(R.id.drawer_layout_);
                        NavigationView navigationView = findViewById(R.id.nav_view_cus);

                        mAppBarConfiguration2 = new AppBarConfiguration.Builder(
                                R.id.nav_home_cus)
                                .setDrawerLayout(drawer)
                                .build();
                        NavController navController = Navigation.findNavController(cus_map.this, R.id.nav_host_fragment_rr);
                        NavigationUI.setupActionBarWithNavController(cus_map.this, navController, mAppBarConfiguration2);
                        NavigationUI.setupWithNavController(navigationView, navController);
                        View headerView = navigationView.getHeaderView(0);

                        circleImageView = headerView.findViewById(R.id.profile_cus);
                        navUsername = (TextView) headerView.findViewById(R.id.name_customer);
                        navphone = (TextView) headerView.findViewById(R.id.phone_customer);

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
        repositry.getData_customer(currentuserid).observe(this, new Observer<List<info>>() {
            @Override
            public void onChanged(List<info> infos) {
                navUsername.setText(infos.get(0).getName());
                navphone.setText(infos.get(0).getPhone());
                Picasso.get().load(infos.get(0).getImage()).
                        placeholder(R.drawable.profile_image).error(R.drawable.profile_image).into(circleImageView);

            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.cus_map, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.ettings_cus) {
            Intent intent = new Intent(cus_map.this, setting_customer.class);
            startActivity(intent);
        }else if (item.getItemId() == R.id.action_settings_) {
            auth.signOut();
            Intent intent=new Intent(cus_map.this,login_customer.class);
            startActivity(intent);

        }
        return true;
    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_rr);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration2)
                || super.onSupportNavigateUp();
    }
}
