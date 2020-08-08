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
import com.namazvakitleri.app.entity.sehirler;
import com.namazvakitleri.app.entity.ulkeler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class activity_settings_city extends AppCompatActivity {
    List<sehirler> sehirlerList = new ArrayList<sehirler>();
    ProgressDialog progressDialog2;

    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    HttpHandler httpHandler;
    Gson gson;

    Spinner spinner;
    Button btn;
    String ulkem_id;
    private String JSON_URL_CITY = "https://ezanvakti.herokuapp.com/sehirler?ulke=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_city);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        sharedPref = this.getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        gson = new Gson();
        spinner = findViewById(R.id.settings_sehir);
        btn = findViewById(R.id.settings_sehir_button);

        if (internetErisimi()) {
            ulkem_id = sharedPref.getString("ulkem_id", "2");
            JSON_URL_CITY = "https://ezanvakti.herokuapp.com/sehirler?ulke=" + ulkem_id;
            //new getCityList().execute();
            executeAsyncTask();
        } else {
            btn.setText("İnternet Yok");
            Toast.makeText(this, "Lütfen internet bağlantısını aktif ediniz.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(activity_settings_city.this, activity_settings_vakitler.class);
            startActivity(intent);
            finish();
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = spinner.getSelectedItem().toString();
                String sehir = spinner.getSelectedItem().toString();
                id = id.substring(1, id.indexOf("]"));
                sehir = sehir.substring(sehir.indexOf("-"));
                sehir = sehir.substring(1);
                editor.putString("sehrim_id", id);
                editor.putString("sehrim_isim", sehir);
                editor.commit();
                Intent i = new Intent(activity_settings_city.this, activity_settings_district.class);
                startActivity(i);
                finish();
                //Toast.makeText(activity_settings_city.this, "id: " + id + ", şehir: " + sehir, Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void sehirleriGetir() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    String sehirler = sharedPref.getString("sehirler", "Şehirler Bulunamadı.");
                    System.out.println("ŞEHİRLER: " + sehirler);
                    sehirlerList = Arrays.asList(gson.fromJson(sehirler, sehirler[].class));
                    String[] arraySpinner = new String[sehirlerList.size()];
                    for (int i = 0; i < sehirlerList.size(); i++) {
                        arraySpinner[i] = "[" + sehirlerList.get(i).getSehirID() + "]- " + sehirlerList.get(i).getSehirAdi();
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                            R.layout.spinner_item, arraySpinner);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);
                    btn.setText("ŞEHİR SEÇ");
                    btn.setEnabled(true);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
        }, 10);
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

    public void executeAsyncTask() {
        @SuppressLint("StaticFieldLeak")
        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            List<sehirler> geciciSehir = new ArrayList<sehirler>();
            String jsonString;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog2 = new ProgressDialog(activity_settings_city.this);
                progressDialog2.setMessage("Lütfen şehirler indirilirken bekleyiniz..");
                progressDialog2.setCancelable(false);
                progressDialog2.show();
            }

            @Override
            protected String doInBackground(Void... params) {
                httpHandler = new HttpHandler();
                jsonString = httpHandler.makeServiceCall(JSON_URL_CITY);
                if (null != jsonString) {
                    geciciSehir = Arrays.asList(gson.fromJson(jsonString, sehirler[].class));
                    for (int i = 0; i < geciciSehir.size(); i++) {
                        sehirlerList.add(geciciSehir.get(i));
                    }
                    editor.putString("sehirler", jsonString);
                    editor.commit();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String msg) {
                if (progressDialog2.isShowing()) {
                    progressDialog2.dismiss();
                }
                sehirleriGetir();
            }
        };

        if (Build.VERSION.SDK_INT >= 11/*HONEYCOMB*/) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            task.execute();
        }
    }

}