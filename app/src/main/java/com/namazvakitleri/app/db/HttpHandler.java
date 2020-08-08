package com.namazvakitleri.app.db;

import android.media.MediaParser;
import android.renderscript.ScriptGroup;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpHandler {
    public String makeServiceCall(String requestUrl) {
        String response = null;
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            InputStream in = new BufferedInputStream(connection.getInputStream());
            response = convertStreamToString(in);
        } catch (IOException e) {
            System.out.println("Hata Bulundu: " +e.getMessage());
        }
        return response;
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String satir = "";
        try {
            while ((satir = reader.readLine()) != null){
                sb.append(satir).append("\n");
            }
        }catch (IOException e){

        }finally {
            try{
                is.close();

            }catch (IOException e){

            }
        }
        return sb.toString();
    }
}
