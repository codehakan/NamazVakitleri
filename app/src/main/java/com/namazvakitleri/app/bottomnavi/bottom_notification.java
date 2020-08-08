package com.namazvakitleri.app.bottomnavi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ListView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.namazvakitleri.app.MainActivity;
import com.namazvakitleri.app.R;
import com.namazvakitleri.app.adapter.bottom_notificationAdapter;
import com.namazvakitleri.app.adapter.namazvakitleriAdapter;
import com.namazvakitleri.app.entity.bottom_bildirim;
import com.namazvakitleri.app.install.activity_settings_country;

import java.util.ArrayList;
import java.util.List;

public class bottom_notification extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    ListView listView;
    bottom_notificationAdapter bottom_notificationAdapter;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_notification);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getSupportActionBar().setTitle("HATIRLATICI AYARLARI");

        sharedPreferences = this.getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        listView = findViewById(R.id.listview_bildirimler);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        // ana sayfa seçili
        bottomNavigationView.setSelectedItemId(R.id.navigation_notifications);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home: {
                        startActivity(new Intent(bottom_notification.this, MainActivity.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    }
                    case R.id.navigation_dashboard: {
                        startActivity(new Intent(bottom_notification.this, bottom_menu.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    }
                    case R.id.navigation_notifications: {
                        return true;
                    }
                }
                return false;
            }
        });

        bottom_bildirim temp = new bottom_bildirim();
        List<bottom_bildirim> tempList = new ArrayList<bottom_bildirim>();
        tempList.add(temp);
        bottom_notificationAdapter = new bottom_notificationAdapter(this, tempList);
        listView.setAdapter(bottom_notificationAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.ayarlar: {
                Intent i = new Intent(bottom_notification.this, activity_settings_country.class);
                startActivity(i);
                editor.putBoolean("ilkAcilis", true);
                editor.commit();
                finish();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}