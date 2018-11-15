package com.cisco.user.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cisco.user.R;
import com.cisco.user.models.DataBukti;
import com.cisco.user.models.InsertValue;
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
import java.util.ArrayList;
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

public class BuktiBayar extends AppCompatActivity {
    DBHandler dbHandler = new DBHandler(BuktiBayar.this);
    LinearLayout llnojadwal,llbukti;
    TextView lbdeskripsi;
    TextView fileupload;
    TextView kirim;
    Button btnupload;
    ImageView imgbuktibayar;
    RelativeLayout kirimbukti, rlimage;
    ProgressDialog pDialog;

    private String strimage;
    private static final int GALLERY_PICTURE_REQUEST = 997;
    private static final int CAMERA_PICTURE_REQUEST = 998;
    private String photos = "", mPictureImagePath;
    private Bitmap mBitmap;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bukti_bayar);
        llnojadwal = findViewById(R.id.llnojadwal);
        llbukti=findViewById(R.id.llbukti);
        lbdeskripsi = findViewById(R.id.lbdeskripsi);
        kirim = findViewById(R.id.kirim);
        imgbuktibayar = findViewById(R.id.imgbuktibayar);
        fileupload = findViewById(R.id.fileupload);
        btnupload = findViewById(R.id.btnupload);
        kirimbukti = findViewById(R.id.kirimbukti);
        rlimage = findViewById(R.id.rlimage);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Bukti Bayar");
        }

        if (!dbHandler.findAllButiBayar().isEmpty()) {
            llnojadwal.setVisibility(View.GONE);
            llbukti.setVisibility(View.VISIBLE);

            List<DataBukti> dataBukti = dbHandler.findAllButiBayar();
            lbdeskripsi.setText(dataBukti.get(0).getIdinstrukturkelas() + "\nInstruktur : " + dataBukti.get(0).getNamains());

            if (!dataBukti.get(0).getBuktipembayaran().equals("")) {
                fileupload.setText(dataBukti.get(0).getBuktipembayaran());
                Glide.with(BuktiBayar.this)
                        .load(Utilities.getBaseURLImageBukti() + Utilities.getUser(BuktiBayar.this).getGambar())
                        .into(imgbuktibayar);

                rlimage.setVisibility(View.GONE);
                if (!fileupload.getText().toString().equals("File Upload")) {
                    btnupload.setEnabled(false);
                    kirimbukti.setEnabled(false);
                    kirim.setText(R.string.proses);
                }
            } else {
                fileupload.setText(R.string.fileupload);
                rlimage.setVisibility(View.VISIBLE);
            }
        } else {
            llnojadwal.setVisibility(View.VISIBLE);
            llbukti.setVisibility(View.GONE);
        }

        btnupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] options = {"Kamera", "Ambil dari Galeri", "Batal"};
                AlertDialog.Builder builder = new AlertDialog.Builder(BuktiBayar.this);
                builder.setTitle("Upload Bukti Bayar");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Kamera")) {
                            TedRx2Permission.with(BuktiBayar.this)
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
                                                Snackbar.make((BuktiBayar.this).getWindow().getDecorView().getRootView(),
                                                        R.string.text_ask_permission_camera,
                                                        Snackbar.LENGTH_INDEFINITE)
                                                        .setAction(BuktiBayar.this.getString(R.string.action_settings), new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                Intent intent = new Intent();
                                                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                                                Uri uri = Uri.fromParts("package", BuktiBayar.this.getPackageName(), null);
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
                            TedRx2Permission.with(BuktiBayar.this)
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
                                                Snackbar.make((BuktiBayar.this).getWindow().getDecorView().getRootView(),
                                                        R.string.text_ask_permission_storege,
                                                        Snackbar.LENGTH_INDEFINITE)
                                                        .setAction(BuktiBayar.this.getString(R.string.action_settings), new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                Intent intent = new Intent();
                                                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                                                Uri uri = Uri.fromParts("package", BuktiBayar.this.getPackageName(), null);
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


        kirimbukti.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                pDialog = new ProgressDialog(BuktiBayar.this);
                pDialog.setTitle("Loading...");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(false);
                pDialog.show();
                if (Utilities.isNetworkAvailable(BuktiBayar.this)) {
                    if (!fileupload.getText().toString().equals("File Upload"))
                        setbukti(Utilities.getUser(BuktiBayar.this).getNim(),fileupload.getText().toString(),strimage);
                    else {
                        Utilities.showAsToast(BuktiBayar.this, "File bukti blm diupload");
                        pDialog.dismiss();
                    }
                } else {
                    pDialog.dismiss();
                    Utilities.showAsToast(BuktiBayar.this, "Tidak Ada Koneksi Internet");
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void CapturePhoto() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                BuktiBayar.this.getResources().getString(R.string.uri_image_bukti);
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
            (BuktiBayar.this).startActivityForResult(captureImageIntent, CAMERA_PICTURE_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            //Log.e( TAG, "Cannot make photo: " + e );
        }
    }

    private void OpenImage() {
        try {
            Intent openImageIntent = new Intent(Intent.ACTION_PICK);
            openImageIntent.setType("image/*");
            (BuktiBayar.this).startActivityForResult(Intent.createChooser(openImageIntent, "Select Image .."), GALLERY_PICTURE_REQUEST);
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
                String paths = Utilities.getPath(BuktiBayar.this, selectedImage);
                mBitmap = Utilities.getBitmapFromPath(paths);

                @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "user_" + timeStamp + ".png";

                photos = imageFileName;

                path=paths;

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
                //String filePath = SiliCompressor.with(BuktiBayar.this).compress(path, new File(dirpath), true);
                rlimage.setVisibility(View.GONE);
                Glide.with(BuktiBayar.this)
                        .load(new File(path))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imgbuktibayar);
                //photos = new File(filePath).getName();
                fileupload.setText(photos);
                strimage = Utilities.getArrayByteFromBitmap(mBitmap);
            }
        }
    }

    private void setbukti(final String nim, String imagename, String gambar) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utilities.getBaseURLUser())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APIServices apiService = retrofit.create(APIServices.class);
        Call<Value<InsertValue>> call2 = apiService.setuploadbukti("b5e5ac98-f40d-44b5-a5a9-0b3a37382b05", nim,imagename,gambar);
        call2.enqueue(new Callback<Value<InsertValue>>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(@NonNull Call<Value<InsertValue>> call2, @NonNull Response<Value<InsertValue>> response) {
                if (response.body() != null) {
                    assert response.body() != null;
                    int success = Objects.requireNonNull(response.body()).getSuccess();
                    if (success == 1) {
                        if (!nim.equals("")) {
                            Utilities.showAsToast(BuktiBayar.this, "Bukti Selesai diupload");
                            Intent intent = new Intent(BuktiBayar.this, MainActivity.class)
                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                            pDialog.dismiss();
                        }
                    } else {
                        Utilities.showAsToast(BuktiBayar.this, "Gagal mengambil data");
                        pDialog.dismiss();
                    }
                } else {
                    Utilities.showAsToast(BuktiBayar.this, "Gagal mengambil data");
                    pDialog.dismiss();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Value<InsertValue>> call2, @NonNull Throwable t) {
                System.out.println("Retrofit Error:" + t.getMessage());
                Utilities.showAsToast(BuktiBayar.this, "Tidak ada koneksi internet");
                pDialog.dismiss();
            }
        });
    }
}
