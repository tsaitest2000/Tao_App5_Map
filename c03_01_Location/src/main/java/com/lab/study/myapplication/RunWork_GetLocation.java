package com.lab.study.myapplication;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.google.android.gms.maps.model.LatLng;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by MB-teacher on 2016/10/15.
 */

public class RunWork_GetLocation extends Thread {
    private String json = "無資料";
    private Context context;
    private String address;
    private View pocket;
    private OkHttpClient client = new OkHttpClient();

    public RunWork_GetLocation(Context context, String address, View pocket) {
        this.address = address;
        this.context = context;
        this.pocket = pocket;
    }
    String run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    Runnable r = new Runnable() {
        @Override
        public void run() {

            try {
                String lat = new JSONObject(json).getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getString("lat");
                String lng = new JSONObject(json).getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getString("lng");
                //Toast.makeText(context, lat + "," + lng, Toast.LENGTH_SHORT).show();
                LatLng latLng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
                pocket.setTag(latLng);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    };

    public void run() {
        String url = String.format("http://maps.googleapis.com/maps/api/geocode/json?address=%s&language=zh_tw", address);
        try {
            json = run(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ((Activity)context).runOnUiThread(r);
    }
}
