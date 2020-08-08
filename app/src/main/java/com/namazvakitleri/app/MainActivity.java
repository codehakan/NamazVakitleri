package com.namazvakitleri.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.app.ApplicationErrorReport;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.RingtoneManager;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ExpandableListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.namazvakitleri.app.bottomnavi.bottom_menu;
import com.namazvakitleri.app.bottomnavi.bottom_notification;
import com.namazvakitleri.app.install.activity_settings_country;
import com.namazvakitleri.app.adapter.namazvakitleriAdapter;
import com.namazvakitleri.app.entity.namazvakitleri;
import com.namazvakitleri.app.services.NotificationService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    List<namazvakitleri> namazvakitleriList;
    namazvakitleriAdapter namazvakitleriAdapter;
    namazvakitleri bugununVakitleri;
    // veri yazma ve okuma için gerekli olanlar
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    Gson gson = new Gson();
    BottomNavigationView bottomNavigationView;

    ListView listview_vakitler;
    TextView txt_miladi_tarih, txt_hicri_tarih, txt_kalan_sure;
    String bugun, bugunsaat;
    Date simdikiZaman;

    public static long KALAN_ZAMAN = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        sharedPref = this.getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        namazvakitleriList = new ArrayList<namazvakitleri>();
        listview_vakitler = findViewById(R.id.listview_vakitler);

        txt_hicri_tarih = findViewById(R.id.txt_hicri_tarih);
        txt_miladi_tarih = findViewById(R.id.txt_miladi_tarih);
        txt_kalan_sure = findViewById(R.id.txt_kalan_sure);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        // ana sayfa seçili
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);

        bugununVakitleri = new namazvakitleri();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home: {
                        return true;
                    }
                    case R.id.navigation_dashboard: {
                        startActivity(new Intent(MainActivity.this, bottom_menu.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    }
                    case R.id.navigation_notifications: {
                        startActivity(new Intent(MainActivity.this, bottom_notification.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    }
                }
                return false;
            }
        });

        // tanımlamalar
        simdikiZaman = new Date();
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        bugun = df.format(simdikiZaman);
        df = new SimpleDateFormat("HH:mm.ss");
        bugunsaat = df.format(simdikiZaman);

        boolean ilkAcilis = sharedPref.getBoolean("ilkAcilis", true);
        if (ilkAcilis) {
            Intent intent = new Intent(MainActivity.this, activity_settings_country.class);
            startActivity(intent);
            finish();
        } else {
            namazVakitleriniGetir();
        }
        // yanıp sönen animasyon
        Animation anim = new AlphaAnimation(0.2f, 1.0f);
        anim.setDuration(500); //You can manage the time of the blink with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        txt_kalan_sure.startAnimation(anim);
        // yanıp sönen animasyon
    }

    public int daysBetween(Date d1, Date d2) {
        return (int) ((d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
    }

    public int hoursBetween(Date d1, Date d2) {
        return (int) ((d2.getTime() - d1.getTime()));
    }


    private void namazVakitleriniGetir() {
        try {
            String namazVakitleri = sharedPref.getString("namazvakitleri", "Vakitler Bulunamadı.");
            if (!namazVakitleri.equals("Vakitler Bulunamadı.")) {
                //System.out.println("HAKANBEY: " + namazVakitleri);
                namazvakitleriList = Arrays.asList(gson.fromJson(namazVakitleri, namazvakitleri[].class));
                if (namazvakitleriList != null) {
                    for (int i = 0; i < namazvakitleriList.size(); i++) {
                        if (namazvakitleriList.get(i).getMiladiTarihKisa().equals(bugun)) {
                            bugununVakitleri = namazvakitleriList.get(i);
                            break;
                        }
                    }
                    // son 3 günlük namaz vakti varsa
                    Calendar cal1 = new GregorianCalendar();
                    Calendar cal2 = new GregorianCalendar();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                    Date date = sdf.parse(bugun);
                    cal1.setTime(date);
                    date = sdf.parse(namazvakitleriList.get(namazvakitleriList.size() - 2).getMiladiTarihKisa());
                    cal2.setTime(date);
                    // son 3 günlük namaz vakti varsa
                    if (daysBetween(cal1.getTime(), cal2.getTime()) <= 3) {
                        Toast.makeText(this, "Namaz verilerinin yüklenebilmesi için şehir bilgilerinizi tekrar girmeniz gerekiyor. " +
                                "Kurulum ekranına yönlendiriliyorsunuz..", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(MainActivity.this, activity_settings_country.class);
                        startActivity(intent);
                        finish();
                    } else if (bugununVakitleri != null) {
                        List<namazvakitleri> temp = new ArrayList<namazvakitleri>();
                        temp.add(bugununVakitleri);
                        namazvakitleriAdapter = new namazvakitleriAdapter(this, temp);
                        listview_vakitler.setAdapter(namazvakitleriAdapter);
                        String sehrim = sharedPref.getString("sehrim_isim", "ŞEHİR YOK");
                        String ilcem = sharedPref.getString("ilcem_isim", "İLÇE YOK");
                        getSupportActionBar().setTitle(sehrim + " /" + ilcem);

                        txt_miladi_tarih.setText(bugununVakitleri.getMiladiTarihUzun());
                        txt_hicri_tarih.setText(bugununVakitleri.getHicriTarihUzun());

                        // kalan zamanı gösterme
                        int saat = Integer.parseInt(bugunsaat.substring(0, bugunsaat.indexOf(':')));
                        int dakika = Integer.parseInt(bugunsaat.substring(bugunsaat.indexOf(':') + 1, bugunsaat.indexOf('.')));
                        int saniye = Integer.parseInt(bugunsaat.substring(bugunsaat.indexOf('.') + 1));
                        //Toast.makeText(this, "Saat: " + saat + ", dakika: " + dakika + ", saniye: " + saniye, Toast.LENGTH_SHORT).show();


                        Calendar simdiki_vakit = new GregorianCalendar();
                        simdiki_vakit.set(Calendar.HOUR_OF_DAY, saat);
                        simdiki_vakit.set(Calendar.MINUTE, dakika);
                        simdiki_vakit.set(Calendar.SECOND, saniye);
                        //System.out.println("şimdiki vakit: " + simdiki_vakit.getTime());

                        Calendar imsak_vakti = new GregorianCalendar();
                        imsak_vakti.set(Calendar.HOUR_OF_DAY, Integer.parseInt(bugununVakitleri.getImsak().substring(1, bugununVakitleri.getImsak().indexOf(':'))));
                        imsak_vakti.set(Calendar.MINUTE, Integer.parseInt(bugununVakitleri.getImsak().substring(bugununVakitleri.getImsak().indexOf(':') + 1)));
                        imsak_vakti.set(Calendar.SECOND, 0);
                        //System.out.println("imsak vakti: " + imsak_vakti.getTime());

                        Calendar gunes_vakti = new GregorianCalendar();
                        gunes_vakti.set(Calendar.HOUR_OF_DAY, Integer.parseInt(bugununVakitleri.getGunes().substring(1, bugununVakitleri.getGunes().indexOf(':'))));
                        gunes_vakti.set(Calendar.MINUTE, Integer.parseInt(bugununVakitleri.getGunes().substring(bugununVakitleri.getGunes().indexOf(':') + 1)));
                        gunes_vakti.set(Calendar.SECOND, 0);
                        //System.out.println("Güneş Vakti: " + gunes_vakti.get(Calendar.HOUR_OF_DAY) + ":" + gunes_vakti.get(Calendar.MINUTE));

                        Calendar ogle_vakti = new GregorianCalendar();
                        ogle_vakti.set(Calendar.HOUR_OF_DAY, Integer.parseInt(bugununVakitleri.getOgle().substring(0, bugununVakitleri.getOgle().indexOf(':'))));
                        ogle_vakti.set(Calendar.MINUTE, Integer.parseInt(bugununVakitleri.getOgle().substring(bugununVakitleri.getOgle().indexOf(':') + 1)));
                        ogle_vakti.set(Calendar.SECOND, 0);
                        //System.out.println("Öğle Vakti: " + ogle_vakti.get(Calendar.HOUR_OF_DAY) + ":" + ogle_vakti.get(Calendar.MINUTE));

                        Calendar ikindi_vakti = new GregorianCalendar();
                        ikindi_vakti.set(Calendar.HOUR_OF_DAY, Integer.parseInt(bugununVakitleri.getIkindi().substring(0, bugununVakitleri.getIkindi().indexOf(':'))));
                        ikindi_vakti.set(Calendar.MINUTE, Integer.parseInt(bugununVakitleri.getIkindi().substring(bugununVakitleri.getIkindi().indexOf(':') + 1)));
                        ikindi_vakti.set(Calendar.SECOND, 0);
                        //System.out.println("İkindi Vakti: " + ikindi_vakti.get(Calendar.HOUR_OF_DAY) + ":" + ikindi_vakti.get(Calendar.MINUTE));

                        final Calendar aksam_vakti = new GregorianCalendar();
                        aksam_vakti.set(Calendar.HOUR_OF_DAY, Integer.parseInt(bugununVakitleri.getAksam().substring(0, bugununVakitleri.getAksam().indexOf(':'))));
                        aksam_vakti.set(Calendar.MINUTE, Integer.parseInt(bugununVakitleri.getAksam().substring(bugununVakitleri.getAksam().indexOf(':') + 1)));
                        aksam_vakti.set(Calendar.SECOND, 0);
                        //System.out.println("Akşam Vakti: " + aksam_vakti.get(Calendar.HOUR_OF_DAY) + ":" + aksam_vakti.get(Calendar.MINUTE));

                        Calendar yatsi_vakti = new GregorianCalendar();
                        yatsi_vakti.set(Calendar.HOUR_OF_DAY, Integer.parseInt(bugununVakitleri.getYatsi().substring(0, bugununVakitleri.getYatsi().indexOf(':'))));
                        yatsi_vakti.set(Calendar.MINUTE, Integer.parseInt(bugununVakitleri.getYatsi().substring(bugununVakitleri.getYatsi().indexOf(':') + 1)));
                        yatsi_vakti.set(Calendar.SECOND, 0);
                        //System.out.println("Yatsı Vakti: " + yatsi_vakti.get(Calendar.HOUR_OF_DAY) + ":" + yatsi_vakti.get(Calendar.MINUTE));


                        String vakit_ismi = null;
                        long kalanzaman = 0;
                        if (!(hoursBetween(simdiki_vakit.getTime(), imsak_vakti.getTime()) < 0)) {
                            kalanzaman = hoursBetween(simdiki_vakit.getTime(), imsak_vakti.getTime());
                            vakit_ismi = "İmsak";
                        } else if (!(hoursBetween(simdiki_vakit.getTime(), gunes_vakti.getTime()) < 0)) {
                            kalanzaman = hoursBetween(simdiki_vakit.getTime(), gunes_vakti.getTime());
                            vakit_ismi = "Güneş";
                        } else if (!(hoursBetween(simdiki_vakit.getTime(), ogle_vakti.getTime()) < 0)) {
                            kalanzaman = hoursBetween(simdiki_vakit.getTime(), ogle_vakti.getTime());
                            vakit_ismi = "Öğle";
                        } else if (!(hoursBetween(simdiki_vakit.getTime(), ikindi_vakti.getTime()) < 0)) {
                            kalanzaman = hoursBetween(simdiki_vakit.getTime(), ikindi_vakti.getTime());
                            vakit_ismi = "İkindi";
                        } else if (!(hoursBetween(simdiki_vakit.getTime(), aksam_vakti.getTime()) < 0)) {
                            kalanzaman = hoursBetween(simdiki_vakit.getTime(), aksam_vakti.getTime());
                            vakit_ismi = "Akşam";
                        } else if (!(hoursBetween(simdiki_vakit.getTime(), yatsi_vakti.getTime()) < 0)) {
                            kalanzaman = hoursBetween(simdiki_vakit.getTime(), yatsi_vakti.getTime());
                            vakit_ismi = "Yatsı";
                        } else {
                            Calendar geceyarisi = new GregorianCalendar();
                            geceyarisi.set(Calendar.HOUR_OF_DAY, 0);
                            geceyarisi.set(Calendar.MINUTE, 0);
                            geceyarisi.set(Calendar.SECOND, 1);
                            kalanzaman = hoursBetween(simdiki_vakit.getTime(), imsak_vakti.getTime()) + 86399000;
                        }
                        // kalan zamanı gösterme
                        KALAN_ZAMAN = kalanzaman;
                        if (!servisCalisiyorMu()) {
                            startService(new Intent(MainActivity.this, NotificationService.class));
                        }
                        // geri sayım
                        final long finalKalanzaman = kalanzaman;
                        final long[] dakka = new long[1];
                        final long[] sanye = new long[1];
                        final String finalVakit_ismi = vakit_ismi;
                        new CountDownTimer(finalKalanzaman, 1000) {
                            public void onTick(long millisUntilFinished) {
                                dakka[0] = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60;
                                sanye[0] = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                                toMinutes(millisUntilFinished));
                                if (dakka[0] < 10) {
                                    txt_kalan_sure.setText("" + String.format("0%d:0%d:%d",
                                            TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60,
                                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                                            toMinutes(millisUntilFinished))));
                                    if (sanye[0] < 10) {
                                        txt_kalan_sure.setText("" + String.format("0%d:0%d:0%d",
                                                TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60,
                                                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                                                toMinutes(millisUntilFinished))));
                                    }
                                } else if (sanye[0] < 10) {
                                    txt_kalan_sure.setText("" + String.format("0%d:%d:0%d",
                                            TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60,
                                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                                            toMinutes(millisUntilFinished))));
                                } else {
                                    txt_kalan_sure.setText("" + String.format("0%d:%d:%d",
                                            TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60,
                                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                                            toMinutes(millisUntilFinished))));
                                }

                            }

                            public void onFinish() {
                                if (finalKalanzaman < 0) {
                                    txt_kalan_sure.setText("Hesaplama Hatası!");
                                } else {
                                    txt_kalan_sure.setText(finalVakit_ismi + " Vakti Girdi!");
                                    NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                    Notification.Builder builder = new Notification.Builder(MainActivity.this);
                                    builder.setContentTitle(finalVakit_ismi + " Vakti Girdi");
                                    builder.setContentText(finalVakit_ismi + " için ezan okundu. Haydi çok geçmeden namazımızı kılalım");
                                    builder.setSmallIcon(R.drawable.ic_launcher_foreground);
                                    builder.setAutoCancel(true);
                                    builder.setTicker("Ezan okunuyooor..");
                                    builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

                                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                                    PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 1, intent, 0);
                                    builder.setContentIntent(pendingIntent);

                                    Notification notification = builder.getNotification();
                                    notificationManager.notify(1, notification);

                                    new CountDownTimer(10000, 1000) {

                                        @Override
                                        public void onTick(long l) {

                                        }

                                        @Override
                                        public void onFinish() {
                                            Intent intent = new Intent(MainActivity.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }.start();
                                }
                            }
                        }.start();
                        // geri sayım
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Namaz Vakitlerini Getir Hatası: " + e.getMessage());
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.ayarlar: {
                Intent i = new Intent(MainActivity.this, activity_settings_country.class);
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


    public boolean servisCalisiyorMu() {
        ActivityManager servisYoneticisi = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo serviceInfo : servisYoneticisi.getRunningServices(Integer.MAX_VALUE)) {
            if (getApplicationContext().getPackageName().equals(serviceInfo.service.getPackageName())) {
                return true;
            }
        }
        return false;
    }

}