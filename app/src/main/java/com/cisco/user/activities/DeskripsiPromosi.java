package com.cisco.user.activities;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cisco.user.R;
import com.cisco.user.models.IsiPromosi;
import com.cisco.user.models.Promosi;
import com.cisco.user.models.Value;
import com.cisco.user.services.APIServices;
import com.cisco.user.utils.Utilities;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DeskripsiPromosi extends AppCompatActivity {
    TextView title, deskripsi;
    ImageView imgpromo;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deskripsi_promosi);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Deskripsi Promo");
        }

        title = findViewById(R.id.title);
        deskripsi = findViewById(R.id.deskripsi);
        imgpromo = findViewById(R.id.imgpromosi);

        if (Utilities.isNetworkAvailable(DeskripsiPromosi.this)) {
            isipromosi();
        } else
            Utilities.showAsToast(DeskripsiPromosi.this, "Tidak Ada Koneksi Internet");

    }

    public void isipromosi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utilities.getBaseURLUser())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APIServices apiService = retrofit.create(APIServices.class);
        Call<Value<IsiPromosi>> call = apiService.getisipromosi("b5e5ac98-f40d-44b5-a5a9-0b3a37382b05", getIntent().getStringExtra("id"));
        call.enqueue(new Callback<Value<IsiPromosi>>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(@NonNull Call<Value<IsiPromosi>> call, @NonNull Response<Value<IsiPromosi>> response) {
                if (response.body() != null) {
                    int success = Objects.requireNonNull(response.body()).getSuccess();
                    if (success == 1) {
                        List<IsiPromosi> isipromosi = Objects.requireNonNull(response.body()).getData();
                        title.setText(getIntent().getStringExtra("title"));
                        deskripsi.setText(isipromosi.get(0).getIsi());

                        if (!getIntent().getStringExtra("gambar").equals("")) {
                            Glide.with(DeskripsiPromosi.this)
                                    .load(Utilities.getBaseURLImagePromo() + getIntent().getStringExtra("gambar"))
                                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                    .placeholder(R.color.colorDivider)
                                    .into(imgpromo);
                        } else imgpromo.setVisibility(View.GONE);

                    } else {
                        Utilities.showAsToast(DeskripsiPromosi.this, "data kosong");
                    }
                } else {
                    Utilities.showAsToast(DeskripsiPromosi.this, "Tidak ada data");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Value<IsiPromosi>> call, @NonNull Throwable t) {
                System.out.println("Retrofit Error:" + t.getMessage());
                Utilities.showAsToast(DeskripsiPromosi.this, "Tidak Terhubung Dengan Server. Silahkan Coba Lagi!");
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}

