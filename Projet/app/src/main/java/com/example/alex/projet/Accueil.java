package com.example.alex.projet;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class Accueil extends AppCompatActivity implements View.OnClickListener {

    Button btnConfirm;
    EditText txtPseudo;

    private String mUsername;
    private SharedPreferences prefs;


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

        Intent intent = new Intent(Accueil.this, Zone.class);
        this.startActivity(intent);
    }

    private Boolean getUsername(){
        prefs = getApplication().getSharedPreferences("chatPrefs",0);
        mUsername = prefs.getString("username", null);
        return mUsername != null;
    }

    private void setUsername(){
        mUsername = txtPseudo.getText().toString();
        prefs.edit().putString("username", mUsername).commit();
    }
}
