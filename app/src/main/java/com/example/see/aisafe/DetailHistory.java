package com.example.see.aisafe;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.SQLOutput;
import java.util.Iterator;

public class DetailHistory extends Fragment {
    View myView;
    TableLayout tableLayout;

    private TextView tvUsernameDetail, tvTaskDetail, tvWaktu, tvTanggalDetail,
            tvSektorDetail, tvKerusakanDetail, tvKorban, tvAddDetail;
    private TextView tvUsernameDetail2, tvTaskDetail2, tvWaktu2, tvTanggalDetail2,
            tvSektorDetail2, tvKerusakanDetail2, tvKorban2, tvAddDetail2;
    private String keyDetailHistory, keyBencana;
    private DatabaseReference rootHistory;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.detail_history, container, false);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("History Detail Edit");
        tableLayout = myView.findViewById(R.id.tableLayoutDetail);

        final TableRow row1 = new TableRow(getContext());
        final TableRow row2 = new TableRow(getContext());
        final TableRow row3 = new TableRow(getContext());
        final TableRow row4 = new TableRow(getContext());
        final TableRow row5 = new TableRow(getContext());
        final TableRow row6 = new TableRow(getContext());
        final TableRow row7 = new TableRow(getContext());
        final TableRow row8 = new TableRow(getContext());
        TableRow.LayoutParams params = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        row1.setLayoutParams(params);
        row2.setLayoutParams(params);
        row3.setLayoutParams(params);
        row4.setLayoutParams(params);
        row5.setLayoutParams(params);
        row6.setLayoutParams(params);
        row7.setLayoutParams(params);
        row8.setLayoutParams(params);
        tvUsernameDetail = new TextView(getActivity());
        tvTaskDetail = new TextView(getActivity());
        tvWaktu = new TextView(getActivity());
        tvTanggalDetail = new TextView(getActivity());
        tvSektorDetail = new TextView(getActivity());
        tvKerusakanDetail = new TextView(getActivity());
        tvKorban = new TextView(getActivity());
        tvAddDetail = new TextView(getActivity());
        tvUsernameDetail2 = new TextView(getActivity());
        tvTaskDetail2 = new TextView(getActivity());
        tvWaktu2 = new TextView(getActivity());
        tvTanggalDetail2 = new TextView(getActivity());
        tvSektorDetail2 = new TextView(getActivity());
        tvKerusakanDetail2 = new TextView(getActivity());
        tvKorban2 = new TextView(getActivity());
        tvAddDetail2 = new TextView(getActivity());
        tvUsernameDetail.setGravity(Gravity.START);
        tvTaskDetail.setGravity(Gravity.START);
        tvWaktu.setGravity(Gravity.START);
        tvTanggalDetail.setGravity(Gravity.START);
        tvUsernameDetail.setGravity(Gravity.START);
        tvSektorDetail.setGravity(Gravity.START);
        tvKerusakanDetail.setGravity(Gravity.START);
        tvKorban.setGravity(Gravity.START);
        tvAddDetail.setGravity(Gravity.START);
        tvUsernameDetail2.setGravity(Gravity.START);
        tvTaskDetail2.setGravity(Gravity.START);
        tvWaktu2.setGravity(Gravity.START);
        tvTanggalDetail2.setGravity(Gravity.START);
        tvUsernameDetail2.setGravity(Gravity.START);
        tvSektorDetail2.setGravity(Gravity.START);
        tvKerusakanDetail2.setGravity(Gravity.START);
        tvKorban2.setGravity(Gravity.START);
        tvAddDetail2.setGravity(Gravity.START);

        keyDetailHistory = getArguments().getString("keyHistory");
        keyBencana = getArguments().getString("keyBencana");
        rootHistory = FirebaseDatabase.getInstance().getReference().child("Bencana").child(keyBencana).child("History").child(keyDetailHistory);

        rootHistory.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterator iterator = dataSnapshot.getChildren().iterator();
                tvAddDetail2.setText("Detail");
                tvKerusakanDetail2.setText("Kerusakan");
                tvKorban2.setText("Korban");
                tvSektorDetail2.setText("Sektor");
                tvTanggalDetail2.setText("Tanggal");
                tvTaskDetail2.setText("Task");
                tvUsernameDetail2.setText("Username");
                tvWaktu2.setText("Waktu");
                tvAddDetail.setText(((DataSnapshot)iterator.next()).getValue().toString());
                tvKerusakanDetail.setText(((DataSnapshot)iterator.next()).getValue().toString());
                tvKorban.setText(((DataSnapshot)iterator.next()).getValue().toString());
                tvSektorDetail.setText(((DataSnapshot)iterator.next()).getValue().toString());
                tvTanggalDetail.setText(((DataSnapshot)iterator.next()).getValue().toString());
                tvTaskDetail.setText(((DataSnapshot)iterator.next()).getValue().toString());
                tvUsernameDetail.setText(((DataSnapshot)iterator.next()).getValue().toString());
                tvWaktu.setText(((DataSnapshot)iterator.next()).getValue().toString());
                row1.addView(tvUsernameDetail2);
                row1.addView(tvUsernameDetail);
                row2.addView(tvTaskDetail2);
                row2.addView(tvTaskDetail);
                row3.addView(tvWaktu2);
                row3.addView(tvWaktu);
                row4.addView(tvTanggalDetail2);
                row4.addView(tvTanggalDetail);
                row5.addView(tvSektorDetail2);
                row5.addView(tvSektorDetail);
                row6.addView(tvKerusakanDetail2);
                row6.addView(tvKerusakanDetail);
                row7.addView(tvKorban2);
                row7.addView(tvKorban);
                row8.addView(tvAddDetail2);
                row8.addView(tvAddDetail);
                tableLayout.addView(row1);
                tableLayout.addView(row2);
                tableLayout.addView(row3);
                tableLayout.addView(row4);
                tableLayout.addView(row5);
                tableLayout.addView(row6);
                tableLayout.addView(row7);
                tableLayout.addView(row8);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return myView;
    }
}
