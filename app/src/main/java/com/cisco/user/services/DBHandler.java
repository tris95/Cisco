package com.cisco.user.services;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.cisco.user.models.DataBukti;
import com.cisco.user.models.Jadwal;
import com.cisco.user.models.Kelas;
import com.cisco.user.models.Promosi;

import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "cisco";
    private static final String TABLE_promo = "tblpromosi", TABLE_namaKelas = "tblnamakelas", TABLE_jadwal = "tbljadwal", TABLE_bukti = "tblbukti";
    private static final String KEY_ID = "idinstrukturkelas", nm_kelas = "nm_kelas", namadetailkelas = "namadetailkelas",
            harga = "harga", namains = "namains", alamat = "alamat", nohp = "nohp", email = "email", minkelas = "minkelas",
            maxkelas = "maxkelas", jumlahkelas = "jumlahkelas", ketwaktu = "ketwaktu", tanggal = "tanggal", buktipembayaran = "buktipembayaran",
            id="id",judul = "judul", isi = "isi", penulis = "penulis", gambar = "gambar";


    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE;
        CREATE_CONTACTS_TABLE = "create table if not exists " + TABLE_promo + "(" +
                id + " TEXT," +judul + " TEXT," + isi + " TEXT," + penulis + " TEXT," + tanggal + " TEXT," + gambar + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);

        String CREATE_CONTACTS_TABLE1;
        CREATE_CONTACTS_TABLE1 = "create table if not exists " + TABLE_namaKelas + "(" +
                KEY_ID + " TEXT," + nm_kelas + " TEXT," + namadetailkelas + " TEXT," + harga + " TEXT," + namains + " TEXT," + alamat + " TEXT," +
                nohp + " TEXT," + email + " TEXT," + minkelas + " TEXT," + maxkelas + " TEXT," + jumlahkelas +
                " TEXT," + ketwaktu + " TEXT," + tanggal + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE1);

        String CREATE_CONTACTS_TABLE2;
        CREATE_CONTACTS_TABLE2 = "create table if not exists " + TABLE_jadwal + "(" +
                KEY_ID + " TEXT," + namains + " TEXT," + ketwaktu + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE2);

        String CREATE_CONTACTS_TABLE3;
        CREATE_CONTACTS_TABLE3 = "create table if not exists " + TABLE_bukti + "(" +
                KEY_ID + " TEXT," + namains + " TEXT," + buktipembayaran + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE3);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_promo);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_namaKelas);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_jadwal);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_bukti);
        onCreate(db);
    }

    public void addPromosi(List<Promosi> promosi) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        for (int i = 0; i < 5; i++) {
            values.put(id, promosi.get(i).getId());
            values.put(judul, promosi.get(i).getJudul());
            values.put(penulis, promosi.get(i).getPenulis());
            values.put(tanggal, promosi.get(i).getTanggal());
            values.put(gambar, promosi.get(i).getGambar());
            db.insert(TABLE_promo, null, values);
        }
        db.close();
    }

    public void addNamaKelas(List<Kelas> kelas) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        for (int i = 0; i < kelas.size(); i++) {
            values.put(KEY_ID, kelas.get(i).getIdinstrukturkelas());
            values.put(nm_kelas, kelas.get(i).getNm_kelas());
            values.put(namadetailkelas, kelas.get(i).getNamadetailkelas());
            values.put(harga, kelas.get(i).getHarga());
            values.put(namains, kelas.get(i).getNamains());
            values.put(alamat, kelas.get(i).getAlamat());
            values.put(nohp, kelas.get(i).getNohp());
            values.put(email, kelas.get(i).getEmail());
            values.put(minkelas, kelas.get(i).getMinkelas());
            values.put(maxkelas, kelas.get(i).getMaxkelas());
            values.put(jumlahkelas, kelas.get(i).getJumlahkelas());
            values.put(ketwaktu, kelas.get(i).getKetwaktu());
            values.put(tanggal, kelas.get(i).getTanggal());
            db.insert(TABLE_namaKelas, null, values);
        }
        db.close();
    }

    public void addJadwal(List<Jadwal> jadwal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        for (int i = 0; i < jadwal.size(); i++) {
            values.put(KEY_ID, jadwal.get(i).getIdinstrukturkelas());
            values.put(namains, jadwal.get(i).getNamains());
            values.put(ketwaktu, jadwal.get(i).getKetwaktu());
            db.insert(TABLE_jadwal, null, values);
        }
        db.close();
    }

    public void addBuktiBayar(List<DataBukti> dataBukti) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        for (int i = 0; i < dataBukti.size(); i++) {
            values.put(KEY_ID, dataBukti.get(i).getIdinstrukturkelas());
            values.put(namains, dataBukti.get(i).getNamains());
            values.put(buktipembayaran, dataBukti.get(i).getBuktipembayaran());
            db.insert(TABLE_bukti, null, values);
        }
        db.close();
    }

    public List<Promosi> findAllPromosi() {
        List<Promosi> promosiList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_promo;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Promosi promosi = new Promosi();
                promosi.setId(cursor.getString(0));
                promosi.setJudul(cursor.getString(1));
                promosi.setPenulis(cursor.getString(3));
                promosi.setTanggal(cursor.getString(4));
                promosi.setGambar(cursor.getString(5));
                promosiList.add(promosi);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return promosiList;
    }

    public List<Kelas> findAllNamaKelas() {
        List<Kelas> kelasList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_namaKelas;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Kelas kelas = new Kelas();
                kelas.setIdinstrukturkelas(cursor.getString(0));
                kelas.setNm_kelas(cursor.getString(1));
                kelas.setNamadetailkelas(cursor.getString(2));
                kelas.setHarga(cursor.getString(3));
                kelas.setNamains(cursor.getString(4));
                kelas.setAlamat(cursor.getString(5));
                kelas.setNohp(cursor.getString(6));
                kelas.setEmail(cursor.getString(7));
                kelas.setMinkelas(cursor.getString(8));
                kelas.setMaxkelas(cursor.getString(9));
                kelas.setJumlahkelas(cursor.getString(10));
                kelas.setKetwaktu(cursor.getString(11));
                kelas.setTanggal(cursor.getString(12));
                kelasList.add(kelas);
            } while (cursor.moveToNext());
            cursor.close();
        }

        return kelasList;
    }

    public List<Jadwal> findAllJadwal() {
        List<Jadwal> jadwalList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_jadwal;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Jadwal jadwal = new Jadwal();
                jadwal.setIdinstrukturkelas(cursor.getString(0));
                jadwal.setNamains(cursor.getString(1));
                jadwal.setKetwaktu(cursor.getString(2));
                jadwalList.add(jadwal);
            } while (cursor.moveToNext());
            cursor.close();
        }

        return jadwalList;
    }

    public List<DataBukti> findAllButiBayar() {
        List<DataBukti> buktibayarList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_bukti;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                DataBukti dataBukti = new DataBukti();
                dataBukti.setIdinstrukturkelas(cursor.getString(0));
                dataBukti.setNamains(cursor.getString(1));
                dataBukti.setBuktipembayaran(cursor.getString(2));
                buktibayarList.add(dataBukti);
            } while (cursor.moveToNext());
            cursor.close();
        }

        return buktibayarList;
    }

    public void deletePromosi() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_promo);
        db.close();
    }

    public void deleteNamaKelas() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_namaKelas);
        db.close();
    }

    public void deleteJadwal() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_jadwal);
        db.close();
    }

    public void deleteBukti() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_bukti);
        db.close();
    }

}
