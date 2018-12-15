package com.example.see.aisafe;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class listBencanaFragment extends Fragment {
    View myView;
    RecyclerView rvListBencana;
    RelativeLayout layout;

    private DatabaseReference root;
    private ProgressBar progressBar;

    private ArrayList<Bencana> LIST_BENCANA;
    private ArrayList<String>listKey = new ArrayList<>();


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.list_bencana, container, false);


        layout = myView.findViewById(R.id.mainLayoutBencana);
        rvListBencana = myView.findViewById(R.id.rvListBencana);

        root = FirebaseDatabase.getInstance().getReference().getRoot();
        DatabaseReference bencanaDatabase = root.child("Bencana");

        ((MainActivity)getActivity()).getSupportActionBar().setTitle("List Bencana");

        progressBar = new ProgressBar(getContext());
        progressBar.setIndeterminateTintList(ColorStateList.valueOf(Color.parseColor("#028e00")));
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100,100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        layout.addView(progressBar,params);
        progressBar.setVisibility(View.VISIBLE);


        bencanaDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Bencana bencana;
                Iterator i= dataSnapshot.getChildren().iterator();
                LIST_BENCANA = new ArrayList<>();

                while (i.hasNext()) {
                    bencana = new Bencana();
                    int jumlahKorban = 0;
                    long jumlahKerusakan = 0;
                    ArrayList<String> listKebutuhanSektor = new ArrayList<>();
                    Iterator iterator = ((DataSnapshot)i.next()).getChildren().iterator();
                    while (iterator.hasNext()) {
                        bencana.setFoto(((DataSnapshot)iterator.next()).getValue().toString());
                        iterator.next();
                        bencana.setNamaBencana(((DataSnapshot)iterator.next()).getValue().toString());
                        Iterator iterator1 = ((DataSnapshot)iterator.next()).getChildren().iterator();
                        while (iterator1.hasNext()) {
                            Iterator iterator2 = ((DataSnapshot)iterator1.next()).getChildren().iterator();
                            while (iterator2.hasNext()) {
                                jumlahKerusakan += Long.parseLong(((DataSnapshot)iterator2.next()).getValue().toString());
                                jumlahKorban += Integer.parseInt(((DataSnapshot)iterator2.next()).getValue().toString());
                                Iterator iterator3 = ((DataSnapshot)iterator2.next()).getChildren().iterator();
                                while (iterator3.hasNext()) {
                                    listKebutuhanSektor.add(((DataSnapshot)iterator3.next()).getValue().toString());
                                }
                                iterator2.next();
                            }
                        }
                        bencana.setJumlahKerusakan(jumlahKerusakan);
                        bencana.setJumlahKorbanJiwa(jumlahKorban);
                        bencana.setKebutuhan(listKebutuhanSektor);
                    }
                    LIST_BENCANA.add(bencana);
                }

                i= dataSnapshot.getChildren().iterator();
                while (i.hasNext()) {
                    listKey.add(((DataSnapshot)i.next()).getKey());
                }
                rvListBencana.setLayoutManager(new LinearLayoutManager(getActivity()));
                listBencanaAdapter adapter = new listBencanaAdapter(getContext());
                adapter.setListBencana(LIST_BENCANA);
                rvListBencana.setAdapter(adapter);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return myView;
    }
}
