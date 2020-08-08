package com.namazvakitleri.app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.namazvakitleri.app.db.HttpHandler;
import com.namazvakitleri.app.entity.ilceler;
import com.namazvakitleri.app.entity.sehirler;
import com.namazvakitleri.app.entity.ulkeler;
import com.namazvakitleri.app.install.activity_settings_city;
import com.namazvakitleri.app.install.activity_settings_district;
import com.namazvakitleri.app.install.activity_settings_vakitler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class kurulum extends AppCompatActivity {
    List<ulkeler> ulkelerList = new ArrayList<ulkeler>();
    List<sehirler> sehirlerList = new ArrayList<sehirler>();
    List<ilceler> ilcelerList = new ArrayList<ilceler>();


    protected SharedPreferences sharedPref;
    protected SharedPreferences.Editor editor;

    HttpHandler httpHandler;
    Gson gson;

    Spinner spinner_ulke, spinner_sehir, spinner_ilce;
    Button kurulum_button;

    private String JSON_URL_COUNTRY = "http://ezanvakti.herokuapp.com/ulkeler";
    private String JSON_URL_CITY = "http://ezanvakti.herokuapp.com/sehirler?ulke=";
    private String JSON_URL_DISTRICT = "https://ezanvakti.herokuapp.com/ilceler?sehir=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kurulum);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        sharedPref = this.getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        gson = new Gson();
        spinner_ulke = findViewById(R.id.spinner_ulke);
        spinner_sehir = findViewById(R.id.spinner_sehir);
        spinner_ilce = findViewById(R.id.spinner_ilce);
        kurulum_button = findViewById(R.id.kurulum_button);


        spinner_ulke.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String id = spinner_ulke.getSelectedItem().toString();
                String ulke = spinner_ulke.getSelectedItem().toString();
                id = id.substring(1, id.indexOf("]"));
                ulke = ulke.substring(ulke.indexOf("-"));
                ulke = ulke.substring(1);
                editor.putString("ulkem_id", id);
                editor.putString("ulkem_isim", ulke);
                editor.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner_sehir.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String id = spinner_sehir.getSelectedItem().toString();
                String sehir = spinner_sehir.getSelectedItem().toString();
                id = id.substring(1, id.indexOf("]"));
                sehir = sehir.substring(sehir.indexOf("-"));
                sehir = sehir.substring(1);
                editor.putString("sehrim_id", id);
                editor.putString("sehrim_isim", sehir);
                editor.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner_ilce.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String id = spinner_ilce.getSelectedItem().toString();
                String ilce = spinner_ilce.getSelectedItem().toString();
                id = id.substring(1, id.indexOf("]"));
                ilce = ilce.substring(ilce.indexOf("-"));
                ilce = ilce.substring(1);
                editor.putString("ilcem_id", id);
                editor.putString("ilcem_isim", ilce);
                editor.putBoolean("ilkAcilis",false);
                editor.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        kurulum_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(kurulum.this, activity_settings_vakitler.class);
                startActivity(in);
                finish();
            }
        });

        boolean ilkMi = sharedPref.getBoolean("ilkMi", true);
        if (ilkMi) {
            new getCountryList().execute();
        } else {
            ulkeleriGetir();
        }

        Toast.makeText(this, "Yeni Ekrandayız", Toast.LENGTH_SHORT).show();
    }

    protected void ulkeleriGetir() {
        Handler handler = new Handler();
        final SharedPreferences tempshared = this.getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {// Süre sonunda burada yer alan kodlar çalışır.
                String ulkeler = tempshared.getString("ulkeler", "Ülkeler Bulunamadı.");
                System.out.println("ÜLKELER : " + ulkeler);
                ulkelerList = Arrays.asList(gson.fromJson(ulkeler, ulkeler[].class));
                String[] arraySpinner = new String[ulkelerList.size()];
                for (int i = 0; i < ulkelerList.size(); i++) {
                    arraySpinner[i] = "[" + ulkelerList.get(i).getUlkeID() + "]- " + ulkelerList.get(i).getUlkeAdi();
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                        R.layout.spinner_item, arraySpinner);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_ulke.setAdapter(adapter);
                spinner_ulke.setSelection(187);
                sehirleriGetir();
            }
        }, 500);
    }

    protected void sehirleriGetir() {
        final SharedPreferences sharedPreferences = this.getSharedPreferences("sharedPref",Context.MODE_PRIVATE);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {// Süre sonunda burada yer alan kodlar çalışır.
                String sehirler = sharedPreferences.getString("sehirler", "Şehirler Bulunamadı.");
                System.out.println("HAKANBEY: " + sehirler);
                sehirlerList = Arrays.asList(gson.fromJson(sehirler, com.namazvakitleri.app.entity.sehirler[].class));
                String[] arraySpinner = new String[sehirlerList.size()];
                for (int i = 0; i < sehirlerList.size(); i++) {
                    arraySpinner[i] = "[" + sehirlerList.get(i).getSehirID() + "]- " + sehirlerList.get(i).getSehirAdi();
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                        R.layout.spinner_item, arraySpinner);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_sehir.setAdapter(adapter);
                ilceleriGetir();
                kurulum_button.setEnabled(true);
            }
        }, 500);
    }

    protected void ilceleriGetir() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {// Süre sonunda burada yer alan kodlar çalışır.
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
                spinner_ilce.setAdapter(adapter);
                kurulum_button.setEnabled(true);
            }
        }, 500);

    }

    // VERİLERİ ÇEKMEK İÇİN GEREKLİ OLAN CLASS YAPILARI

    private class getCountryList extends AsyncTask<Void, Void, Void> {
        List<ulkeler> geciciUlke = new ArrayList<ulkeler>();
        String jsonString;

        @Override
        protected void onPreExecute() { // işlem başladığında gerçekleşecek olanlar
            /*progressDialog1 = new ProgressDialog(activity_settings_country.this);
            progressDialog1.setMessage("Lütfen ülkeler indirilirken bekleyiniz..");
            progressDialog1.setCancelable(false);
            //progressDialog1.show();*/
        }

        @Override
        protected void onPostExecute(Void aVoid) { // işlem gerçekleştikten sonra yapılacak olan işlemler
            super.onPostExecute(aVoid);
            /*if (progressDialog1.isShowing()) {
                progressDialog1.dismiss();
            }*/
            new getCityList().execute();
        }


        @Override
        protected Void doInBackground(Void... params) { // işlemlerin gerçekleşme anı
            httpHandler = new HttpHandler();
            jsonString = httpHandler.makeServiceCall(JSON_URL_COUNTRY);
            if (null != jsonString) {
                geciciUlke = Arrays.asList(gson.fromJson(jsonString, ulkeler[].class));
                for (int i = 0; i < geciciUlke.size(); i++) {
                    ulkelerList.add(geciciUlke.get(i));
                }
                editor.putString("ulkeler", jsonString);
                editor.commit();
            }
            return null;
        }
    }

    private class getCityList extends AsyncTask<Void, Void, Void> {
        List<sehirler> geciciSehir = new ArrayList<sehirler>();
        String jsonString;

        @Override
        protected void onPreExecute() { // işlem başladığında gerçekleşecek olanlar
            /*progressDialog2 = new ProgressDialog(activity_settings_city.this);
            progressDialog2.setMessage("Lütfen şehirler indirilirken bekleyiniz..");
            progressDialog2.setCancelable(false);
            //progressDialog2.show();*/
            String id = sharedPref.getString("ulkem_id", "2");
            JSON_URL_CITY = "http://ezanvakti.herokuapp.com/sehirler?ulke=" + id;
        }

        @Override
        protected void onPostExecute(Void aVoid) { // işlem gerçekleştikten sonra yapılacak olan işlemler
            super.onPostExecute(aVoid);
            /*if (progressDialog2.isShowing()) {
                progressDialog2.dismiss();
            }*/
            new getDistrictList().execute();
        }


        @Override
        protected Void doInBackground(Void... params) { // işlemlerin gerçekleşme anı
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
    }

    private class getDistrictList extends AsyncTask<Void, Void, Void> {
        List<ilceler> geciciIlce = new ArrayList<ilceler>();
        String jsonString;

        @Override
        protected void onPreExecute() { // işlem başladığında gerçekleşecek olanlar
            /*progressDialog3 = new ProgressDialog(activity_settings_district.this);
            progressDialog3.setMessage("Lütfen ilçeler indirilirken bekleyiniz..");
            progressDialog3.setCancelable(false);
            //progressDialog3.show();*/
            String id = sharedPref.getString("sehrim_id", "Şehir İD Bulunamadı");
            JSON_URL_DISTRICT = "https://ezanvakti.herokuapp.com/ilceler?sehir=" + id;
        }

        @Override
        protected void onPostExecute(Void aVoid) { // işlem gerçekleştikten sonra yapılacak olan işlemler
            super.onPostExecute(aVoid);
            /*if (progressDialog3.isShowing()) {
                progressDialog3.dismiss();
            }*/
            ulkeleriGetir();
        }


        @Override
        protected Void doInBackground(Void... params) { // işlemlerin gerçekleşme anı
            httpHandler = new HttpHandler();
            jsonString = httpHandler.makeServiceCall(JSON_URL_DISTRICT);

            if (null != jsonString) {
                geciciIlce = Arrays.asList(gson.fromJson(jsonString, ilceler[].class));
                for (int i = 0; i < geciciIlce.size(); i++) {
                    ilcelerList.add(geciciIlce.get(i));
                }
                editor.putString("ilceler", jsonString);
                editor.commit();
            }
            return null;
        }
    }


}