package com.cisco.user.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.cisco.user.R;
import com.cisco.user.adapters.JadwalViewAdapter;
import com.cisco.user.models.Jadwal;
import com.cisco.user.services.DBHandler;

import java.util.ArrayList;
import java.util.List;

public class JadwalKelas extends AppCompatActivity {
    DBHandler dbHandler = new DBHandler(JadwalKelas.this);
    LinearLayout llnojadwal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jadwal_kelas);
        RecyclerView rcjadwal = findViewById(R.id.rcjadwal);
        llnojadwal = findViewById(R.id.llnojadwal);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Jadwal Kelas");
        }

        if (!dbHandler.findAllJadwal().isEmpty()) {
            llnojadwal.setVisibility(View.GONE);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(JadwalKelas.this);
            rcjadwal.setLayoutManager(mLayoutManager);

            List<Jadwal> jadwal = dbHandler.findAllJadwal();
            JadwalViewAdapter adapter = new JadwalViewAdapter((ArrayList<Jadwal>) jadwal);
            rcjadwal.setAdapter(adapter);
        } else{
            llnojadwal.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }


}
