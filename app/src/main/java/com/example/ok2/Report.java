package com.example.ok2;

import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Report extends AsyncTask<String, Void, Void> {
    GPS gps;

    protected Void doInBackground(String... text) {


        Log.i("Text input", text[0]);

        sendReport(text[0]);
        return null;
    }


    public void sendReport(String text) {
        try {

            Log.d("tag", "background started");
            URL url = new URL("http://main.jaapelst.nl:2121/report");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            conn.setRequestProperty("Accept","application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);



            GPS gps = new MainActivity().getGps();
            Location l = gps.getLocation();
            Log.d("Lat", String.valueOf(l.getLatitude()));
            Log.d("Lon", String.valueOf(l.getLongitude()));

            JSONObject jsonParam = new JSONObject();
            jsonParam.put("text", text);
            jsonParam.put("lat", l.getLatitude());
            jsonParam.put("lon", l.getLongitude());

            Log.i("JSON", jsonParam.toString());
            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
            //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
            os.writeBytes(jsonParam.toString());

            os.flush();
            os.close();

            Log.i("STATUS", String.valueOf(conn.getResponseCode()));
            Log.i("MSG" , conn.getResponseMessage());

            conn.disconnect();
            return;

        } catch (Exception e) {
            Log.e("REPORT", e.getMessage());
            e.printStackTrace();
            return;
        }
    }
}
