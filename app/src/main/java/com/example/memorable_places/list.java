package com.example.memorable_places;

import androidx.activity.OnBackPressedDispatcherOwner;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class list extends AppCompatActivity  {
    public static ArrayList<String> addresses;
   public static ArrayAdapter<String> ad;
   public static ArrayList<LatLng> locations;
    ListView lv;
    SharedPreferences s;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        locations=new ArrayList<>();
        addresses=new ArrayList<>();
        s=getSharedPreferences("com.example.memorable_places", Context.MODE_PRIVATE);
        try {
            addresses=(ArrayList<String>)ObjectSerializer.deserialize(s.getString("addresses", ObjectSerializer.serialize(new ArrayList<String>())));
            ArrayList<String> latitudes=(ArrayList<String>)ObjectSerializer.deserialize(s.getString("latitudes", ObjectSerializer.serialize(new ArrayList<String>())));
            ArrayList<String> longitudes=(ArrayList<String>) ObjectSerializer.deserialize(s.getString("longitudes", ObjectSerializer.serialize(new ArrayList<String>())));

            if(latitudes.size()!=0 && latitudes.size()==longitudes.size() && latitudes.size()==addresses.size()){
            for(int i=0;i<latitudes.size();i++)
                locations.add(new LatLng(Double.parseDouble(latitudes.get(i)),Double.parseDouble(longitudes.get(i))));}
            else
            {
                addresses.add("Add a new location...");
                locations.add(new LatLng(0,0));
            }

        } catch (IOException e) {

            e.printStackTrace();
        }
        ad=new ArrayAdapter<>(list.this,android.R.layout.simple_expandable_list_item_1,addresses);

        lv=(ListView) findViewById(R.id.lv);
        lv.setAdapter(ad);



        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i=new Intent(list.this,maps.class);
                i.putExtra("pos",(position));

                startActivity(i);

            }

        });

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog b=new AlertDialog.Builder(list.this)
                        .setTitle("Delete location?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                addresses.remove(position);
                                locations.remove(position);
                                ad.notifyDataSetChanged();
                                save();
                            }
                        }).setNegativeButton("Cancel",null).show();
                return true;
            }
        });



    }
    public void save() {
        try {
            s=getSharedPreferences("com.example.memorable_places", Context.MODE_PRIVATE);

            ArrayList<String> latitudes = new ArrayList<>();
            ArrayList<String> longitudes = new ArrayList<>();
            for (LatLng coord :locations) {
                latitudes.add(((Object) coord.latitude).toString());
                longitudes.add(((Object) coord.longitude).toString());
            }
            s.edit().putString("latitudes", ObjectSerializer.serialize(latitudes)).apply();
            s.edit().putString("longitudes", ObjectSerializer.serialize(longitudes)).apply();
            s.edit().putString("addresses", ObjectSerializer.serialize(addresses)).apply();

        } catch (IOException e) {

            e.printStackTrace();
        }
    }
    }




