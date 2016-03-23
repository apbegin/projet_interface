package com.example.alex.projet;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

;

public class Liste_usagers extends AppCompatActivity {

    ListView list;
    public final int n = 30;
    MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_usagers);

        list = (ListView)findViewById(R.id.listView);
        adapter  = new MyAdapter();
        list.setAdapter(adapter);
    }

    public class MyAdapter extends BaseAdapter{
        LayoutInflater inflater;

        public MyAdapter(){
            inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return n;
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
            if(v == null){
                v = inflater.inflate(R.layout.rangee, parent, false);
            }
            TextView tv = (TextView)v.findViewById(R.id.textView1);
            tv.setText("Item " + ((Integer)position).toString());
            return v;
        }
    }
}
