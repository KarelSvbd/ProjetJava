/*
    Projet  : TripBuddy
    Class   : Carte.java
    Desc.   : Permet de gérer la carte de l'application
    Version : 1
    Date    : 01.12.2021

    Auteur  : Karel Vilém Svoboda
    Classe  : I.DA-P4A / Atelier Smartphone

*/
package com.example.tripbuddy;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationRequest;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class Carte extends AppCompatActivity {

    GoogleMap _map;
    Location _location;

    public LatLng getLatLng(){
        return  new LatLng(_location.getLatitude(), _location.getLongitude());
    }

    //Retourne la vitesse de l'utilisateur en KM/H
    public double getSpeedByKMH(){
        return _location.getSpeed() * 3.6;
    }

    //Retourne la vitesse de l'utilisateur en MPH
    public double getSpeedByMPH(){
        return _location.getSpeed() * 2.23694;
    }

    //Permet de recentrer la carte sur la position de l'utilisateur
    public void recentrer(View view){
        _map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(_location.getLatitude(), _location.getLongitude()), 20));
    }

    public Carte(GoogleMap map, Location location){
        _map = map;
        _location = location;
    }
}
