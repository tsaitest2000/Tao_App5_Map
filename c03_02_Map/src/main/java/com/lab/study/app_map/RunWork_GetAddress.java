package com.lab.study.app_map;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.view.View;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by MB-teacher on 2016/10/15.
 */

public class RunWork_GetAddress extends Thread {
    private String json = "無資料";
    private Context context;
    private Location location;
    private View pocket;
    private OkHttpClient client = new OkHttpClient();

    public RunWork_GetAddress(Context context, Location location, View pocket) {
        this.location = location;
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
            String formatted_address = null;
            try {
                formatted_address = new JSONObject(json).getJSONArray("results").getJSONObject(0).getString("formatted_address");
            } catch (Exception e) {
                e.printStackTrace();
            }
            pocket.setTag(formatted_address);
        }
    };

    public void run() {
        String url = String.format("http://maps.googleapis.com/maps/api/geocode/json?latlng=%f,%f&language=zh_tw", location.getLatitude(), location.getLongitude());
        try {
            json = run(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ((Activity)context).runOnUiThread(r);
    }
}
