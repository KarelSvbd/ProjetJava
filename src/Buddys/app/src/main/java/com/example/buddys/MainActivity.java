package com.example.buddys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class MainActivity extends AppCompatActivity {

    private Button button;
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Map nouvelleMap = new Map();
        uptadeGPS();
        button = (Button) findViewById(R.id.btnMenu);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View v){
                openSpotifyActivity();
            }
        });
    }

    public void nextMusic(){

    }

    public void openSpotifyActivity(){
        Intent intent = new Intent(this, SpotifyActivity.class);
        startActivity(intent);
    }

    public void uptadeGPS(){
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

            //todo
        }
    }
}