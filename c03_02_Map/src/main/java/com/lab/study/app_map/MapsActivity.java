package com.lab.study.app_map;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationManager manager;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        context = this;
        manager = (LocationManager) getSystemService(LOCATION_SERVICE);
        String provider = manager.getBestProvider(new Criteria(), true);
        manager.requestLocationUpdates(provider, 0, 0, listener);
    }

    private LocationListener listener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            // Add a marker in Sydney and move the camera
            View pocket = new View(context) {
                @Override
                public void setTag(Object tag) {
                    super.setTag(tag);
                    String addr = "無地址";
                    if(tag != null) {
                        addr = tag.toString();
                    }
                    LatLng sydney = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(sydney).title(addr));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 17));
                }
            };
            new RunWork_GetAddress(context, location, pocket).start();


        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


    }

    public void onClick(View view) {

        //透過 Intent 的方式開啟內建的語音辨識 Activity...
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "請說話..."); //語音辨識 Dialog 上要顯示的提示文字

        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            //把所有辨識的可能結果取出。
            List result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if(result.size() > 0) {
                final String address = result.get(0).toString(); // 最接近的資料
                Toast.makeText(context, address, Toast.LENGTH_SHORT).show();
                View pocket = new View(context) {
                    @Override
                    public void setTag(Object tag) {
                        super.setTag(tag);
                        LatLng sydney = (LatLng)tag;
                        mMap.addMarker(new MarkerOptions().position(sydney).title(address));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 17));
                    }
                };
                new RunWork_GetLocation(context, address, pocket).start();

            }

        }

    }


}
