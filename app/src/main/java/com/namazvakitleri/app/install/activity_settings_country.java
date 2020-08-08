package com.namazvakitleri.app.install;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.namazvakitleri.app.db.HttpHandler;
import com.namazvakitleri.app.R;
import com.namazvakitleri.app.entity.ulkeler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class activity_settings_country extends AppCompatActivity {
    List<ulkeler> ulkelerList = new ArrayList<ulkeler>();

    ProgressDialog progressDialog1;

    protected SharedPreferences sharedPref;
    protected SharedPreferences.Editor editor;

    HttpHandler httpHandler;
    Gson gson;

    Spinner spinner;
    Button btn;

    private String JSON_URL_COUNTRY = "https://ezanvakti.herokuapp.com/ulkeler";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_country);
        /*if (!checkAndRequestPermissions()) {
            return;
        }*/
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        sharedPref = this.getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        gson = new Gson();
        spinner = findViewById(R.id.settings_ulke);
        btn = findViewById(R.id.settings_ulke_button);

        if (internetErisimi()) {
            boolean ilkMi = sharedPref.getBoolean("ilkMi", true);
            if (ilkMi) {
                executeAsyncTask();
                //new getCountryList().execute();
            } else {
                ulkeleriGetir();
            }
        } else {
            btn.setText("İnternet Yok");
            Toast.makeText(this, "Lütfen internet bağlantısını aktif ediniz.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(activity_settings_country.this, activity_settings_vakitler.class);
            startActivity(intent);
            finish();
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = spinner.getSelectedItem().toString();
                String ulke = spinner.getSelectedItem().toString();
                id = id.substring(1, id.indexOf("]"));
                ulke = ulke.substring(ulke.indexOf("-"));
                ulke = ulke.substring(1);
                editor.putString("ulkem_id", id);
                editor.putString("ulkem_isim", ulke);
                editor.commit();
                Intent i = new Intent(activity_settings_country.this, activity_settings_city.class);
                startActivity(i);
                finish();
                //Toast.makeText(activity_settings_country.this, "id: " + id + ", ülke: " + ulke, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ulkeleriGetir() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    String ulkeler = sharedPref.getString("ulkeler", "Ülkeler Bulunamadı.");
                    System.out.println("ÜLKELER: " + ulkeler);
                    ulkelerList = Arrays.asList(gson.fromJson(ulkeler, ulkeler[].class));
                    String[] arraySpinner = new String[ulkelerList.size()];
                    for (int i = 0; i < ulkelerList.size(); i++) {
                        arraySpinner[i] = "[" + ulkelerList.get(i).getUlkeID() + "]- " + ulkelerList.get(i).getUlkeAdi();
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                            R.layout.spinner_item, arraySpinner);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);
                    spinner.setSelection(187);
                    btn.setText("ÜLKE SEÇ");
                    btn.setEnabled(true);

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(activity_settings_country.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        }, 10);

    }

    public void executeAsyncTask() {
        @SuppressLint("StaticFieldLeak")
        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            List<ulkeler> geciciUlke = new ArrayList<ulkeler>();
            String jsonString;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog1 = new ProgressDialog(activity_settings_country.this);
                progressDialog1.setMessage("Lütfen ülkeler indirilirken bekleyiniz..");
                progressDialog1.setCancelable(false);
                progressDialog1.show();
            }

            @Override
            protected String doInBackground(Void... params) {
                httpHandler = new HttpHandler();
                jsonString = httpHandler.makeServiceCall(JSON_URL_COUNTRY);
                if (null != jsonString) {
                    geciciUlke = Arrays.asList(gson.fromJson(jsonString, ulkeler[].class));
                    /*for (int i = 0; i < geciciUlke.size(); i++) {
                        ulkelerList.add(geciciUlke.get(i));
                    }*/
                    editor.putString("ulkeler", jsonString);
                    editor.commit();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String msg) {
                if (progressDialog1.isShowing()) {
                    progressDialog1.dismiss();
                }
                ulkeleriGetir();
            }
        };

        if (Build.VERSION.SDK_INT >= 11/*HONEYCOMB*/) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            task.execute();
        }
    }

    public boolean internetErisimi() {

        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (conMgr.getActiveNetworkInfo() != null

                && conMgr.getActiveNetworkInfo().isAvailable()

                && conMgr.getActiveNetworkInfo().isConnected()) {

            return true;

        } else {

            return false;

        }

    }


}
