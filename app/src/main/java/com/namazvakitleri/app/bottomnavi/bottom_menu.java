package com.namazvakitleri.app.bottomnavi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.namazvakitleri.app.MainActivity;
import com.namazvakitleri.app.R;

import java.util.ArrayList;
import java.util.List;

public class bottom_menu extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    ListView listView_menu;
    ImageButton btn_dini_gunler_ve_geceler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_menu);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getSupportActionBar().setTitle("YARDIMCI ARAÇLAR");

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        btn_dini_gunler_ve_geceler = findViewById(R.id.btn_dini_gunler_ve_geceler);


        /*
        listView_menu = findViewById(R.id.listview_menu);
        List<String> stringList = new ArrayList<String>();
        stringList.add("Dini Günler ve Geceler");
        stringList.add("Ezan Dinle");
        stringList.add("Cuma Mesajları");
        stringList.add("Kıble Pusulası");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, stringList);
        listView_menu.setAdapter(adapter);
        */

        // ana sayfa seçili
        bottomNavigationView.setSelectedItemId(R.id.navigation_dashboard);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home: {
                        startActivity(new Intent(bottom_menu.this, MainActivity.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    }
                    case R.id.navigation_dashboard: {
                        return true;
                    }
                    case R.id.navigation_notifications: {
                        startActivity(new Intent(bottom_menu.this, bottom_notification.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    }
                }
                return false;
            }
        });
    }
}