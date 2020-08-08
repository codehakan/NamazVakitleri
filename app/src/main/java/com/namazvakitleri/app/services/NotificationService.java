package com.namazvakitleri.app.services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.namazvakitleri.app.MainActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


public class NotificationService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    static long ZAMAN = MainActivity.KALAN_ZAMAN;
    Timer zamanlayici;
    Handler yardimci;

    @Override
    public void onCreate() {
        super.onCreate();
        zamanlayici = new Timer();
        yardimci = new Handler(Looper.myLooper());
        zamanlayici.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                bilgiver();
            }

        }, 0, ZAMAN);
    }

    public void bilgiver() {
        long zaman = java.lang.System.currentTimeMillis();
        SimpleDateFormat bilgi = new SimpleDateFormat("dd MMMM yyyy, EEEE / hh:mm");
        final String sonuc = bilgi.format(new Date(zaman));
        yardimci.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(NotificationService.this, "Arka planda çalışıyor!\n\nTarih: " + sonuc, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        zamanlayici.cancel();
    }
}
