package com.example.see.aisafe;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CustomAdapter extends BaseAdapter {
    ArrayList<String> kebutuhan;
    ArrayList<String> qty;
    int delBtn;
    Context context;
    String namaUser, namaSektor;
    DatabaseReference root;
    DatabaseReference rootListKebutuhan;
    DatabaseReference rootHistory;
    DatabaseReference rootBencana;

    CustomAdapter(Context context, ArrayList<String> kebutuhan, ArrayList<String> qty, int delBtn, String namaUser, String namaSektor) {
        this.context = context;
        this.kebutuhan = kebutuhan;
        this.qty = qty;
        this.delBtn = delBtn;
        this.namaUser = namaUser;
        this.namaSektor = namaSektor;
    }

    public DatabaseReference getRootHistory() {
        return rootHistory;
    }

    public void setRootHistory(DatabaseReference rootHistory) {
        this.rootHistory = rootHistory;
    }

    public DatabaseReference getRootBencana() {
        return rootBencana;
    }

    public void setRootBencana(DatabaseReference rootBencana) {
        this.rootBencana = rootBencana;
    }

    public DatabaseReference getRoot() {
        return root;
    }

    public void setRoot(DatabaseReference root) {
        this.root = root;
    }

    public DatabaseReference getRootListKebutuhan() {
        return rootListKebutuhan;
    }

    public void setRootListKebutuhan(DatabaseReference rootListKebutuhan) {
        this.rootListKebutuhan = rootListKebutuhan;
    }

    @Override
    public int getCount() {
        return qty.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater li;
        li = LayoutInflater.from(context);
        convertView = li.inflate(R.layout.custom_list_kebutuhan, null);

        TextView tvNamaKebutuhan = convertView.findViewById(R.id.tvNamaKebutuhan);
        TextView tvQtyKebutuhan = convertView.findViewById(R.id.tvQtyKebutuhan);
        Button btnDeleteKebutuhan = convertView.findViewById(R.id.btnDelete);

        tvNamaKebutuhan.setText(kebutuhan.get(position));
        tvQtyKebutuhan.setText(qty.get(position).toString());

        if (delBtn == 0){
            btnDeleteKebutuhan.setVisibility(View.GONE);
        }

        btnDeleteKebutuhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rootListKebutuhan != null){
                    String detailUpdate = kebutuhan.get(position) + " " +  qty.get(position);
                    rootListKebutuhan.child(kebutuhan.get(position)).removeValue();
                    kebutuhan.remove(position);
                    qty.remove(position);
                    notifyDataSetChanged();

                    String newKey = rootBencana.push().getKey();
                    rootHistory = rootBencana.child(newKey);

                    String formattedDate, time;

                    Date c = Calendar.getInstance().getTime();
                    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                    formattedDate = df.format(c);
                    SimpleDateFormat dfTime = new SimpleDateFormat("hh:mm aa");
                    time = dfTime.format(c);


                    Map<String, Object> mapHistory = new HashMap<>();

                    mapHistory.put("Username", namaUser);
                    mapHistory.put("Tanggal", formattedDate);
                    mapHistory.put("Waktu", time);
                    mapHistory.put("Task", "Delete");
                    mapHistory.put("Detail", detailUpdate);
                    mapHistory.put("Sektor", namaSektor);
                    mapHistory.put("Kerusakan", " - ");
                    mapHistory.put("Korban", " - ");

                    rootHistory.updateChildren(mapHistory);
                }
                rootListKebutuhan.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                        Iterator i = dataSnapshot.getChildren().iterator();
                        qty.clear();
                        while (i.hasNext()) {
                            qty.add(((DataSnapshot)i.next()).getValue().toString());
                        }
                        root.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Iterator iterator = dataSnapshot.getChildren().iterator();
                                kebutuhan.clear();
                                while (iterator.hasNext()) {
                                    Iterator iterator1 = ((DataSnapshot)iterator.next()).getChildren().iterator();
                                    while (iterator1.hasNext()) {
                                        kebutuhan.add(((DataSnapshot)iterator1.next()).getKey().toString());
                                    }

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });


        return convertView;
    }
}
