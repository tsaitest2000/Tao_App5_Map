package com.lab.study.myapplication;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends AppCompatActivity {
    private LocationManager manager;
    private TextView textView, textView2, textView3;
    private EditText editText;
    private Context context;
    private Location location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        textView = (TextView) findViewById(R.id.textView);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView3 = (TextView) findViewById(R.id.textView3);
        editText = (EditText) findViewById(R.id.editText);
        manager = (LocationManager) getSystemService(LOCATION_SERVICE);
        String provider = manager.getBestProvider(new Criteria(), true);

        if(provider == null) {
            Toast.makeText(context, "目前沒有任何定位服務", Toast.LENGTH_SHORT).show();
            return;
        }

        manager.requestLocationUpdates(provider, 0, 0, listener);

    }


    private LocationListener listener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            MainActivity.this.location = location;
            String info = String.format("緯度:%f\n經度:%f\n標高:%.2f\n誤差:%.1f m", location.getLatitude(), location.getLongitude(), location.getAltitude(), location.getAccuracy());
            textView.setText(info);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {
            Toast.makeText(context, "GPS被關閉", Toast.LENGTH_SHORT).show();
        }
    };

    public void onClick(View view) {

        View pocket = new View(context) {
            @Override
            public void setTag(Object tag) {
                super.setTag(tag);
                textView2.setText(tag.toString());
            }
        };
        new RunWork_GetAddress(context, location, pocket).start();

    }

    public void onClick2(View view) {

        View pocket = new View(context) {
            @Override
            public void setTag(Object tag) {
                super.setTag(tag);
                LatLng latLng = (LatLng)tag;
                textView3.setText(latLng.toString());
            }
        };
        new RunWork_GetLocation(context, editText.getText().toString(), pocket).start();

    }



}
