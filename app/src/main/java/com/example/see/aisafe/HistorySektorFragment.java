package com.example.see.aisafe;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Iterator;

public class HistorySektorFragment extends Fragment {
    View myView;

    private TextView tvUsername, tvTanggal, tvTask;
    private Button btnDetail;

    private String namaSektor, keyBencana;
    private TableLayout tableLayout;
    private DatabaseReference root;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.history_sektor, container, false);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("History " + namaSektor);

        tableLayout = myView.findViewById(R.id.tableSektorHistory);

        keyBencana = getArguments().getString("keyBencana");
        namaSektor = getArguments().getString("namaSektor");

        root = FirebaseDatabase.getInstance().getReference().child("Bencana").child(keyBencana).child("History");

        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterator iterator = dataSnapshot.getChildren().iterator();
                Iterator iteratorKey = dataSnapshot.getChildren().iterator();
                final ArrayList<String> keyHistory = new ArrayList<>();
                ArrayList<String> splitSektor;
                String sektor;
                int count = 0;
                while (iteratorKey.hasNext()) {
                    keyHistory.add(((DataSnapshot)iteratorKey.next()).getKey());
                }
                while (iterator.hasNext()) {
                    Iterator iterator1 = ((DataSnapshot) iterator.next()).getChildren().iterator();
                    while (iterator1.hasNext()) {
                        splitSektor = new ArrayList<>();
                        TableRow row = new TableRow(getContext());
                        TableRow.LayoutParams params = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        params.gravity = Gravity.CENTER;
                        row.setLayoutParams(params);
                        iterator1.next();
                        iterator1.next();
                        iterator1.next();
                        sektor = ((DataSnapshot) iterator1.next()).getValue().toString();
                        if (!sektor.equals(namaSektor)) {
                            count++;
                            continue;
                        }
                        splitSektor.add(sektor);
                        splitSektor.add(((DataSnapshot) iterator1.next()).getValue().toString());
                        splitSektor.add(((DataSnapshot) iterator1.next()).getValue().toString());
                        splitSektor.add(((DataSnapshot) iterator1.next()).getValue().toString());
                        iterator1.next();
                        tvUsername = new TextView(getActivity());
                        tvTanggal = new TextView(getActivity());
                        tvTask = new TextView(getActivity());
                        btnDetail = new Button(getActivity());

                        tvUsername.setGravity(Gravity.CENTER);
                        tvTanggal.setGravity(Gravity.CENTER);
                        tvTask.setGravity(Gravity.CENTER);
                        tvUsername.setTextSize(12.0f);
                        tvTanggal.setTextSize(12.0f);
                        tvTask.setTextSize(12.0f);
                        btnDetail.setGravity(Gravity.CENTER);
                        btnDetail.setText("Detail");
                        btnDetail.setTextSize(10.0f);
                        btnDetail.setBackgroundColor(Color.argb(0, 255, 255, 255));
                        btnDetail.setTextColor(Color.parseColor("#2b2bf2"));
                        tvTanggal.setText(splitSektor.get(1));
                        tvTask.setText(splitSektor.get(2));
                        tvUsername.setText(splitSektor.get(3));
                        row.addView(tvUsername);
                        row.addView(tvTanggal);
                        row.addView(tvTask);
                        row.addView(btnDetail);
                        final int finalCount = count;
                        btnDetail.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Bundle bundle = new Bundle();
                                bundle.putString("keyHistory", keyHistory.get(finalCount));
                                bundle.putString("keyBencana", keyBencana);
                                Fragment fragment = new DetailHistory();
                                fragment.setArguments(bundle);
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.content_frame, fragment);
                                fragmentTransaction.addToBackStack("tag");
                                fragmentTransaction.commit();
                            }
                        });
                        tableLayout.addView(row);
                        count++;
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
