package com.ift2905.chat;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Antoine on 4/17/2016.
 * Une petite partie du code provenant du demo chat de firebase: https://github.com/firebase/AndroidChat
 */

public class ChatRoomActivity extends AppCompatActivity {


    private static final String FIREBASE_URL = "https://dazzling-inferno-8175.firebaseio.com/";

    private Firebase mFirebaseRef;
    private ValueEventListener mConnectedListener;
    protected ChatListAdapter mChatListAdapter; //le ChatListAdapter qui est connecté
    private User user;
    private ArrayList<String> listUser;
    TextView title;
    TextView subtitle;
    public TextView envoi;
    private String nomDeZone="";
    private ValueEventListener chatroomUserCount;
    private GPSTracker tracker;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);
        user = (User)getIntent().getSerializableExtra("User");
        tracker = new GPSTracker(ChatRoomActivity.this);
        //Initialisation du GPStracker, position et chatroom
        UpdateLocation.zoneHasChanged(tracker);


        //Initialisation des références au layout
        title = (TextView)findViewById(R.id.chatroom_title);
        subtitle = (TextView)findViewById(R.id.chatroom_subtitle);
        envoi = (TextView)findViewById(R.id.tv_destinataire);
        envoi.setText("Envoyer à tous");

        //On assigne le nouveau URL de chat déterminé selon la zone + set nom de zone
        mFirebaseRef = new Firebase(FIREBASE_URL).
                child(LocZone.getCurrentZoneName() + "/chat");
        nomDeZone = LocZone.getCurrentZoneName();
        title.setText(nomDeZone);


        // Setup our input methods. Enter key on the keyboard or pushing the send button
        EditText inputText = (EditText) findViewById(R.id.messageInput);
        inputText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_NULL && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    sendMessage();
                }
                return true;
            }
        });

        findViewById(R.id.sendButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Setup our view and list adapter. Ensure it scrolls to the bottom as data changes
        listView = (ListView)findViewById(R.id.chatroom_listView);
        if(mChatListAdapter==null) {
            mChatListAdapter = new ChatListAdapter(mFirebaseRef.limitToLast(200),
                    this, R.layout.chat_message, user.getUsername(), this);
            listView.setAdapter(mChatListAdapter);
            mChatListAdapter.registerDataSetObserver(new DataSetObserver() {
                @Override
                public void onChanged() {
                    super.onChanged();
                    listView.setSelection(mChatListAdapter.getCount() - 1);
                }
            });
        }
        // Finally, a little indication of connection status
        mConnectedListener = mFirebaseRef.getRoot().child(".info/connected").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean connected = (Boolean) dataSnapshot.getValue();

                // ------------------POURRAIT ETRE UNE FONCTION ENTIERE------------------------
                tracker.getLocation();                      //Update de la position
                //Si on a changé de zone
                if (UpdateLocation.zoneHasChanged(tracker)){
                    mFirebaseRef = new Firebase(FIREBASE_URL).
                            child(LocZone.getCurrentZoneName()+"/chat");
                    nomDeZone = LocZone.getCurrentZoneName();
                    title.setText(nomDeZone);
                    Intent intent2 = new Intent(ChatRoomActivity.this, HorsZoneActivity.class);
                    intent2.putExtra("username", user.getUsername());
                    startActivity(intent2);
                }
//                //----------------------------------------------------------------------------
                if (connected) {
                    HashMap<String,Object> userUpdate = new HashMap<String, Object>();
                    userUpdate.put(user.getUsername(),user.getUsername());
                    mFirebaseRef.getParent().child("/users/").updateChildren(userUpdate);
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });

        //Subtitle: user count
        chatroomUserCount = mFirebaseRef.getParent().child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int nbUser=0;
                ArrayList<String> lst = new ArrayList<String>();

                try{
                    nbUser = (int)dataSnapshot.getChildrenCount();
                }catch (Exception e){
                    subtitle.setText("aucun autre utilisateurs présentement");
                }

                if(nbUser>1) {
                    subtitle.setText("" +(nbUser-1)+" autre(s) utilisateur(s) présentement");
                    Iterator<DataSnapshot> usrList = dataSnapshot.getChildren().iterator();

                    for (int i = 0; i <= nbUser - 1; i++) {
                        lst.add(usrList.next().getKey().toString());
                    }
                    listUser = lst;
                }
                else{
                    subtitle.setText("aucun autre utilisateurs présentement");
                    lst.add(user.getUsername());
                    listUser = lst;
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        //mise à jour du pseudonyme pour les requetes firebase
        if(!user.getUsername().equals(PreferenceManager.getDefaultSharedPreferences(this).getString("pseudonyme", null))) {
            user.setUsername(PreferenceManager.getDefaultSharedPreferences(this).getString("pseudonyme", null));
            mChatListAdapter = new ChatListAdapter(mFirebaseRef.limitToLast(200),
                    this, R.layout.chat_message, user.getUsername(), this);
            listView.setAdapter(mChatListAdapter);
            mChatListAdapter.registerDataSetObserver(new DataSetObserver() {
                @Override
                public void onChanged() {
                    super.onChanged();
                    listView.setSelection(mChatListAdapter.getCount() - 1);
                }
            });
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //retire le user de firebase
        mFirebaseRef.getParent().child("/users/" + user.getUsername()).removeValue();
        mFirebaseRef.getRoot().child(".info/connected").removeEventListener(mConnectedListener);
        mChatListAdapter.cleanup();
    }

    public void sendMessage() {
        EditText inputText = (EditText) findViewById(R.id.messageInput);
        String input = inputText.getText().toString();
        if (!input.equals("")) {
            // Create our 'model', a Chat object
            Chat chat = new Chat(input, user.getUsername(), "");
            // Create a new, auto-generated child of that chat location, and save our chat data there
            mFirebaseRef.push().setValue(chat);
            inputText.setText("");
            this.envoi.setText("Envoyer à tous");
        }
    }



    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.settings:
                intent = new Intent(this, SettingsActivity.class);
                intent.putExtra("zone", mFirebaseRef.getParent().toString());
                this.startActivity(intent);
                return true;
            case R.id.carte:
                startActivity(new Intent(this, CarteActivity.class));
                return true;
            case R.id.usagers:
                intent = new Intent(this, ListeUsagersActivity.class);
                intent.putExtra("listUser",listUser);
                this.startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
