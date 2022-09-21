package com.example.criminalalertapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class location extends AppCompatActivity {
    Button location,next,save,call;
    TextView view1, view2, view3, view4, view5;
    FusedLocationProviderClient fusedLocationProviderClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        location = findViewById(R.id.btngetloc);
        view1 = findViewById(R.id.txt1);
        view2 = findViewById(R.id.txt2);
        view3 = findViewById(R.id.txt3);
        view4 = findViewById(R.id.txt4);
        view5 = findViewById(R.id.txt5);
        next=findViewById(R.id.btnnext);
        call=findViewById(R.id.call);
        //save=findViewById(R.id.save);
        //initialize fuselocationprovider clied
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//check permission
                if (ActivityCompat.checkSelfPermission(location.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    //when permission granted
                    getLocation();

                } else {
                    //when permission denied
                    ActivityCompat.requestPermissions(location.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                }
            }
        });
    }
    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                // initialize loaction
                Location location = task.getResult();
                if (location != null) {
                    //initialize geocoder
                    Geocoder geocoder = new Geocoder(location.this, Locale.getDefault());
                    //initialize address list
                    try {
                        List<Address> addresses = geocoder.getFromLocation(
                                location.getLatitude(), location.getLongitude(), 1
                        );
                        //setlatitute text
                        view1.setText(Html.fromHtml(
                                "<font color='#6200EE'><b>Latitude:</b><br></font>"+addresses.get(0).getLatitude()
                        ));
                        //set longitude
                        view2.setText(Html.fromHtml(
                                "<font color='#6200EE'><b>Longitude:</b><br></font>"+addresses.get(0).getLongitude()
                        ));
                        //set country name
                        view3.setText(Html.fromHtml(
                                "<font color='#6200EE'><b>Country Name:</b><br></font>"+addresses.get(0).getCountryName()
                        ));

                        //set locality
                        view4.setText(Html.fromHtml(
                                "<font color='#6200EE'><b>Locality:</b><br></font>"+addresses.get(0).getLocality()
                        ));
                        //set address
                        view5.setText(Html.fromHtml(
                                "<font color='#6200EE'><b>Address:</b><br></font>"+addresses.get(0).getAddressLine(0)
                        ));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String la=view1.getText().toString();
                String lon=view2.getText().toString();
                String country=view3.getText().toString();
                String locality=view4.getText().toString();
                String address=view5.getText().toString();
                Intent intent=new Intent(location.this,send.class);
                intent.putExtra("keyla",la);
                intent.putExtra("keylon",lon);
                intent.putExtra("keycountry",country);
                intent.putExtra("keylocality",locality);
                intent.putExtra("keyaddress",address);
                startActivity(intent);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(location.this,map.class);
                startActivity(intent);
            }
        });
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:0715479382"));
                startActivity(intent);
            }
        });
    }
    }
