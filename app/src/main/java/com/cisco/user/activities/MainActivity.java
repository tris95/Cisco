package com.cisco.user.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cisco.user.R;
import com.cisco.user.adapters.PromosiViewAdapter;
import com.cisco.user.models.DataBukti;
import com.cisco.user.models.Jadwal;
import com.cisco.user.models.Kelas;
import com.cisco.user.models.Promosi;
import com.cisco.user.models.Value;
import com.cisco.user.services.APIServices;
import com.cisco.user.services.DBHandler;
import com.cisco.user.utils.Utilities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    APIServices apiService;
    Retrofit retrofit;
    PromosiViewAdapter adapter;
    RecyclerView rcpromosi;
    ProgressBar progressBar;
    TextView namauser, emailuser;
    ImageView imguser;
    LinearLayout llnointernet;
    DBHandler dbHandler = new DBHandler(MainActivity.this);

    GridLayoutManager mLayoutManager;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        rcpromosi = findViewById(R.id.rcpromosi);
        progressBar = findViewById(R.id.progressBar);
        llnointernet = findViewById(R.id.llnointernet);

        mLayoutManager = new GridLayoutManager(MainActivity.this, 1);
        rcpromosi.setLayoutManager(mLayoutManager);


        if (!Utilities.isNetworkAvailable(MainActivity.this)) {
            Utilities.showAsToast(MainActivity.this, "Tidak Ada Koneksi Internet");
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setCancelable(true)
                    .setTitle("Peringatan")
                    .setMessage("Lanjut Tanpa Jaringan?")
                    .setPositiveButton(R.string.text_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .setNegativeButton(R.string.text_cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    })
                    .show();
        }
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View v = LayoutInflater.from(this).inflate(R.layout.nav_header_main, navigationView);
        namauser = v.findViewById(R.id.namauser);
        emailuser = v.findViewById(R.id.emailuser);
        imguser = v.findViewById(R.id.imageView);

        Log.e("url",Utilities.getBaseURLImageUser() + Utilities.getUser(MainActivity.this).getGambar());
        Glide.with(MainActivity.this)
                .load(Utilities.getBaseURLImageUser() + Utilities.getUser(MainActivity.this).getGambar())
                .into(imguser);

        namauser.setText(Utilities.getUser(MainActivity.this).getFirstname() + " " + Utilities.getUser(MainActivity.this).getLastname());
        emailuser.setText(Utilities.getUser(MainActivity.this).getEmail());


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.daftarkelas) {
            startActivity(new Intent(MainActivity.this, Taketheclassroom.class));
        } else if (id == R.id.jadwal) {
            startActivity(new Intent(MainActivity.this, JadwalKelas.class));
        } else if (id == R.id.buktibayar) {
            startActivity(new Intent(MainActivity.this, BuktiBayar.class));
        } else if (id == R.id.logout) {
            confirmSignOut();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void promosi() {
        progressBar.setVisibility(View.VISIBLE);

        retrofit = new Retrofit.Builder()
                .baseUrl(Utilities.getBaseURLUser())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(APIServices.class);
        Call<Value<Promosi>> call = apiService.getpromosi("b5e5ac98-f40d-44b5-a5a9-0b3a37382b05");
        call.enqueue(new Callback<Value<Promosi>>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(@NonNull Call<Value<Promosi>> call, @NonNull Response<Value<Promosi>> response) {
                if (response.body() != null) {
                    int success = Objects.requireNonNull(response.body()).getSuccess();
                    if (success == 1) {
                        List<Promosi> promosi = Objects.requireNonNull(response.body()).getData();
                        adapter = new PromosiViewAdapter((ArrayList<Promosi>) promosi, MainActivity.this);
                        rcpromosi.setAdapter(adapter);
                        progressBar.setVisibility(View.GONE);
                        nmkelas();
                    } else {
                        Utilities.showAsToast(MainActivity.this, "data kosong");
                        progressBar.setVisibility(View.GONE);
                    }
                } else {
                    Utilities.showAsToast(MainActivity.this, "Tidak ada data");
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Value<Promosi>> call, @NonNull Throwable t) {
                System.out.println("Retrofit Error:" + t.getMessage());
                Utilities.showAsToast(MainActivity.this, "Tidak Terhubung Dengan Server. Silahkan Coba Lagi!");
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    public void nmkelas() {
        retrofit = new Retrofit.Builder()
                .baseUrl(Utilities.getBaseURLUser())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        @SuppressLint("SimpleDateFormat") String tanggal = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        apiService = retrofit.create(APIServices.class);
        Call<Value<Kelas>> call = apiService.getnamakelas("b5e5ac98-f40d-44b5-a5a9-0b3a37382b05", tanggal);
        call.enqueue(new Callback<Value<Kelas>>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(@NonNull Call<Value<Kelas>> call, @NonNull Response<Value<Kelas>> response) {
                if (response.body() != null) {
                    int success = Objects.requireNonNull(response.body()).getSuccess();
                    if (success == 1) {
                        List<Kelas> kelas = Objects.requireNonNull(response.body()).getData();
                        if (!dbHandler.findAllNamaKelas().isEmpty())
                            dbHandler.deleteNamaKelas();
                        dbHandler.addNamaKelas(kelas);

                        jadwalkls();
                    } else
                        Utilities.showAsToast(MainActivity.this, "data kosong");
                } else {
                    Utilities.showAsToast(MainActivity.this, "Tidak ada data");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Value<Kelas>> call, @NonNull Throwable t) {
                System.out.println("Retrofit Error:" + t.getMessage());
                Utilities.showAsToast(MainActivity.this, "Tidak Terhubung Dengan Server. Silahkan Coba Lagi!");
            }
        });
    }

    public void confirmSignOut() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(true)
                .setTitle("Konfirmasi")
                .setMessage("Keluar dari Aplikasi?")
                .setPositiveButton(R.string.text_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //if (Utilities.isNetworkAvailable(MainActivity.this)) {
                        Utilities.clearUser(MainActivity.this);
                        startActivity(new Intent(MainActivity.this, Login.class));
                        finish();
                        //}
                    }
                })
                .setNegativeButton(R.string.text_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }

    public void jadwalkls() {
        retrofit = new Retrofit.Builder()
                .baseUrl(Utilities.getBaseURLUser())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(APIServices.class);
        Call<Value<Jadwal>> call = apiService.getjadwalkelas("b5e5ac98-f40d-44b5-a5a9-0b3a37382b05", Utilities.getUser(MainActivity.this).getNim());
        call.enqueue(new Callback<Value<Jadwal>>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(@NonNull Call<Value<Jadwal>> call, @NonNull Response<Value<Jadwal>> response) {
                if (response.body() != null) {
                    int success = Objects.requireNonNull(response.body()).getSuccess();
                    if (success == 1) {
                        List<Jadwal> jadwal = Objects.requireNonNull(response.body()).getData();
                        if (!dbHandler.findAllJadwal().isEmpty())
                            dbHandler.deleteJadwal();
                        dbHandler.addJadwal(jadwal);
                        bukti();
                    } else
                        Utilities.showAsToast(MainActivity.this, "data kosong");
                } else {
                    Utilities.showAsToast(MainActivity.this, "Tidak ada data");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Value<Jadwal>> call, @NonNull Throwable t) {
                System.out.println("Retrofit Error:" + t.getMessage());
                Utilities.showAsToast(MainActivity.this, "Tidak Terhubung Dengan Server. Silahkan Coba Lagi!");
            }
        });
    }

    public void bukti() {
        retrofit = new Retrofit.Builder()
                .baseUrl(Utilities.getBaseURLUser())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(APIServices.class);
        Call<Value<DataBukti>> call = apiService.getbukti("b5e5ac98-f40d-44b5-a5a9-0b3a37382b05", Utilities.getUser(MainActivity.this).getNim());
        call.enqueue(new Callback<Value<DataBukti>>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(@NonNull Call<Value<DataBukti>> call, @NonNull Response<Value<DataBukti>> response) {
                if (response.body() != null) {
                    int success = Objects.requireNonNull(response.body()).getSuccess();
                    if (success == 1) {
                        List<DataBukti> bukti = Objects.requireNonNull(response.body()).getData();
                        if (!dbHandler.findAllButiBayar().isEmpty())
                            dbHandler.deleteBukti();
                        dbHandler.addBuktiBayar(bukti);
                    } else
                        Utilities.showAsToast(MainActivity.this, "data kosong");
                } else {
                    Utilities.showAsToast(MainActivity.this, "Tidak ada data");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Value<DataBukti>> call, @NonNull Throwable t) {
                System.out.println("Retrofit Error:" + t.getMessage());
                Utilities.showAsToast(MainActivity.this, "Tidak Terhubung Dengan Server. Silahkan Coba Lagi!");
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onResume() {
        if (Utilities.isNetworkAvailable(MainActivity.this)) {
            llnointernet.setVisibility(View.GONE);

            promosi();

        } else {
            llnointernet.setVisibility(View.VISIBLE);
        }
        super.onResume();
    }
}
