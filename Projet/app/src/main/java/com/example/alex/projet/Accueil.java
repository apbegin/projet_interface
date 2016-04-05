package com.example.alex.projet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Accueil extends AppCompatActivity implements View.OnClickListener {

    Button ok;
    EditText editText;

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
        ok = (Button) findViewById(R.id.button_ok);
        String s = editText.getText().toString();

        if(s.trim().equals("")){
            ok.setEnabled(false);
        }else{
            ok.setEnabled(true);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);
        editText = (EditText) findViewById(R.id.pseudonyme);

        editText.addTextChangedListener(mTextWatcher);
        checkFieldsForEmptyValues();

        ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(Accueil.this, Zone.class);
        this.startActivity(intent);
    }
}

//TODO: faire en sorte qu'on ne puisse pas faire back de ((Chat vers Zone)) et ((Zone vers Accueil

