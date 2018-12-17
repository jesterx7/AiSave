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

import java.util.ArrayList;
import java.util.Iterator;

public class HistoryUserFragment extends Fragment {
    View myView;

    private TableLayout tableLayout;
    private TextView tvBencana, tvSektor, tvTanggal;
    private Button btnDetail;

    private DatabaseReference root;
    private String namaUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.history_user, container, false);
        tableLayout = myView.findViewById(R.id.tableLayoutHistoryUser);

        namaUser = getArguments().getString("nama");
        root = FirebaseDatabase.getInstance().getReference().child("Bencana");

        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterator iterator = dataSnapshot.getChildren().iterator();
                Iterator iteratorNamaBencana = dataSnapshot.getChildren().iterator();
                Iterator iteratorKeyHistory = dataSnapshot.getChildren().iterator();
                final ArrayList<String> keyBencana = new ArrayList<>();
                final ArrayList<String> keyHistory = new ArrayList<>();
                while (iteratorNamaBencana.hasNext()) {
                    keyBencana.add(((DataSnapshot)iteratorNamaBencana.next()).getKey());
                }
                while (iteratorKeyHistory.hasNext()) {
                    Iterator iteratorBencanaForKey = ((DataSnapshot)iteratorKeyHistory.next()).getChildren().iterator();
                    while (iteratorBencanaForKey.hasNext()) {
                        iteratorBencanaForKey.next();
                        Iterator iteratorGetKeyHistory = ((DataSnapshot)iteratorBencanaForKey.next()).getChildren().iterator();
                        while (iteratorGetKeyHistory.hasNext()) {
                            keyHistory.add(((DataSnapshot)iteratorGetKeyHistory.next()).getKey());
                        }
                        iteratorBencanaForKey.next();
                        iteratorBencanaForKey.next();
                    }
                }
                ArrayList<String> splitHistory;
                String username;
                int count1 = 0;
                int count2 = 0;
                while (iterator.hasNext()) {
                    Iterator iteratorBencana = ((DataSnapshot) iterator.next()).getChildren().iterator();
                    while (iteratorBencana.hasNext()) {
                        iteratorBencana.next();
                        Iterator iteratorHistory = ((DataSnapshot) iteratorBencana.next()).getChildren().iterator();
                        while (iteratorHistory.hasNext()) {
                            Iterator iteratorListHistory = ((DataSnapshot) iteratorHistory.next()).getChildren().iterator();
                            while (iteratorListHistory.hasNext()) {
                                splitHistory = new ArrayList<>();
                                splitHistory.add(((DataSnapshot) iteratorListHistory.next()).getValue().toString());
                                splitHistory.add(((DataSnapshot) iteratorListHistory.next()).getValue().toString());
                                splitHistory.add(((DataSnapshot) iteratorListHistory.next()).getValue().toString());
                                splitHistory.add(((DataSnapshot) iteratorListHistory.next()).getValue().toString());
                                splitHistory.add(((DataSnapshot) iteratorListHistory.next()).getValue().toString());
                                splitHistory.add(((DataSnapshot) iteratorListHistory.next()).getValue().toString());
                                System.out.println("HASIL SPLITTTTTTTT" + splitHistory);
                                username = ((DataSnapshot) iteratorListHistory.next()).getValue().toString();
                                if (!username.equals(namaUser)) {
                                    count1++;
                                    iteratorListHistory.next();
                                    continue;
                                }
                                splitHistory.add(username);
                                splitHistory.add(((DataSnapshot) iteratorListHistory.next()).getValue().toString());
                                System.out.println("SPLITTTTTTTTING"+splitHistory);
                                TableRow row = new TableRow(getContext());
                                TableRow.LayoutParams params = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                params.gravity = Gravity.CENTER;
                                row.setLayoutParams(params);

                                tvBencana = new TextView(getActivity());
                                tvSektor = new TextView(getActivity());
                                tvTanggal = new TextView(getActivity());
                                btnDetail = new Button(getActivity());

                                tvBencana.setGravity(Gravity.CENTER);
                                tvSektor.setGravity(Gravity.CENTER);
                                tvTanggal.setGravity(Gravity.CENTER);
                                btnDetail.setGravity(Gravity.CENTER);
                                btnDetail.setText("Detail");
                                btnDetail.setTextSize(10.0f);
                                btnDetail.setBackgroundColor(Color.argb(0, 255, 255, 255));
                                btnDetail.setTextColor(Color.parseColor("#2b2bf2"));

                                tvBencana.setText(keyBencana.get(count2));
                                tvSektor.setText(splitHistory.get(3));
                                tvTanggal.setText(splitHistory.get(4));

                                final int finalCount = count1;
                                final int finalCount1 = count2;
                                btnDetail.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Bundle bundle = new Bundle();
                                        bundle.putString("keyHistory", keyHistory.get(finalCount));
                                        bundle.putString("keyBencana", keyBencana.get(finalCount1));
                                        Fragment fragment = new DetailHistory();
                                        fragment.setArguments(bundle);
                                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                        fragmentTransaction.replace(R.id.content_frame, fragment);
                                        fragmentTransaction.addToBackStack("tag");
                                        fragmentTransaction.commit();
                                    }
                                });

                                row.addView(tvBencana);
                                row.addView(tvSektor);
                                row.addView(tvTanggal);
                                row.addView(btnDetail);
                                tableLayout.addView(row);
                                count1++;
                            }
                        }
                        iteratorBencana.next();
                        iteratorBencana.next();
                    }
                    count2++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return myView;
    }
}
