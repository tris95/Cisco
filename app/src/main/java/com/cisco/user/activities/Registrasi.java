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
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.cisco.user.R;
import com.cisco.user.models.InsertValue;
import com.cisco.user.models.Value;
import com.cisco.user.services.APIServices;
import com.cisco.user.utils.Utilities;
import com.gun0912.tedpermission.TedPermissionResult;
import com.tedpark.tedpermission.rx2.TedRx2Permission;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Registrasi extends AppCompatActivity {
    ProgressDialog pDialog;
    EditText id, firstname, lastname, email, password;
    boolean intPassword;
    TextView fileuploadgambar;
    Button daftar, btnupload;
    Spinner cmbprodi;
    private static final int GALLERY_PICTURE_REQUEST = 997;
    private static final int CAMERA_PICTURE_REQUEST = 998;
    String photos, prodipilih;
    Bitmap mBitmap;
    private String mPictureImagePath, strimage;
    String[] programstudi = {"Program Studi", "Teknik Informatika", "Sistem Informasi", "Teknik Komputer"};

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrasi);

        photos = "";
        prodipilih = "";

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Registrasi");
        }

        id = findViewById(R.id.et_nim);
        password = findViewById(R.id.et_password);
        firstname = findViewById(R.id.et_firstnama);
        lastname = findViewById(R.id.et_lastnama);
        email = findViewById(R.id.et_email);
        cmbprodi = findViewById(R.id.cmbprodi);
        daftar = findViewById(R.id.btndaftar);
        btnupload = findViewById(R.id.btnupload);
        fileuploadgambar = findViewById(R.id.fileuploadgambar);

        requestFocus(id);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(Registrasi.this, android.R.layout.simple_spinner_dropdown_item, programstudi);
        cmbprodi.setAdapter(adapter);

        cmbprodi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int m, long id) {
                if (m != 0)
                    prodipilih = programstudi[m];
                else
                    prodipilih = "";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] options = {"Kamera", "Ambil dari Galeri", "Batal"};
                AlertDialog.Builder builder = new AlertDialog.Builder(Registrasi.this);
                builder.setTitle("Upload Bukti Bayar");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Kamera")) {
                            TedRx2Permission.with(Registrasi.this)
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
                                                                Uri uri = Uri.fromParts("package", Registrasi.this.getPackageName(), null);
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
                            TedRx2Permission.with(Registrasi.this)
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
                                                                Uri uri = Uri.fromParts("package", Registrasi.this.getPackageName(), null);
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

        password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                //final int DRAWABLE_LEFT = 0;
                //final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                //final int DRAWABLE_BOTTOM = 3;

                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (motionEvent.getRawX() >= (password.getRight() - password.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        int selectionStart = password.getSelectionStart();
                        int selectionEnd = password.getSelectionEnd();
                        if (intPassword) {
                            password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            password.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_off_black_24dp, 0);
                        } else {
                            password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                            password.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_black_24dp, 0);
                        }
                        password.setSelection(selectionStart, selectionEnd);
                        intPassword = !intPassword;
                    }
                }
                return false;
            }
        });

        daftar.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                pDialog = new ProgressDialog(Registrasi.this);
                pDialog.setTitle("Loading...");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(false);
                pDialog.show();

                if (id.getText().toString().isEmpty()) id.setError("NIM Kosong");
                if (firstname.getText().toString().isEmpty())
                    firstname.setError("Firstname Kosong");
                if (lastname.getText().toString().isEmpty()) lastname.setError("lastname Kosong");
                if (email.getText().toString().isEmpty()) email.setError("email Kosong");
                if (password.getText().toString().isEmpty()) password.setError("password Kosong");

                if (id.getText().toString().isEmpty()) requestFocus(id);
                else if (firstname.getText().toString().isEmpty()) requestFocus(firstname);
                else if (lastname.getText().toString().isEmpty()) requestFocus(lastname);
                else if (email.getText().toString().isEmpty()) requestFocus(email);
                else if (password.getText().toString().isEmpty()) requestFocus(password);
                else if (prodipilih.equals(""))
                    Utilities.showAsToast(Registrasi.this, "Program Studi Belum dipilih");
                else if (fileuploadgambar.getText().equals("Upload Gambar"))
                    Utilities.showAsToast(Registrasi.this, "File gambar belum diunggah");

                if (!id.getText().toString().isEmpty() && !firstname.getText().toString().isEmpty() &&
                        !lastname.getText().toString().isEmpty() && !email.getText().toString().isEmpty() &&
                        !password.getText().toString().isEmpty() && !prodipilih.equals("") &&
                        !fileuploadgambar.getText().equals("Upload Gambar")) {
                    if (Utilities.isNetworkAvailable(Registrasi.this))
                        signup(id.getText().toString(), firstname.getText().toString(), lastname.getText().toString(),
                                email.getText().toString(), prodipilih, password.getText().toString(), photos, strimage);
                    else {
                        Utilities.showAsToast(Registrasi.this, "Tidak Ada Koneksi Internet");
                        pDialog.dismiss();
                    }
                }
                else
                    pDialog.dismiss();
            }
        });
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public void signup(final String nim, String firstname, String lastname, String email, String prodi, String password, String imageName, String gambar) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utilities.getBaseURLUser())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APIServices apiService = retrofit.create(APIServices.class);
        Call<Value<InsertValue>> call = apiService.signup("b5e5ac98-f40d-44b5-a5a9-0b3a37382b05", nim, firstname, lastname, email, prodi, password, imageName, gambar);
        call.enqueue(new Callback<Value<InsertValue>>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(@NonNull Call<Value<InsertValue>> call, @NonNull Response<Value<InsertValue>> response) {
                pDialog.dismiss();
                if (response.body() != null) {
                    int success = Objects.requireNonNull(response.body()).getSuccess();
                    if (success == 1) {
                        finish();
                        Utilities.showAsToast(Registrasi.this, "Pendaftaran Berhasil");
                        Intent intent = new Intent(Registrasi.this, Login.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("nim", nim);
                        startActivity(intent);
                        finish();
                    } else
                        Utilities.showAsToast(Registrasi.this, "Gagal");
                } else {
                    Utilities.showAsToast(Registrasi.this, "Tidak ada data");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Value<InsertValue>> call, @NonNull Throwable t) {
                System.out.println("Retrofit Error:" + t.getMessage());
                Utilities.showAsToast(Registrasi.this, "Tidak Terhubung Dengan Server. Silahkan Coba Lagi!");
                pDialog.dismiss();
            }
        });
    }

    public void CapturePhoto() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                getResources().getString(R.string.uri_image_user);
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "user_" + timeStamp + ".jpg";
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
            //Log.e( TAG, "Cannot make photo: " + e );
        }
    }

    public void OpenImage() {
        try {
            Intent openImageIntent = new Intent(Intent.ACTION_PICK);
            openImageIntent.setType("image/*");
            startActivityForResult(Intent.createChooser(openImageIntent, "Select Image .."), GALLERY_PICTURE_REQUEST);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            //Log.e( TAG, "No gallery: " + e );
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String dirpath = Environment.getExternalStorageDirectory().getAbsolutePath() +
                    getResources().getString(R.string.uri_image_user);
            String path = null;
            if (requestCode == GALLERY_PICTURE_REQUEST) {
                path = dirpath;
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                Uri selectedImage = data.getData();
                String paths = Utilities.getPath(Registrasi.this, selectedImage);
                mBitmap = Utilities.getBitmapFromPath(paths);
                System.out.println("MBitmap:" + mBitmap);

                @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "user_" + timeStamp + ".png";

                photos = imageFileName;

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
                //String filePath = SiliCompressor.with(Registrasi.this).compress(path, new File(dirpath), true);

                //photos = new File(filePath).getName();
                fileuploadgambar.setText(photos);
                strimage = Utilities.getArrayByteFromBitmap(mBitmap);
            }

        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
