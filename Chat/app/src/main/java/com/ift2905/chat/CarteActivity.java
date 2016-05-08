package com.ift2905.chat;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

public class CarteActivity extends FragmentActivity implements OnMapReadyCallback {

    GoogleMap zoneMap;
    GPSTracker tracker;
    TextView title;
    String nomDeZone = "";
    String staticTitle = "Vous êtes dans la zone: ";
    String fullTitle = "";
    String purgatoire = "Vous êtes dans le Purgatoire";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carte);

        tracker = new GPSTracker(this);

        //Référence au textview dans le layout
        title = (TextView)findViewById(R.id.textView2);
        //Initialisation du fragment de map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Déterminer la zone de l'utilsiateur
        UpdateLocation.zoneHasChanged(tracker);
        nomDeZone = LocZone.getCurrentZoneName();
        //Changement du texte selon la zone de l'utilisateur
        if(LocZone.getCurrentZone() == -1){
            title.setText(purgatoire);
        }
        else{fullTitle = staticTitle + nomDeZone;
            title.setText(fullTitle);}

    }

    public void onMapReady(GoogleMap map) {
        zoneMap = map;

        setupMap(zoneMap);
    }

    private void setupMap(GoogleMap map){
        //Calcul de la position avec le tracker
       UpdateLocation.zoneHasChanged(tracker);
        double[][] coords = LocZone.getZoneCoord(tracker.getLongitude(), tracker.getLatitude());

        //Calcul du polygon si on n'est pas dans le Purgatoire
        if(LocZone.getCurrentZone() != -1) {
            Polygon polygon = map.addPolygon(new PolygonOptions()
                    .add(new LatLng(coords[0][1], coords[0][0]),
                            new LatLng(coords[2][1], coords[2][0]),
                            new LatLng(coords[3][1], coords[3][0]),
                            new LatLng(coords[1][1], coords[1][0]))
                    .strokeColor(Color.BLUE));
        }

        //Code fortement inspiré du code exemple du Google API
        CameraPosition cameraPosition = new CameraPosition.Builder()
                // Sets the center of the map to Mountain View
                .target(new LatLng(tracker.getLatitude(), tracker.getLongitude()))
                .zoom(14)                   // Sets the zoom
                .bearing(0)                // Sets the orientation of the camera to east
                .tilt(0)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        map.addMarker(new MarkerOptions()
                .position(new LatLng(tracker.getLatitude(), tracker.getLongitude()))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

    }


//    onFinish()
//    Invoked if the animation goes to completion without interruption.
//            onCancel()
//    Invoked if the animation is interrupted by calling stopAnimation() or starting a new camera movement.
//
//    Alternatively, this can also occur if you call GoogleMap.stopAnimation().


}

