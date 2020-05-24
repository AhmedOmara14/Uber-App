package com.example.uberapp.ui.ui.home;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.uberapp.R;
import com.example.uberapp.data.Repositry;
import com.example.uberapp.pojo.location;
import com.example.uberapp.ui.login_customer;
import com.example.uberapp.ui.yallah;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class HomeFragment extends Fragment implements OnMapReadyCallback {

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Location_customers");
    FirebaseAuth auth = FirebaseAuth.getInstance();
    Location currentlocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    SupportMapFragment supportMapFragment;
    android.widget.SearchView searchView;
    Button confirm;
    ImageView gg;
    DatePickerDialog.OnDateSetListener onDateSetListener;
    private static final int REQUEST_CODE = 101;
    Repositry repositry;
    int year_,month_,day_;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        searchView = root.findViewById(R.id.search_location);

        confirm = root.findViewById(R.id.confirm);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        repositry = ViewModelProviders.of(this).get(Repositry.class);
        fetchlastlocation();


        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                year_=year;
                month_=month;
                day_=dayOfMonth;
                Toast.makeText(getContext(), year_ + "/" + month_ + "/" + day_, Toast.LENGTH_SHORT).show();
            }
        };

        return root;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng latLng = new LatLng(currentlocation.getLatitude(), currentlocation.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions().position(latLng)
                .title("I am here");
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        googleMap.addMarker(markerOptions);
        List<Address> addresses = null;
        Geocoder geocoder = new Geocoder(getContext());
        try {
            addresses = geocoder.getFromLocation(currentlocation.getLatitude(), currentlocation.getLongitude(), 1);
        } catch (IOException ex) {
            ex.printStackTrace();
        }


        // curr_location.setText(addresses.get(0).getCountryName()+","+addresses.get(0).getFeatureName()+","+addresses.get(0).getAdminArea());

        repositry.getData().observe((LifecycleOwner) getContext(), new Observer<List<location>>() {
            @Override
            public void onChanged(List<location> class_items) {
                for (int i = 0; i <= class_items.size(); i++) {
                    LatLng latLng1 = new LatLng(class_items.get(i).getLatitude(), class_items.get(i).getLongitude());
                    MarkerOptions markerOptions1 = new MarkerOptions().position(latLng1)
                            .title("I am here").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher));
                    googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng1));
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng1, 15));
                    googleMap.addMarker(markerOptions1);
                }
            }

        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                {
                    String location = searchView.getQuery().toString();
                    List<Address> addresses = null;
                    if (location != null || !location.equals("")) {
                        Geocoder geocoder = new Geocoder(getContext());
                        try {
                            addresses = geocoder.getFromLocationName(location, 1);

                        } catch (Exception e) {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        Address address = addresses.get(0);
                        LatLng latLng2 = new LatLng(address.getLatitude(), address.getLongitude());
                        MarkerOptions markerOptions2 = new MarkerOptions().position(latLng2)
                                .title(location);
                        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng2));
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng2, 15));
                        googleMap.addMarker(markerOptions2);



                        confirm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getContext(), yallah.class);
                                List<Address> addresses = null;
                                try {
                                    addresses = geocoder.getFromLocation(currentlocation.getLatitude(), currentlocation.getLongitude(), 1);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                intent.putExtra("currentlocation", addresses.get(0).getCountryName() + ","
                                        + addresses.get(0).getFeatureName() + "," +
                                        addresses.get(0).getAdminArea());
                                intent.putExtra("Latitude_currentlocation", currentlocation.getLatitude() + "");
                                intent.putExtra("Longitude_currentlocation", currentlocation.getLongitude() + "");
                                intent.putExtra("tolocation", address.getCountryName() + "," + location);
                                intent.putExtra("Latitude_tolocation", address.getLatitude() + "");
                                intent.putExtra("Longitude_tolocation", address.getLongitude() + "");
                                intent.putExtra("Longitude_tolocation", address.getLongitude() + "");
     //                          intent.putExtra("Longitude_tolocation",  year_ + "/" + month_ + "/" + day_);
                                startActivity(intent);
                            }
                        });

                    }

                    return false;
                }


            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    public void fetchlastlocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
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
                        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_cus);
                        if (supportMapFragment == null) {
                            FragmentManager fm = getFragmentManager();
                            FragmentTransaction ft = fm.beginTransaction();
                            supportMapFragment = SupportMapFragment.newInstance();
                            ft.replace(R.id.map, supportMapFragment).commit();
                        }
                        supportMapFragment.getMapAsync(HomeFragment.this);
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("Latitude", currentlocation.getLatitude());
                        hashMap.put("Longitude", currentlocation.getLongitude());
                        hashMap.put("type", "Customer");
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
            Intent intent = new Intent(getContext(), login_customer.class);
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
