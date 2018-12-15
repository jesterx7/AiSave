package com.example.see.aisafe;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by user on 07/12/2018.
 */

public class ListSektorAdapter extends RecyclerView.Adapter<ListSektorAdapter.SektorViewHolder> {
    private Context context;
    private ArrayList<Sektor> listSektor;

    public ListSektorAdapter(Context context) { this.context = context;}

    public ArrayList<Sektor> getListSektor() {return listSektor;}

    public void setListSektor(ArrayList<Sektor> listSektor) {this.listSektor = listSektor;}

    @NonNull
    @Override
    public ListSektorAdapter.SektorViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemRow = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_sektor, viewGroup, false);
        return new SektorViewHolder(itemRow);
    }

    @Override
    public void onBindViewHolder(@NonNull SektorViewHolder sektorViewHolder, int i) {
        sektorViewHolder.tvSektor.setText(getListSektor().get(i).getNamaSektor());
        sektorViewHolder.tvKorban.setText("Korban Jiwa " + getListSektor().get(i).getJumlahKorban() + " Orang");
        sektorViewHolder.tvKerusakan.setText("Jumlah Bangunan Rusak Ada " + getListSektor().get(i).getJumlahKerusakan() + " Bangunan");
    }

    @Override
    public int getItemCount() {
        return getListSektor().size();
    }

    public class SektorViewHolder extends RecyclerView.ViewHolder {
        private TextView tvSektor, tvKorban, tvKerusakan;
        public SektorViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSektor = itemView.findViewById(R.id.tvNamaSektor);
            tvKorban = itemView.findViewById(R.id.tvKorban);
            tvKerusakan = itemView.findViewById(R.id.tvKerusakan);
        }
    }
}
