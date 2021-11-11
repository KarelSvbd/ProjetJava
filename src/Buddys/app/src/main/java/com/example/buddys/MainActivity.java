package com.example.buddys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

public class MainActivity extends AppCompatActivity{

    private Button button;
    private Button buttonAllerCarte;
    FusedLocationProviderClient fusedLocationProviderClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("test");



        /*button = (Button) findViewById(R.id.btnMenu);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View v){
                //openSpotifyActivity();
            }
        });

        buttonAllerCarte = (Button) findViewById(R.id.btnAllerCarte);
        buttonAllerCarte.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View v){
                //openActivityMap();
            }
        });*/


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void nextMusic(){

    }

    public void openSpotifyActivity(){
        Intent intent = new Intent(this, SpotifyActivity.class);
        startActivity(intent);
    }

    public void openActivityMap(){
        Intent map = new Intent(this, Activity_map.class);
        startActivity(map);
    }

    public void updtadeGPS(){
        final int PERSOMISSION_FINE_LOCATION = 99;
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

                fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        //Si l'utlisateur accepte la geolocalisation


                    }
                });
        }
        else{
            //Si la permition n'est pas accordée
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions((new String[] {Manifest.permission.ACCESS_FINE_LOCATION}), PERSOMISSION_FINE_LOCATION);
            }

        }
    }

}