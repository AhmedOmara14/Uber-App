package com.example.uberapp.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.uberapp.R;
import com.example.uberapp.data.Repositry;
import com.example.uberapp.databinding.ActivityYallahBinding;
import com.example.uberapp.pojo.info;
import com.example.uberapp.pojo.location;
import com.example.uberapp.ui.DirectionHelper.FetchURL;
import com.example.uberapp.ui.DirectionHelper.TaskLoadedCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.security.AccessController.getContext;

public class yallah extends FragmentActivity implements OnMapReadyCallback , TaskLoadedCallback {
    ActivityYallahBinding binding;
    private String currentlocation, Latitude_currentlocation, Longitude_currentlocation, tolocation, Latitude_tolocation, Longitude_tolocation;
    Location location;
    Polyline polyline;
    GoogleMap mMap;
    FusedLocationProviderClient fusedLocationProviderClient;
    Repositry repositry;
    private static final String TAG = "yallah";
    FirebaseAuth auth = FirebaseAuth.getInstance();
    private static final int REQUEST_CODE = 101;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    float Distance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_yallah);
        currentlocation = getIntent().getExtras().get("currentlocation").toString();
        Latitude_currentlocation = getIntent().getExtras().get("Latitude_currentlocation").toString();
        Longitude_currentlocation = getIntent().getExtras().get("Longitude_currentlocation").toString();
        tolocation = getIntent().getExtras().get("tolocation").toString();
        Latitude_tolocation = getIntent().getExtras().get("Latitude_tolocation").toString();
        Longitude_tolocation = getIntent().getExtras().get("Longitude_tolocation").toString();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        repositry = ViewModelProviders.of(this).get(Repositry.class);
        Toast.makeText(this, currentlocation, Toast.LENGTH_SHORT).show();
        fetchlastlocation();
        binding.currentLocation.setText(currentlocation + "");
        binding.tolocation.setText(tolocation + "");
        binding.closeRequestP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference.child("Customer_Request").child(auth.getCurrentUser().getUid()).removeValue();
            }
        });
        binding.phoneP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + binding.phone.getText()));
                if (ActivityCompat.checkSelfPermission(yallah.this, Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(callIntent);
            }
        });

    }


    public void fetchlastlocation() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(yallah.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location loca_tion) {
                if (loca_tion != null) {
                    location = loca_tion;
                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_cus_yallah);
                    supportMapFragment.getMapAsync(yallah.this);
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap=googleMap;
        LatLng latLng = new LatLng(Double.parseDouble(Latitude_currentlocation), Double.parseDouble(Longitude_currentlocation));
        MarkerOptions markerOptions = new MarkerOptions().position(latLng)
                .title(currentlocation);
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        mMap.addMarker(markerOptions);

        LatLng latLng1 = new LatLng(Double.parseDouble(Latitude_tolocation), Double.parseDouble(Longitude_tolocation));
        MarkerOptions markerOptions1 = new MarkerOptions().position(latLng1)
                .title(tolocation);
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng1));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng1, 15));
        mMap.addMarker(markerOptions1);

        Location location2 = new Location("");
        Location location = new Location("");
        location.setLatitude(Double.parseDouble(Latitude_currentlocation));
        location.setLatitude(Double.parseDouble(Longitude_currentlocation));
        repositry.getData().observe(yallah.this, new Observer<List<com.example.uberapp.pojo.location>>() {
            @Override
            public void onChanged(List<location> class_items) {
                Toast.makeText(yallah.this, "ok", Toast.LENGTH_SHORT).show();
                for (int i = 0; i <= class_items.size(); i++) {
                    location2.setLatitude(class_items.get(i).getLatitude());
                    location2.setLongitude(class_items.get(i).getLongitude());
                    Distance = location.distanceTo(location2) / 100000;
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("Latitude_currentlocation", Latitude_currentlocation);
                    hashMap.put("Longitude_currentlocation", Longitude_currentlocation);
                    hashMap.put("Latitude_currentdriver", class_items.get(i).getLatitude());
                    hashMap.put("Longitude_currentdriver", class_items.get(i).getLongitude());
                    hashMap.put("id", auth.getCurrentUser().getUid());

                    Toast.makeText(yallah.this, Distance + "", Toast.LENGTH_SHORT).show();
                    binding.yalhah.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (Distance > 30) {
                                binding.cardReach.setVisibility(View.VISIBLE);
                                binding.DriverReached.setText("There is no car in this area");
                            } else {
                                binding.yalhah.setText("Driver Found: " + String.valueOf(Distance));
                                reference.child("Customer_Request").child(auth.getCurrentUser().getUid()).updateChildren(hashMap);
                                reference.child("Drivers").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                            Toast.makeText(yallah.this, dataSnapshot1.getKey(), Toast.LENGTH_SHORT).show();
                                            reference.child("Location_drivers").addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                                                        Toast.makeText(yallah.this, dataSnapshot2.getKey(), Toast.LENGTH_SHORT).show();
                                                        if (dataSnapshot1.getKey().equals(dataSnapshot2.getKey())) {

                                                            repositry.getData_driver(dataSnapshot1.getKey()).observe(yallah.this, new Observer<List<info>>() {
                                                                @Override
                                                                public void onChanged(List<info> infos) {
                                                                        binding.cardInfo.setVisibility(View.VISIBLE);
                                                                        binding.name.setText(infos.get(0).getName());
                                                                        binding.phone.setText(infos.get(0).getPhone());
                                                                        Picasso.get().load(infos.get(0).getImage()).placeholder(R.drawable.profile_image)
                                                                                .error(R.drawable.profile_image).into(binding.profileTolocation);

                                                                }
                                                            });
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                                LatLng DriverLatLng = new LatLng(location2.getLatitude(), location2.getLongitude());
                                MarkerOptions markerOptions2 = new MarkerOptions().position(DriverLatLng)
                                        .title("your driver is here").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher));
                                googleMap.animateCamera(CameraUpdateFactory.newLatLng(DriverLatLng));
                                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(DriverLatLng, 15));
                                googleMap.addMarker(markerOptions2);
                            }
                        }
                    });
                }
            }
        });

     //   String Url=getDirectionsUrl(latLng,latLng1);
      // new FetchURL(yallah.this).execute(Url,"driving");


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchlastlocation();
                }
                break;
        }
    }



    @Override
    public void onTaskDone(Object... values) {
        if (polyline != null)
            polyline.remove();
        polyline = mMap.addPolyline((PolylineOptions) values[0]);
    }



}
