package com.example.see.aisafe;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class listSektorFragment extends Fragment {
    View myView;
    RecyclerView rvListSektor;
    RelativeLayout layout;

    private String namaBencana;
    private List<String> listSektor = new ArrayList<>();
    private ArrayList<Sektor> listDetailSektor;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.list_sektor, container, false);

        rvListSektor = myView.findViewById(R.id.rvListSektor);

        layout = myView.findViewById(R.id.mainLayoutSektor);
        progressBar = new ProgressBar(getContext());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100,100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        layout.addView(progressBar,params);
        progressBar.setVisibility(View.VISIBLE);

        final String keyBencana = getArguments().getString("keyBencana");
        DatabaseReference root = FirebaseDatabase.getInstance().getReference().child("Bencana").child(keyBencana).child("Sektor");
        DatabaseReference rootNama = FirebaseDatabase.getInstance().getReference().child("Bencana").child(keyBencana).child("Nama");

        rootNama.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                namaBencana = dataSnapshot.getValue().toString();
                ((MainActivity)getActivity()).getSupportActionBar().setTitle(namaBencana);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterator i = dataSnapshot.getChildren().iterator();
                Iterator i1 = dataSnapshot.getChildren().iterator();
                listDetailSektor = new ArrayList<>();
                int count = 0;
                Sektor sektor;
                listSektor.clear();
                listDetailSektor.clear();

                while (i.hasNext()) {
                    listSektor.add(((DataSnapshot)i.next()).getKey());
                }

                while (i1.hasNext()) {
                    sektor = new Sektor();
                    ArrayList<String>listKebutuhanSektor = new ArrayList<>();
                    Iterator iterator = ((DataSnapshot)i1.next()).getChildren().iterator();
                    sektor.setNamaSektor(listSektor.get(count));
                    while (iterator.hasNext()) {
                        sektor.setJumlahKerusakan(((DataSnapshot)iterator.next()).getValue().toString());
                        sektor.setJumlahKorban(((DataSnapshot)iterator.next()).getValue().toString());
                        Iterator iterator1 = ((DataSnapshot)iterator.next()).getChildren().iterator();
                        while (iterator1.hasNext()) {
                            listKebutuhanSektor.add(((DataSnapshot)iterator1.next()).getValue().toString());
                        }
                        sektor.setListKebutuhan(listKebutuhanSektor);
                    }
                    listDetailSektor.add(sektor);
                    count++;
                }
                rvListSektor.setLayoutManager(new LinearLayoutManager(getActivity()));
                ListSektorAdapter adapter = new ListSektorAdapter(getContext());
                adapter.setListSektor(listDetailSektor);
                rvListSektor.setAdapter(adapter);
                rvListSektor.addItemDecoration(new DividerItemDecoration(rvListSektor.getContext(), DividerItemDecoration.VERTICAL));
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        rvListSektor.addOnItemTouchListener(new ListSektorListener(getContext(), rvListSektor, new ListSektorListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putString("keyBencana", keyBencana);
                bundle.putStringArrayList("listKebutuhanSektor", listDetailSektor.get(position).getListKebutuhan());
                bundle.putString("sektor", listSektor.get(position));
                Fragment fragment = new listKebutuhan();
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack("tag");
                fragmentTransaction.commit();
            }
        }));

        /*lvSektor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putString("keyBencana", keyBencana);
                bundle.putString("sektor", ((TextView)view).getText().toString());
                Fragment fragment = new listKebutuhan();
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack("tag");
                fragmentTransaction.commit();
            }
        });*/
        return myView;
    }
}
