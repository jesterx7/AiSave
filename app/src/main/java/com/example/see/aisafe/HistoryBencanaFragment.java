package com.example.see.aisafe;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

public class HistoryBencanaFragment extends Fragment {
    View myView;
    DatabaseReference root;
    String keyBencana;

    private TextView tvUsername, tvTanggal, tvWaktu;
    private Button btnDetail;
    private TableLayout tableHistory;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.history_bencana, container, false);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("History Bencana");

        tableHistory = myView.findViewById(R.id.tableHistory);

        keyBencana = getArguments().getString("keyBencana");
        root = FirebaseDatabase.getInstance().getReference().child("Bencana").child(keyBencana).child("History");

        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterator iterator = dataSnapshot.getChildren().iterator();
                while (iterator.hasNext()) {
                    Iterator iterator1 = ((DataSnapshot)iterator.next()).getChildren().iterator();
                    while (iterator1.hasNext()) {
                        TableRow row = new TableRow(getContext());
                        TableRow.LayoutParams params = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                        params.gravity = Gravity.CENTER;
                        row.setLayoutParams(params);
                        tvUsername = new TextView(getActivity());
                        tvTanggal = new TextView(getActivity());
                        tvWaktu = new TextView(getActivity());
                        btnDetail = new Button(getActivity());
                        tvUsername.setGravity(Gravity.CENTER);
                        tvTanggal.setGravity(Gravity.CENTER);
                        tvWaktu.setGravity(Gravity.CENTER);
                        btnDetail.setGravity(Gravity.CENTER);
                        btnDetail.setText("Detail");
                        btnDetail.setBackgroundColor(Color.parseColor("#80000000"));
                        btnDetail.setTextColor(Color.parseColor("#2b2bf2"));
                        tvTanggal.setText(((DataSnapshot)iterator1.next()).getValue().toString());
                        tvUsername.setText(((DataSnapshot)iterator1.next()).getValue().toString());
                        tvWaktu.setText(((DataSnapshot)iterator1.next()).getValue().toString());
                        row.addView(tvUsername);
                        row.addView(tvTanggal);
                        row.addView(tvWaktu);
                        row.addView(btnDetail);
                        tableHistory.addView(row);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return myView;
    }
}
