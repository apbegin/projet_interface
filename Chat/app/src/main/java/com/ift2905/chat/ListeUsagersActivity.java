package com.ift2905.chat;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class ListeUsagersActivity extends AppCompatActivity {


    public final int n = 30;
    ArrayList<String> liste;
    ListView list;
    MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_usager);
        list = (ListView)findViewById(R.id.listView);
        liste = (ArrayList<String>)getIntent().getSerializableExtra("listUser");
        adapter  = new MyAdapter(this, liste);
        list.setAdapter(adapter);
    }

    public class MyAdapter extends ArrayAdapter{
        public MyAdapter(Context context, ArrayList<String> liste){
            super(context, R.layout.rangee, liste);
        }

        @Override
        public int getCount() {
            return liste.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if(v == null){
                v = inflater.inflate(R.layout.rangee, parent, false);
            }
            TextView tv = (TextView) v.findViewById(R.id.textView1);
            tv.setText(((Integer)(position + 1)).toString() + ". " + liste.get(position));
            return v;
        }
    }
}
