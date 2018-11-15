package com.cisco.user.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cisco.user.R;
import com.cisco.user.models.InsertValue;
import com.cisco.user.models.Kelas;
import com.cisco.user.models.Value;
import com.cisco.user.services.APIServices;
import com.cisco.user.services.DBHandler;
import com.cisco.user.utils.Utilities;
import com.gun0912.tedpermission.TedPermissionResult;
import com.iceteck.silicompressorr.SiliCompressor;
import com.tedpark.tedpermission.rx2.TedRx2Permission;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Taketheclassroom extends AppCompatActivity {
    Spinner namakelas;
    TextView lbdeskripsi, fileupload;
    Button btnupload;
    private static final int GALLERY_PICTURE_REQUEST = 997;
    private static final int CAMERA_PICTURE_REQUEST = 998;
    RelativeLayout daftarkelas, rlimage;
    public static String[] idinstrukturkelas;
    public static String[] nm_kelas;
    public static String[] namadetailkelas;
    public static String[] harga;
    public static String[] namains;
    public static String[] alamat;
    public static String[] nohp;
    public static String[] email;
    public static String[] minkelas;
    public static String[] maxkelas;
    public static String[] ketwaktu;
    public static String[] tanggal;
    String idtampung, hargatampung, photos = "", strimage = "";
    ImageView imgbuktibayar;
    Bitmap mBitmap;
    ProgressDialog pDialog;
    private String mPictureImagePath;
    DBHandler dbHandler = new DBHandler(Taketheclassroom.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taketheclassroom);

        namakelas = findViewById(R.id.namakelas);
        lbdeskripsi = findViewById(R.id.lbdeskripsi);
        fileupload = findViewById(R.id.fileupload);
        imgbuktibayar = findViewById(R.id.imgbuktibayar);
        daftarkelas = findViewById(R.id.daftarkelas);
        btnupload = findViewById(R.id.btnupload);
        rlimage = findViewById(R.id.rlimage);

        photos = "";

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Pengambilan Kelas");
        }

        if (dbHandler.findAllNamaKelas().isEmpty()) {
            idinstrukturkelas = new String[1];
            idinstrukturkelas[0] = "Pilih Kelas";
        } else {
            List<Kelas> kelas = dbHandler.findAllNamaKelas();
            idinstrukturkelas = new String[kelas.size() + 1];
            nm_kelas = new String[kelas.size()];
            namadetailkelas = new String[kelas.size()];
            harga = new String[kelas.size()];
            namains = new String[kelas.size()];
            alamat = new String[kelas.size()];
            nohp = new String[kelas.size()];
            email = new String[kelas.size()];
            minkelas = new String[kelas.size()];
            maxkelas = new String[kelas.size()];
            ketwaktu = new String[kelas.size()];
            tanggal = new String[kelas.size()];
            idinstrukturkelas[0] = "Pilih Kelas";

            int x = 1;
            for (int y = 0; y < kelas.size(); y++) {
                idinstrukturkelas[x] = kelas.get(y).getIdinstrukturkelas();
                nm_kelas[y] = kelas.get(y).getNm_kelas();
                namadetailkelas[y] = kelas.get(y).getNamadetailkelas();
                harga[y] = kelas.get(y).getHarga();
                namains[y] = kelas.get(y).getNamains();
                alamat[y] = kelas.get(y).getAlamat();
                nohp[y] = kelas.get(y).getNohp();
                email[y] = kelas.get(y).getEmail();
                minkelas[y] = kelas.get(y).getMinkelas();
                maxkelas[y] = kelas.get(y).getMaxkelas();
                ketwaktu[y] = kelas.get(y).getKetwaktu();
                tanggal[y] = kelas.get(y).getTanggal();
                x++;
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(Taketheclassroom.this, android.R.layout.simple_spinner_dropdown_item, idinstrukturkelas);
        namakelas.setAdapter(adapter);

        namakelas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()

        {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int m, long id) {
                if (!(m == 0)) {
                    lbdeskripsi.setText(nm_kelas[m - 1] + " (" + namadetailkelas[m - 1] + ")" + "\nInstruktur : " + namains[m - 1] + "\n\n" +
                            ketwaktu[m - 1] + "\n\n" + Utilities.getCurrency(harga[m - 1]));
                    idtampung = idinstrukturkelas[m];
                    hargatampung = harga[m - 1];
                } else {
                    lbdeskripsi.setText("");
                    idtampung = "";
                    hargatampung = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnupload.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                final CharSequence[] options = {"Kamera", "Ambil dari Galeri", "Batal"};
                AlertDialog.Builder builder = new AlertDialog.Builder(Taketheclassroom.this);
                builder.setTitle("Upload Bukti Bayar");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Kamera")) {
                            TedRx2Permission.with(Taketheclassroom.this)
                                    .setRationaleTitle("Izin Akses")
                                    .setRationaleMessage("Untuk mengakses fitur kamera harap izinkan kamera dan penyimpanan")
                                    .setPermissions(android.Manifest.permission.CAMERA, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                                    .request()
                                    .subscribe(new Observer<TedPermissionResult>() {
                                        @Override
                                        public void onSubscribe(Disposable d) {

                                        }

                                        @Override
                                        public void onNext(TedPermissionResult tedPermissionResult) {
                                            if (tedPermissionResult.isGranted()) {
                                                CapturePhoto();
                                            } else {
                                                Snackbar.make(getWindow().getDecorView().getRootView(),
                                                        R.string.text_ask_permission_camera,
                                                        Snackbar.LENGTH_INDEFINITE)
                                                        .setAction(getString(R.string.action_settings), new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                Intent intent = new Intent();
                                                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                                                Uri uri = Uri.fromParts("package", Taketheclassroom.this.getPackageName(), null);
                                                                intent.setData(uri);
                                                                startActivity(intent);
                                                            }
                                                        })
                                                        .show();
                                            }
                                        }

                                        @Override
                                        public void onError(Throwable e) {

                                        }

                                        @Override
                                        public void onComplete() {

                                        }
                                    });
                        } else if (options[item].equals("Ambil dari Galeri")) {
                            TedRx2Permission.with(Taketheclassroom.this)
                                    .setRationaleTitle("Izin Akses")
                                    .setRationaleMessage("Untuk mengakses file gambar harap izinkan penyimpanan")
                                    .setPermissions(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                                    .request()
                                    .subscribe(new Observer<TedPermissionResult>() {
                                        @Override
                                        public void onSubscribe(Disposable d) {

                                        }

                                        @Override
                                        public void onNext(TedPermissionResult tedPermissionResult) {
                                            if (tedPermissionResult.isGranted()) {
                                                OpenImage();
                                            } else {
                                                Snackbar.make(getWindow().getDecorView().getRootView(),
                                                        R.string.text_ask_permission_storege,
                                                        Snackbar.LENGTH_INDEFINITE)
                                                        .setAction(getString(R.string.action_settings), new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                Intent intent = new Intent();
                                                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                                                Uri uri = Uri.fromParts("package", Taketheclassroom.this.getPackageName(), null);
                                                                intent.setData(uri);
                                                                startActivity(intent);
                                                            }
                                                        })
                                                        .show();
                                            }
                                        }

                                        @Override
                                        public void onError(Throwable e) {

                                        }

                                        @Override
                                        public void onComplete() {

                                        }

                                    });
                        } else if (options[item].equals("Batal")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            }
        });
        daftarkelas.setOnClickListener(new View.OnClickListener()

        {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                pDialog = new ProgressDialog(Taketheclassroom.this);
                pDialog.setTitle("Loading...");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(false);
                pDialog.show();
                if (Utilities.isNetworkAvailable(Taketheclassroom.this)) {
                    if (!idtampung.equals("")) {
                        if (!photos.isEmpty())
                            setKelas(Utilities.getUser(Taketheclassroom.this).getNim(), idtampung, photos, strimage, "0", hargatampung);
                        else
                            setKelas(Utilities.getUser(Taketheclassroom.this).getNim(), idtampung, "", "", "0", hargatampung);
                    } else {
                        Utilities.showAsToast(Taketheclassroom.this, "Kelas belum dipilih");
                        pDialog.dismiss();
                    }
                } else {
                    pDialog.dismiss();
                    Utilities.showAsToast(Taketheclassroom.this, "Tidak Ada Koneksi Internet");
                }
            }
        });
    }

    public void CapturePhoto() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                getResources().getString(R.string.uri_image_bukti);
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "bukti_" + timeStamp + ".jpg";
        mPictureImagePath = path + imageFileName;
        photos = imageFileName;
        File file = new File(mPictureImagePath);
        Uri outputFileUri = Uri.fromFile(file);

        if (Build.VERSION.SDK_INT >= 24) {
            try {
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            Intent captureImageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            captureImageIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            startActivityForResult(captureImageIntent, CAMERA_PICTURE_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void OpenImage() {
        try {
            Intent openImageIntent = new Intent(Intent.ACTION_PICK);
            openImageIntent.setType("image/*");
            startActivityForResult(Intent.createChooser(openImageIntent, "Select Image .."), GALLERY_PICTURE_REQUEST);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String dirpath = Environment.getExternalStorageDirectory().getAbsolutePath() +
                    getResources().getString(R.string.uri_image_bukti);
            String path = null;
            if (requestCode == GALLERY_PICTURE_REQUEST && data != null) {
                path = dirpath;
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                Uri selectedImage = data.getData();
                String paths = Utilities.getPath(Taketheclassroom.this, selectedImage);
                mBitmap = Utilities.getBitmapFromPath(paths);

                @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "user_" + timeStamp + ".png";

                photos = imageFileName;

                path = paths;

                File file = new File(path + imageFileName);

                try {
                    file.createNewFile();
                    OutputStream out = new FileOutputStream(file);
                    out.write(Utilities.getStringFromBitmap(mBitmap));
                    out.close();
                    path = path + imageFileName;
                } catch (IOException e) {
                    e.printStackTrace();
                }


            } else if (requestCode == CAMERA_PICTURE_REQUEST) {
                path = mPictureImagePath;
                mBitmap = Utilities.getBitmapFromPath(path);

                File file = new File(path);

                try {
                    file.createNewFile();
                    OutputStream out = new FileOutputStream(file);
                    out.write(Utilities.getStringFromBitmap(mBitmap));
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            mBitmap=Bitmap.createScaledBitmap(mBitmap,500,500,true);
            if (path != null) {
                //String filePath = SiliCompressor.with(Taketheclassroom.this).compress(path, new File(dirpath), true);
                rlimage.setVisibility(View.GONE);
                Glide.with(Taketheclassroom.this)
                        .load(new File(path))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imgbuktibayar);
                //photos = new File(filePath).getName();
                fileupload.setText(photos);
                strimage = Utilities.getArrayByteFromBitmap(mBitmap);
            }
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public void setKelas(final String nim, String id, String imageName, String gambar, String statuspembayaran, String harga) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utilities.getBaseURLUser())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APIServices apiService = retrofit.create(APIServices.class);
        Call<Value<InsertValue>> call2 = apiService.setkelas("b5e5ac98-f40d-44b5-a5a9-0b3a37382b05", nim, id, imageName, gambar, statuspembayaran, harga);
        call2.enqueue(new Callback<Value<InsertValue>>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(@NonNull Call<Value<InsertValue>> call2, @NonNull Response<Value<InsertValue>> response) {
                if (response.body() != null) {
                    assert response.body() != null;
                    int success = Objects.requireNonNull(response.body()).getSuccess();
                    if (success == 1) {
                        if (!nim.equals("")) {
                            Utilities.showAsToast(Taketheclassroom.this, "Kelas Selesai dipilih");
                            Intent intent = new Intent(Taketheclassroom.this, MainActivity.class)
                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                            pDialog.dismiss();
                        }
                    } else {
                        Utilities.showAsToast(Taketheclassroom.this, "Gagal mengambil data");
                        pDialog.dismiss();
                    }
                } else {
                    pDialog.dismiss();
                    Utilities.showAsToast(Taketheclassroom.this, "Gagal mengambil data");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Value<InsertValue>> call2, @NonNull Throwable t) {
                System.out.println("Retrofit Error:" + t.getMessage());
                Utilities.showAsToast(Taketheclassroom.this, "Tidak ada koneksi internet");
                pDialog.dismiss();
            }
        });
    }
}
