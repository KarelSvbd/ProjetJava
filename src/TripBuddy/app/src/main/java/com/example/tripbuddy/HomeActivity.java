/*
    Projet  : TripBuddy
    Desc.   : Créer une application qui accompagne un voyageur en voiture
                - Afficher la position de l'utilisateur sur une carte
                - Afficher la vitesse de l'utilisateur
                - Afficher certaines donnes meteo
    Version : 0.1
    Date    : 01.12.2021

    Auteurs : José Ferreira, Rui Mota, Karel Vilém Svoboda
    Classe  : I.DA-P4A / Atelier Smartphone

*/
package com.example.tripbuddy;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationRequest;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.*;

import java.text.DecimalFormat;

public class HomeActivity extends AppCompatActivity implements OnMyLocationButtonClickListener, OnMyLocationClickListener, OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback, LocationListener {
    private Button button;
    private Button btnProf;
    private LocationRequest locationRequest;
    private LocationManager locationManager;
    private Marker markerUser;
    private TextView lblTemperature;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean permissionDenied = false;

    private GoogleMap map;

    private LatLng postionUser = new LatLng(46.204391,6.143158);
    private Location positionUserLocal;

    Carte carte;

    MediaPlayer mp;
    int totalTime;
    private RequestQueue queue;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        btnProf = (Button) findViewById(R.id.btnProfil);
        lblTemperature = (TextView) findViewById(R.id.lblTempreature);


        btnProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openUser();
            }
        });
        //getSupportActionBar().hide();
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSettings();

            }
        });


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.maps);
        mapFragment.getMapAsync(this);

        if(ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(HomeActivity.this,new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, 100);
        }

        getLocation();



    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("MissingPermission")
    private void getLocation() {
        try{
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, HomeActivity.this);
        }catch (Exception e){
            System.out.print(e);
        }



    }

    //Ouvre la page des paramêtres
    public  void openSettings(){
        Intent intent = new Intent(this, Preference.class);
        startActivity(intent);
    }
    //Ouvre la page du modeR (non implémenté)
    public  void openModeR(){
        Intent intent = new Intent(this, ModeR.class);
        startActivity(intent);
    }
    //Ouvre la page openUser (non implémenté)
    public  void openUser(){
        Intent Intent2 = new Intent(this,UserActivity.class);
        startActivity(Intent2);
    }

    //S'execute après la creation de la map
    //Génére une carte et bouge la camera sur une position constante et y ajoute un marker
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        //Définition de la postion en haut dans l'initialisation du projet
        //Niveau de zoom élevé
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(postionUser, 5));
        map = googleMap;

        //Ajout tu marker
        markerUser = map.addMarker(new MarkerOptions()
                .position(postionUser)
                .title("Chargement"));

    }

    private void enableMyLocation() {
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {

    }

    //Se produit à chaque changement de localisation
    //Permet d'actualiser les variables de position de l'utlisateur
    //Permet de modifier l'icone de l'utlisateur
    //Permet de mettre à jour les données de la vitesse
    @Override
    public void onLocationChanged(@NonNull Location location) {

        postionUser = new LatLng(location.getLatitude(), location.getLongitude());
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(postionUser, 15));
        markerUser.remove();
        markerUser = map.addMarker(new MarkerOptions()
                .position(postionUser)
                .title("Votre position")
                .icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_baseline_directions_car_24)));
        TextView lblSpeed = findViewById(R.id.lblSpeed);

        positionUserLocal = location;

        lblSpeed.setText(String.valueOf(Math.round(location.getSpeed() * 3.6)));

        carte = new Carte(map, location);

        getDonnesMeteo();

        /*code pour API de meteo*/

        //Get de l'API de meteo
        //https://api.openweathermap.org/data/2.5/weather?lon=6.143158&lat=46.204391&appid=a4c7fb610faa96a45c7d9e50efa24f58

    }




    //Permet de chercher le Bitmap afin de changer l'icone de l'utilisateur
    private BitmapDescriptor bitmapDescriptorFromVector(Context context, Integer vectorResId){
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0,0,vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap= Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap((bitmap));
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }

    public void musiqueSuivante(View view){

    }


    public void recentrer(View view){
        //carte.recentrer(view);


    }

    public void modeR(View view){
        openModeR();
    }

    //Permet de faire un appel à l'API openweather
    public void getDonnesMeteo(){
        queue = Volley.newRequestQueue(this);

        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                "https://api.openweathermap.org/data/2.5/weather?lon=6.143158&lat=46.204391&appid=a4c7fb610faa96a45c7d9e50efa24f58",
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String APIresponse = response.toString();

                        //APIresponse =  APIresponse.substring(APIresponse.indexOf("\"temp\": ") + 1, APIresponse.indexOf(","));

                        System.out.println(APIresponse);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error.toString());
                    }
                }
        );

        queue.add(objectRequest);
    }



}