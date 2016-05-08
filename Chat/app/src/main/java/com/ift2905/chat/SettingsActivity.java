package com.ift2905.chat;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import com.firebase.client.Firebase;

import java.util.HashMap;

/**
 * Created by Alex on 2016-03-21.
 * Updated by Antoine on 2016-04-27
 */


public class SettingsActivity extends PreferenceActivity {

    private String username;
    private String firebasePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();

        Intent i = getIntent();
        firebasePath = (String)i.getSerializableExtra("zone");
        username = PreferenceManager.getDefaultSharedPreferences(this).getString("pseudonyme",null);

    }

    public static class SettingsFragment extends PreferenceFragment {
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        //Ajoute le nouveau username et supprime l'ancien de firebase
        String user =  PreferenceManager.getDefaultSharedPreferences(this).getString("pseudonyme",null);
        if (!username.equals(user) && !user.trim().equals("")){

            HashMap<String,Object> userUpdate = new HashMap<String, Object>();
            userUpdate.put(user,user);
            Firebase.setAndroidContext(this);
            Firebase ref = new Firebase(firebasePath);

            ref.child("users/" + username).removeValue();
            ref.child("users").updateChildren(userUpdate);
        }
        else
        {
            PreferenceManager.getDefaultSharedPreferences(this).edit().putString("pseudonyme",username).commit();
        }
    }
}