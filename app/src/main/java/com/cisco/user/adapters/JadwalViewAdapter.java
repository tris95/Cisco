package com.cisco.user.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cisco.user.R;
import com.cisco.user.activities.DeskripsiPromosi;
import com.cisco.user.models.Jadwal;
import com.cisco.user.models.Kelas;

import java.util.ArrayList;

public class JadwalViewAdapter extends RecyclerView.Adapter<JadwalViewAdapter.DataObjectHolder> {
    private ArrayList<Jadwal> data;

    public JadwalViewAdapter(ArrayList<Jadwal> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public JadwalViewAdapter.DataObjectHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_jadwal_kelas, parent, false);
        return new DataObjectHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final JadwalViewAdapter.DataObjectHolder holder, final int position) {
        holder.idpengambilankelas.setText(data.get(position).getIdinstrukturkelas());
        holder.namainstruktur.setText("Instruktur : "+data.get(position).getNamains());
        holder.ketwaktu.setText(data.get(position).getKetwaktu());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class DataObjectHolder extends RecyclerView.ViewHolder {
        TextView idpengambilankelas,namainstruktur,ketwaktu;
        DataObjectHolder(View itemView) {
            super(itemView);
            idpengambilankelas= itemView.findViewById(R.id.lblidkelas);
            namainstruktur= itemView.findViewById(R.id.lblnamainstruktur);
            ketwaktu= itemView.findViewById(R.id.lblwaktu);
        }
    }
}
