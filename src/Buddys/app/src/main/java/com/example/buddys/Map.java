/*  Class   : Map
    Desc.   : Permet de gérer les fonctions de la carte
    Projet  : TripBuddys
    Auteur  : Karel Vilém Svoboda
*/
package com.example.buddys;

//Importations de class

//Api de Google qui permet d'acceder aux services de localisation
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;


public class Map {

    LocationRequest locationRequest;
    Integer _lat = 0;
    Integer _long = 0;

    public Integer getLat() {
        return _lat;
    }

    public Integer getLong() {
        return _long;
    }


    public static final int DEFAULT_UPDATE_INTERVAL = 30;
    public static final int FAST_UPDATE_INTERVAL = 5;

    public Map(){
        // définition des propriétées de LocationRequest
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000 * DEFAULT_UPDATE_INTERVAL);
        locationRequest.setFastestInterval(1000 * FAST_UPDATE_INTERVAL);
        definitionPriorite(2);
    }

    //Permet de définir la précision en fonction de la puissance de l'appareil
    //Retourne le texte avec l'utlisation de la batterie
    public String definitionPriorite(Integer puissance){
        String result = "";
        switch(puissance){
            case 0:
                locationRequest.setPriority(LocationRequest.PRIORITY_NO_POWER);
                result = "pas d'utilisation de la batterie";
                break;
            case 1:
                locationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);
                result = "utilisation basse de la batterie";
                break;
            case 2:
                locationRequest.setPriority(locationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
                result = "utilisation modérée de la batterie";
                break;
            case 3:
                locationRequest.setPriority(locationRequest.PRIORITY_HIGH_ACCURACY);
                result = "utilisation élevée de la batterie";
                break;

        }
        return result;
    }







}
