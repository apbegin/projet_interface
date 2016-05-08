package com.ift2905.chat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AccueilActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnConfirm;
    EditText txtPseudo;

    private String mUsername;
    private SharedPreferences prefs;
    //GPSTracker gps; //TODO: delete

    private TextWatcher mTextWatcher = new TextWatcher(){

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            checkFieldsForEmptyValues();
        }
    };

    void checkFieldsForEmptyValues(){
        btnConfirm = (Button) findViewById(R.id.btn_confirm);
        String s = txtPseudo.getText().toString();

        if(s.trim().equals("")){
            btnConfirm.setEnabled(false);
        }else{
            btnConfirm.setEnabled(true);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);
        txtPseudo = (EditText) findViewById(R.id.pseudo);

        txtPseudo.addTextChangedListener(mTextWatcher);
        checkFieldsForEmptyValues();

        btnConfirm.setOnClickListener(this);

        if(getUsername()) {
            txtPseudo.setText(mUsername);
        }
    }

    @Override
    public void onClick(View v) {
        setUsername();
        Intent intent = new Intent(this, ZoneActivity.class);
        intent.putExtra("username", mUsername);
        this.startActivity(intent);
        /*gps = new GPSTracker(Accueil.this);
        UpdateLocation UP = new UpdateLocation();
        //UpdateLocation.zoneHasChanged(gps);
        Toast.makeText(getApplicationContext(), UP.showStats(gps), Toast.LENGTH_LONG).show();*/
    }

    private Boolean getUsername(){
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        mUsername = prefs.getString("pseudonyme", null);
        return mUsername != null;
    }

    private void setUsername(){
        mUsername = txtPseudo.getText().toString();
        prefs.edit().putString("pseudonyme", mUsername).commit();
    }
}
