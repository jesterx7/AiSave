package com.example.see.aisafe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {
    ArrayList<String> kebutuhan;
    ArrayList<String> qty;
    Context context;

    CustomAdapter(Context context, ArrayList<String> kebutuhan, ArrayList<String> qty) {
        this.context = context;
        this.kebutuhan = kebutuhan;
        this.qty = qty;
    }

    @Override
    public int getCount() {
        return kebutuhan.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater li;
        li = LayoutInflater.from(context);
        convertView = li.inflate(R.layout.custom_list_kebutuhan, null);

        TextView tvNamaKebutuhan = convertView.findViewById(R.id.tvNamaKebutuhan);
        TextView tvQtyKebutuhan = convertView.findViewById(R.id.tvQtyKebutuhan);

        tvNamaKebutuhan.setText(kebutuhan.get(position));
        tvQtyKebutuhan.setText(qty.get(position).toString());
        return convertView;
    }
}
