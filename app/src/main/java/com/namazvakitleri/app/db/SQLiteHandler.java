package com.namazvakitleri.app.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.namazvakitleri.app.entity.ulkeler;

import java.util.ArrayList;
import java.util.List;

public class SQLiteHandler extends SQLiteOpenHelper {
    public static final String TAG = SQLiteHandler.class.getSimpleName();
    // veritabanı versiyon kontrolü
    private static int DATABASE_VERSION = 1;
    // veritabanı ismi
    private static String DATABASE_NAME = "ezanvakitleri";
    // tablo isimleri
    private static String TABLE_NAME_ULKE = "ulkeler";
    private static String TABLE_NAME_SEHIR = "sehirler";
    private static String TABLE_NAME_ILCE = "ilceler";
    private static String TABLE_NAME_VAKIT = "vakitler";
    // ülke tablosuna ait sütunlar
    private static final String ULKE_ID = "ulke_id";
    private static final String ULKE_ISIM = "ulke_isim";
    // şehir tablosuna ait sütunlar
    private static final String SEHIR_ID = "sehir_id";
    private static final String SEHIR_ISIM = "sehir_isim";
    // ilçe tablosuna ait sütunlar
    private static final String ILCE_ID = "ilce_id";
    private static final String ILCE_ISIM = "ilce_isim";
    // vakitler tablosuna ait sütunlar
    private static final String VAKIT_ID = "vakit_id";
    private static final String VAKIT_AKSAM = "vakit_aksam";
    private static final String VAKIT_AYIN_SEKLI_URL = "ayin_sekli_url";
    private static final String VAKIT_GUNES = "vakit_gunes";
    private static final String VAKIT_GUNESBATIS = "vakit_gunesbatis";
    private static final String VAKIT_GUNESDOGUS = "vakit_gunesdogus";
    private static final String VAKIT_HICRI_TARIH_KISA = "vakit_hicri_tarih_kisa";
    private static final String VAKIT_HICRI_TARIH_UZUN = "vakit_hicri_tarih_uzun";
    private static final String VAKIT_IKINDI = "vakit_ikindi";
    private static final String VAKIT_IMSAK = "vakit_imsak";
    private static final String VAKIT_KIBLE_SAATI = "vakit_kible_saati";
    private static final String VAKIT_MILADI_TARIH_KISA = "vakit_miladi_tarih_kisa";
    private static final String VAKIT_MILADI_TARIH_KISA_ISO8601 = "vakit_miladi_tarih_kisa_iso8601";
    private static final String VAKIT_MILADI_TARIH_UZUN = "vakit_miladi_tarih_uzun";
    private static final String VAKIT_MILADI_TARIH_UZUN_ISO8601 = "vakit_miladi_tarih_uzun_iso8601";
    private static final String VAKIT_OGLE = "vakit_ogle";
    private static final String VAKIT_YATSI = "vakit_yatsi";

    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE_ULKE = "CREATE TABLE " + TABLE_NAME_ULKE + "(" +
                ULKE_ID + " INTEGER PRIMARY KEY," +
                ULKE_ISIM + "VARCHAR)";
        sqLiteDatabase.execSQL(CREATE_TABLE_ULKE);

        String CREATE_TABLE_SEHIR = "CREATE TABLE " + TABLE_NAME_SEHIR + "(" +
                SEHIR_ID + " INTEGER PRIMARY KEY," +
                SEHIR_ISIM + " TEXT)";
        sqLiteDatabase.execSQL(CREATE_TABLE_SEHIR);

        String CREATE_TABLE_ILCE = "CREATE TABLE " + TABLE_NAME_ILCE + "(" +
                ILCE_ID + " INTEGER PRIMARY KEY," +
                ILCE_ISIM + " TEXT)";
        sqLiteDatabase.execSQL(CREATE_TABLE_ILCE);

        String CREATE_TABLE_VAKITLER = "CREATE TABLE " + TABLE_NAME_VAKIT + "(" +
                VAKIT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                VAKIT_AKSAM + " TEXT," +
                VAKIT_AYIN_SEKLI_URL + " TEXT," +
                VAKIT_GUNES + " TEXT," +
                VAKIT_GUNESBATIS + " TEXT," +
                VAKIT_GUNESDOGUS + " TEXT," +
                VAKIT_HICRI_TARIH_KISA + " TEXT," +
                VAKIT_HICRI_TARIH_UZUN + " TEXT," +
                VAKIT_IKINDI + " TEXT," +
                VAKIT_IMSAK + " TEXT," +
                VAKIT_KIBLE_SAATI + " TEXT," +
                VAKIT_MILADI_TARIH_KISA + " TEXT," +
                VAKIT_MILADI_TARIH_KISA_ISO8601 + " TEXT," +
                VAKIT_MILADI_TARIH_UZUN + " TEXT," +
                VAKIT_MILADI_TARIH_UZUN_ISO8601 + " TEXT," +
                VAKIT_OGLE + " TEXT," +
                VAKIT_YATSI + " TEXT)";
        sqLiteDatabase.execSQL(CREATE_TABLE_VAKITLER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(" DROP TABLE IF EXISTS " + TABLE_NAME_ULKE);
        sqLiteDatabase.execSQL(" DROP TABLE IF EXISTS " + TABLE_NAME_SEHIR);
        sqLiteDatabase.execSQL(" DROP TABLE IF EXISTS " + TABLE_NAME_ILCE);
        sqLiteDatabase.execSQL(" DROP TABLE IF EXISTS " + TABLE_NAME_VAKIT);
        onCreate(sqLiteDatabase);
    }

