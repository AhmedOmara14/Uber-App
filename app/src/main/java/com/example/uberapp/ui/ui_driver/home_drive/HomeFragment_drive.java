package com.example.uberapp.ui.ui_driver.home_drive;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;


import com.example.uberapp.R;
import com.example.uberapp.data.Repositry;
import com.example.uberapp.pojo.Customer_Request;
import com.example.uberapp.ui.login_driver;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

public class HomeFragment_drive extends Fragment implements OnMapReadyCallback {

    Location currentlocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    SupportMapFragment supportMapFragment;
    View root;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Location_drivers");
    FirebaseAuth auth = FirebaseAuth.getInstance();
    private static final int REQUEST_CODE = 101;
    CardView cardView;
    TextView name, phone;
    Repositry repositry;
    ImageView btn_close;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_home_drive, container, false);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        repositry = ViewModelProviders.of(this).get(Repositry.class);
        cardView = root.findViewById(R.id.card_info_cus);
        name = root.findViewById(R.id.name_cus);
        phone = root.findViewById(R.id.phone_cus);
        btn_close = root.findViewById(R.id.close_requestll);

        fetchlastlocation();
           btn_close.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   FirebaseDatabase.getInstance().getReference()
                           .child("Customer_Request").child(auth.getCurrentUser().getUid()).removeValue();
               }
           });

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (auth.getCurrentUser() == null) {
            Intent intent = new Intent(getContext(), login_driver.class);
            startActivity(intent);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng latLng = new LatLng(currentlocation.getLatitude(), currentlocation.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions().position(latLng)
                .title("I am here").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher));
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        googleMap.addMarker(markerOptions);
        FirebaseDatabase.getInstance().getReference().child("Customer_Request").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    String kk = currentlocation.getLatitude() + "";
                    String id = dataSnapshot1.child("id").getValue() + "";
                    String lat = dataSnapshot1.child("Latitude_currentdriver").getValue().toString();
                    String lon = dataSnapshot1.child("Longitude_currentdriver").getValue().toString();
                    if (kk.equals(lat)) {
                        if (lon.equals(currentlocation.getLongitude() + "")) {
                            cardView.setVisibility(View.VISIBLE);
                            FirebaseDatabase.getInstance().getReference().child("Customers").
                                    child(id).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    name.setText(dataSnapshot.child("email").getValue().toString());
                                    phone.setText(dataSnapshot.child("phone").getValue().toString());
                                    cardView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            LatLng latLng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lon));
                                            MarkerOptions markerOptions = new MarkerOptions().position(latLng)
                                                    .title("your customer is here");
                                            googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                                            googleMap.addMarker(markerOptions);
                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } else {
                        Toast.makeText(getContext(), "no", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void fetchlastlocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) getContext(), new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    try {
                        currentlocation = location;
                        Toast.makeText(getContext(), currentlocation.getLatitude() + " " +
                                currentlocation.getLongitude(), Toast.LENGTH_SHORT).show();
                        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
                        if (supportMapFragment == null) {
                            FragmentManager fm = getFragmentManager();
                            FragmentTransaction ft = fm.beginTransaction();
                            supportMapFragment = SupportMapFragment.newInstance();
                            ft.replace(R.id.map, supportMapFragment).commit();
                        }
                        supportMapFragment.getMapAsync(HomeFragment_drive.this);
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("Latitude", currentlocation.getLatitude());
                        hashMap.put("Longitude", currentlocation.getLongitude());
                        hashMap.put("type", "Driver");
                        reference.child(auth.getCurrentUser().getUid()).updateChildren(hashMap);

                    } catch (Exception e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        if (auth.getCurrentUser() == null) {
            Intent intent = new Intent(getContext(), login_driver.class);
            startActivity(intent);
        } else {
            reference.child(auth.getCurrentUser().getUid()).removeValue();
        }
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
}