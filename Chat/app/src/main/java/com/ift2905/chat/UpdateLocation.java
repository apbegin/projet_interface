package com.ift2905.chat;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by Poste on 21/04/2016.
 */
public class UpdateLocation extends AppCompatActivity {

    public static Boolean zoneHasChanged(GPSTracker gps){

        double latitude = gps.getLatitude();
        double longitude = gps.getLongitude();
        int currentLocation = LocZone.findCurrentZone(longitude, latitude);

        if(LocZone.getCurrentZone() == currentLocation){
            return false;
        }

        LocZone.setCurrentZone(currentLocation);
        return true;

    }

    public String showStats(GPSTracker gps) {

        String output = "";

        if(gps.canGetLocation()){
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            //Show Location:
            output = "Location: \nLat: " + latitude +
                    "\nLon: " + longitude + "\nZone: " + LocZone.getCurrentZone() +
                    "\nNW: " + LocZone.getZoneCoord(longitude, latitude)[0][0] + " ; " + LocZone.getZoneCoord(longitude, latitude)[0][1] +
                    "\nSE: " + LocZone.getZoneCoord(longitude, latitude)[3][0] + " ; " + LocZone.getZoneCoord(longitude, latitude)[3][1] +
                    "\nChanged Location: " + UpdateLocation.zoneHasChanged(gps);

        }else{
            gps.showSettingsAlert();
        }

        return output;
    }
}
