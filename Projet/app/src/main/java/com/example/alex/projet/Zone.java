package com.example.alex.projet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Zone extends AppCompatActivity implements View.OnClickListener {

    Button joindre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zone);

        joindre = (Button)findViewById(R.id.button_joindre);
        joindre.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        //setContentView(R.layout.activity_chat);
        Intent intent = new Intent(Zone.this, chat.class);
        this.startActivity(intent);
    }
}