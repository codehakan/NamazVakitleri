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
import com.namazvakitleri.app.entity.ilceler;
import com.namazvakitleri.app.entity.sehirler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class activity_settings_district extends AppCompatActivity {
    List<ilceler> ilcelerList = new ArrayList<ilceler>();
    ProgressDialog progressDialog3;

    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    HttpHandler httpHandler;
    Gson gson;

    Spinner spinner;
    Button btn;
    String sehrim_id;
    private String JSON_URL_DISTRICT = "https://ezanvakti.herokuapp.com/ilceler?sehir=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_district);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        sharedPref = this.getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        gson = new Gson();
        spinner = findViewById(R.id.settings_ilce);
        btn = findViewById(R.id.settings_ilce_button);


        if(internetErisimi()){
            sehrim_id = sharedPref.getString("sehrim_id", "571");
            JSON_URL_DISTRICT = "https://ezanvakti.herokuapp.com/ilceler?sehir=" + sehrim_id;
            //new getDistrictList().execute();
            executeAsyncTask();
        }else{
            btn.setText("İnternet Yok");
            Intent intent = new Intent(activity_settings_district.this,activity_settings_vakitler.class);
            startActivity(intent);
            finish();
            Toast.makeText(this, "Lütfen internet bağlantısını aktif ediniz.", Toast.LENGTH_LONG).show();
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = spinner.getSelectedItem().toString();
                String ilce = spinner.getSelectedItem().toString();
                id = id.substring(1, id.indexOf("]"));
                ilce = ilce.substring(ilce.indexOf("-"));
                ilce = ilce.substring(1);
                editor.putString("ilcem_id", id);
                editor.putString("ilcem_isim", ilce);
                editor.putBoolean("ilkAcilis", false);
                editor.commit();
                Intent i = new Intent(activity_settings_district.this, activity_settings_vakitler.class);
                startActivity(i);
                finish();
                //Toast.makeText(activity_settings_district.this, "id: " + id + ", ilçe: " + ilce, Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void ilceleriGetir() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    String ilceler = sharedPref.getString("ilceler", "İlçeler Bulunamadı.");
                    System.out.println("HAKANBEY: " + ilceler);
                    ilcelerList = Arrays.asList(gson.fromJson(ilceler, ilceler[].class));
                    String[] arraySpinner = new String[ilcelerList.size()];
                    for (int i = 0; i < ilcelerList.size(); i++) {
                        arraySpinner[i] = "[" + ilcelerList.get(i).getIlceID() + "]- " + ilcelerList.get(i).getIlceAdi();
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                            R.layout.spinner_item, arraySpinner);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);
                    btn.setText("İLÇE SEÇ VE KUR");
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
            List<ilceler> geciciIlce = new ArrayList<ilceler>();
            String jsonString;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog3 = new ProgressDialog(activity_settings_district.this);
                progressDialog3.setMessage("Lütfen ilçeler indirilirken bekleyiniz..");
                progressDialog3.setCancelable(false);
                progressDialog3.show();
            }

            @Override
            protected String doInBackground(Void... params) {
                httpHandler = new HttpHandler();
                jsonString = httpHandler.makeServiceCall(JSON_URL_DISTRICT);

                if (null != jsonString) {
                    geciciIlce = Arrays.asList(gson.fromJson(jsonString, ilceler[].class));
                    /*for (int i = 0; i < geciciIlce.size(); i++) {
                        ilcelerList.add(geciciIlce.get(i));
                    }*/
                    editor.putString("ilceler", jsonString);
                    editor.commit();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String msg) {
                if (progressDialog3.isShowing()) {
                    progressDialog3.dismiss();
                }
                ilceleriGetir();
            }
        };

        if (Build.VERSION.SDK_INT >= 11/*HONEYCOMB*/) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            task.execute();
        }
    }
}