package com.ift2905.chat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.Query;

import java.util.Random;


/**
 * @author greg
 * @since 6/21/13
 *
 * This class is an example of how to use FirebaseListAdapter. It uses the <code>Chat</code> class to encapsulate the
 * data for each individual chat message
 */
public class ChatListAdapter extends FirebaseListAdapter<Chat> {

    // The mUsername for this client. We use this to indicate which messages originated from this user
    private Activity activity;
    public UserList followed;

    public ChatListAdapter(Query ref, Activity activity, int layout, String mUsername, ChatRoomActivity chatRoom) {
        super(ref, Chat.class, layout, activity, chatRoom);
        this.mUsername = mUsername;
        this.activity=activity;
        blocked = new UserList(12);
        followed = new UserList(12);
    }

    /**
     * Bind an instance of the <code>Chat</code> class to our view. This method is called by <code>FirebaseListAdapter</code>
     * when there is a data change, and we are given an instance of a View that corresponds to the layout that we passed
     * to the constructor, as well as a single <code>Chat</code> instance that represents the current data to bind.
     *
     * @param view A view instance corresponding to the layout we passed to the constructor.
     * @param chat An instance representing the current state of a chat message
     */
    @Override
    protected void populateView(View view, Chat chat) {
        // Map a Chat object to an entry in our listview
        final String author = chat.author;

        if (author != null && author.equals(mUsername)) {
            //Si le message vient de nous
            ((ImageView) view.findViewById(R.id.colored_icon)).setImageResource(0);
            ((TextView) view.findViewById(R.id.author)).setText("");
            if(!chat.destinataire.equals("")){
                ((TextView) view.findViewById(R.id.private_at)).setText("À  "+chat.destinataire+": ");
            } else ((TextView) view.findViewById(R.id.private_at)).setText("");
            ((TextView) view.findViewById(R.id.message)).setText("");
            ((TextView) view.findViewById(R.id.followedauthor)).setText("");
            ((TextView) view.findViewById(R.id.followedmessage)).setText("");
            ((TextView) view.findViewById(R.id.mymessage)).setText(chat.getMessage());
        } else {
            //Si le message vient d'un autre utilisateur
            ImageView im_icon = ((ImageView) view.findViewById(R.id.colored_icon));
            im_icon.setOnClickListener(new ShortClickClass(author));

            if(followed.getPosition(author)>=0){
                im_icon.setImageResource(R.drawable.icon_heart);
                TextView authorText = (TextView) view.findViewById(R.id.followedauthor);
                authorText.setText(author + ": ");
                authorText.setTextColor(Color.BLUE);
                authorText.setOnClickListener(new ShortClickClass(author));
                ((TextView) view.findViewById(R.id.followedmessage)).setText(chat.getMessage());
                ((TextView) view.findViewById(R.id.message)).setText("");
                ((TextView) view.findViewById(R.id.author)).setText("");
            } else {
                im_icon.setImageResource((int) attributeColor(author));
                TextView authorText = (TextView) view.findViewById(R.id.author);
                authorText.setText(author + ": ");
                authorText.setTextColor(Color.BLACK);
                authorText.setOnClickListener(new ShortClickClass(author));
                ((TextView) view.findViewById(R.id.message)).setText(chat.getMessage());
                ((TextView) view.findViewById(R.id.followedmessage)).setText("");
                ((TextView) view.findViewById(R.id.followedauthor)).setText("");
            }
            ((TextView) view.findViewById(R.id.mymessage)).setText("");
            ((TextView) view.findViewById(R.id.private_at)).setText("");

        }

    }

    protected class ShortClickClass implements View.OnClickListener {
        protected String author;

        public ShortClickClass(String author) {
            this.author = author;
        }

        @Override
        public void onClick(View v) {
            AlertDialog.Builder choice = new AlertDialog.Builder(activity);
            choice.setTitle(author);
            if (followed.getPosition(author) < 0) {
                choice.setPositiveButton("Follow", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        followed.addUser(author);
                    }
                });
            } else {
                choice.setPositiveButton("Unfollow", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        followed.deleteUser(author);
                    }
                });
            }
            choice.setNeutralButton("Block", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    blocked.addUser(author);
                }
            });

            if (blocked.getPosition(author) < 0) {
                choice.setNeutralButton("Block", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        blocked.addUser(author);
                    }
                });
            } else {
                choice.setNeutralButton("Unblock", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        blocked.deleteUser(author);
                    }
                });
            }
            choice.show();
            System.out.println("long click on " + author);
        }


    }
    //---------------------------COLORING ICONS-----------------------------
    private Object[][] listOfAuthors = new Object[100][2]; //Users who already wrote something
    private Object[] listOfIcons = {R.drawable.icon_cyan, R.drawable.icon_green, R.drawable.icon_pink,
            R.drawable.icon_orange, R.drawable.icon_palegreen, R.drawable.icon_palepurple,
            R.drawable.icon_yellow, R.drawable.icon_paleblue, R.drawable.icon_red,
            R.drawable.icon_blue, R.drawable.icon_purple, R.drawable.icon_palepink};

    protected Object attributeColor(String author){
        Random rand=new Random();
        int i=0;
        while(listOfAuthors[i][0]!=null){
            if (listOfAuthors[i][0].equals(author)) break;
            i++;
        }
        if(listOfAuthors[i][0]==null){
            listOfAuthors[i][0]=author;
            listOfAuthors[i][1]=listOfIcons[i%12];
        }
        return (int)listOfAuthors[i][1];
    }

}
