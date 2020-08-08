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
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.namazvakitleri.app.db.HttpHandler;
import com.namazvakitleri.app.MainActivity;
import com.namazvakitleri.app.R;
import com.namazvakitleri.app.entity.ilceler;
import com.namazvakitleri.app.entity.namazvakitleri;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class activity_settings_vakitler extends AppCompatActivity {
    List<namazvakitleri> namazvakitleriList = new ArrayList<namazvakitleri>();

    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    TextView label_bilgilendirme;

    HttpHandler httpHandler;
    Gson gson;
    String ilce_kodu;
    String JSON_URL_NAMAZ = "https://ezanvakti.herokuapp.com/vakitler?ilce=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_vakitler);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        label_bilgilendirme = findViewById(R.id.label_bilgilendirme);
        sharedPref = this.getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        gson = new Gson();

        if(internetErisimi()){
            ilce_kodu = sharedPref.getString("ilcem_id", "9868");
            JSON_URL_NAMAZ = "https://ezanvakti.herokuapp.com/vakitler?ilce=" + ilce_kodu;
            //new getNamazList().execute();
            executeAsyncTask();
        }else{
            label_bilgilendirme.setText("Lütfen internet bağlantınızı aktif edip uygulamayı yeniden başlatınız." +
                    "\n\nUygulama 10 saniye içerisinde kapatılacaktır.");
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            },10000);
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

    public void executeAsyncTask() {
        @SuppressLint("StaticFieldLeak")
        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            List<namazvakitleri> namazvakitleriListGecici = new ArrayList<namazvakitleri>();
            String jsonString;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(Void... params) {
                httpHandler = new HttpHandler();
                jsonString = httpHandler.makeServiceCall(JSON_URL_NAMAZ);

                if (null != jsonString) {
                    namazvakitleriListGecici = Arrays.asList(gson.fromJson(jsonString, namazvakitleri[].class));
                    /*for (int i = 0; i < namazvakitleriListGecici.size(); i++) {
                        namazvakitleriList.add(namazvakitleriListGecici.get(i));
                        //System.out.println("VAKITLERI INDIREN SAYFA: " + namazvakitleriListGecici.get(i).getMiladiTarihKisa());
                    }*/
                    editor.putString("namazvakitleri", jsonString);
                    editor.commit();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String msg) {
                super.onPostExecute(msg);
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();
            }
        };

        if (Build.VERSION.SDK_INT >= 11/*HONEYCOMB*/) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            task.execute();
        }
    }

}