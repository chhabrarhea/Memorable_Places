package com.example.memorable_places;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            Button b=(Button) findViewById(R.id.grantButton);
           getPermission(b);
        }
        else
        {
            move();
        }
    }
    private void move()
    {
        Intent i=new Intent(MainActivity.this,list.class);
        finish();
        startActivity(i);
    }
    public void getPermission(View view)
    {
        Dexter.withActivity(MainActivity.this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                move();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {
                if(response.isPermanentlyDenied())
                {
                    AlertDialog builder=new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Device Location Disabled!")
                            .setMessage("Enable Location from App Settings?")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setNegativeButton("Cancel",null)
                            .setPositiveButton("Go to Settings", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent=new Intent();
                                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    intent.setData(Uri.fromParts("package",getPackageName(),null));
                                    startActivity(intent);
                                }
                            }).show();
                }
                else
                    Toast.makeText(MainActivity.this,"Device Location required!",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                token.continuePermissionRequest();

            }
        }).check();
    }
}
