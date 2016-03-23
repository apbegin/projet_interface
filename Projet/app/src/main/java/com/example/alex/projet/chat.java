package com.example.alex.projet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class chat extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.settings:
                startActivity(new Intent(this, Settings.class));
                return true;
            case R.id.carte:
                startActivity(new Intent(this, Carte.class));
                return true;
            case R.id.usagers:
                startActivity(new Intent(this, Liste_usagers.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
