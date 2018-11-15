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
import com.cisco.user.models.Promosi;

import java.util.ArrayList;
import java.util.List;

public class PromosiViewAdapter extends RecyclerView.Adapter<PromosiViewAdapter.ViewHolder> {
    private ArrayList<Promosi> data;
    private Context context;


    public PromosiViewAdapter(ArrayList<Promosi> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_promosi,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.lbltile.setText(data.get(position).getJudul());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,DeskripsiPromosi.class);
                intent.putExtra("id",data.get(position).getId());
                intent.putExtra("title",data.get(position).getJudul());
                intent.putExtra("gambar",data.get(position).getGambar());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView lbltile;

        ViewHolder(View itemView) {
            super(itemView);
            lbltile=itemView.findViewById(R.id.lbltitle);
        }
    }
}
