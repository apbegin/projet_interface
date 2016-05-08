package com.ift2905.chat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.HashMap;
import java.util.Map;

public class ZoneActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {

    Button btnJoin;
    User user;
    GoogleMap zoneMap;
    TextView title;
    String nomDeZone = "";
    String staticTitle = "Vous serez mis en contact avec les usagers de la zone: ";
    String fullTitle = "";
    String purgatoire = "Vous êtes dans aucune zone, vous serez dans le Purgatoire";
    GPSTracker tracker;
    GPSTracker tracker2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zone);

        //Initialiser le tracker
        tracker = new GPSTracker(this);

        Intent i = getIntent();
        String username = (String)i.getSerializableExtra("username");
        user = new User(username);

        Firebase.setAndroidContext(this);
        Firebase ref = new Firebase("https://dazzling-inferno-8175.firebaseio.com/");
        Map<String, Object> chatroom = new HashMap<String, Object>();
        chatroom.put("/", "Chatrooms");

        //Initialisation du fragment de map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //Lien avec layout de l'activité
        title = (TextView)findViewById(R.id.textView);
        //Déterminer la zone de l'utilsiateur
        UpdateLocation.zoneHasChanged(tracker);
        nomDeZone = LocZone.getCurrentZoneName();
        //Changement du texte selon la zone de l'utilisateur
        if(LocZone.getCurrentZone() == -1){
            title.setText(purgatoire);
        }
        else{fullTitle = staticTitle + nomDeZone;
            title.setText(fullTitle);}

        ref.updateChildren(chatroom);

        btnJoin = (Button)findViewById(R.id.button_joindre);
        btnJoin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        setContentView(R.layout.activity_chatroom);
        Intent intent = new Intent(this, ChatRoomActivity.class);
        intent.putExtra("User",user);
        this.startActivity(intent);

    }

    @Override
    public void onMapReady(GoogleMap map) {
        zoneMap = map;

        setupMap(zoneMap);
    }

    private void setupMap(GoogleMap map){
        tracker = new GPSTracker(this);
        //Calcul de la position avec le tracker
        double[][] coords = LocZone.getZoneCoord(tracker.getLongitude(), tracker.getLatitude());

        //Calcul du polygon si on n'est pas dans le Purgatoire
        if(LocZone.getCurrentZone() != -1) {
            PolygonOptions rectOptions = new PolygonOptions()
                    .add(new LatLng(coords[0][1], coords[0][0]),
                            new LatLng(coords[2][1], coords[2][0]),
                            new LatLng(coords[3][1], coords[3][0]),
                            new LatLng(coords[1][1], coords[1][0]))
                    .strokeColor(Color.BLUE);

            map.addPolygon(rectOptions);
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
        //Ajoute un marqueur correspondant à la position de l'utilisateur
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