    public void ulkeEkle(int ulke_id, String ulke_isim) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ULKE_ID, ulke_id);
        values.put(ULKE_ISIM, ulke_isim);
        db.insert(TABLE_NAME_ULKE, null, values);
        db.close();
        Log.d(TAG, "ülke verisi eklendi: ");
    }

    public List<ulkeler> ulkeleriGetir() {
        List<ulkeler> veriler = new ArrayList<ulkeler>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                ULKE_ID,
                ULKE_ISIM};

        Cursor c = db.query(TABLE_NAME_ULKE, projection, null, null, null, null, null);
        while (c.moveToNext()) {
            ulkeler temp = new ulkeler();
            temp.setUlkeID(c.getString(c.getColumnIndex(ULKE_ID)));
            temp.setUlkeAdi(c.getString(c.getColumnIndex(ULKE_ISIM)));
            veriler.add(temp);
        }
        c.close();
        db.close();
        return veriler;
    }

    public void sehirEkle(int sehir_id, String sehir_isim) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ULKE_ID, sehir_id);
        values.put(ULKE_ISIM, sehir_isim);
        long id = db.insert(TABLE_NAME_SEHIR, null, values);
        db.close();
        Log.d("şehir verisi eklendi: ", String.valueOf(id));
    }

    public void ilceEkle(int ilce_id, String ilce_isim) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ILCE_ID, ilce_id);
        values.put(ILCE_ISIM, ilce_isim);
        long id = db.insert(TABLE_NAME_ILCE, null, values);
        db.close();
        Log.d("ilçe verisi eklendi: ", String.valueOf(id));
    }

    public void vakitlerEkle(String vakit_aksam, String ayin_sekli_url, String vakit_gunes, String vakit_gunes_batis, String vakit_gunes_dogus, String vakit_hicri_tarih_kisa, String vakit_hicri_tarih_uzun, String vakit_ikindi, String vakit_imsak, String vakit_kible_saati, String vakit_miladi_tarih_kisa, String vakit_miladi_tarih_kisa_iso8601, String vakit_miladi_tarih_uzun, String vakit_miladi_tarih_uzun_iso8601, String vakit_ogle, String vakit_yatsi) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(VAKIT_AKSAM, vakit_aksam);
        values.put(VAKIT_AYIN_SEKLI_URL, ayin_sekli_url);
        values.put(VAKIT_GUNES, vakit_gunes);
        values.put(VAKIT_GUNESBATIS, vakit_gunes_batis);
        values.put(VAKIT_GUNESDOGUS, vakit_gunes_dogus);
        values.put(VAKIT_HICRI_TARIH_KISA, vakit_hicri_tarih_kisa);
        values.put(VAKIT_HICRI_TARIH_UZUN, vakit_hicri_tarih_uzun);
        values.put(VAKIT_IKINDI, vakit_ikindi);
        values.put(VAKIT_IMSAK, vakit_imsak);
        values.put(VAKIT_KIBLE_SAATI, vakit_kible_saati);
        values.put(VAKIT_MILADI_TARIH_KISA, vakit_miladi_tarih_kisa);
        values.put(VAKIT_MILADI_TARIH_KISA_ISO8601, vakit_miladi_tarih_kisa_iso8601);
        values.put(VAKIT_MILADI_TARIH_UZUN, vakit_miladi_tarih_uzun);
        values.put(VAKIT_MILADI_TARIH_UZUN_ISO8601, vakit_miladi_tarih_uzun_iso8601);
        values.put(VAKIT_OGLE, vakit_ogle);
        values.put(VAKIT_YATSI, vakit_yatsi);
        long id = db.insert(TABLE_NAME_VAKIT, null, values);
        db.close();
        Log.d("vakit verisi eklendi: ", String.valueOf(id));
    }

    public void verileriSil() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME_ULKE, null, null);
        db.delete(TABLE_NAME_SEHIR, null, null);
        db.delete(TABLE_NAME_ILCE, null, null);
        db.delete(TABLE_NAME_VAKIT, null, null);
        db.close();
        Log.d(TAG, "Bütün veriler silindi.");
    }

}
