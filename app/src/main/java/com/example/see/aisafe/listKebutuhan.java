package com.example.see.aisafe;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.internal.NavigationMenu;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;

public class listKebutuhan extends Fragment {
    View myView;
    RelativeLayout layout;
    LinearLayout llListKebutuhan;
    TextView tvQtyKerusakan, tvQtyKorban;
    ListView lvKebutuhan;
    FabSpeedDial fabMenu;

    private String namaSektor, lokasiSektor, namaUser;
    private ArrayList<String> listKebutuhan;
    private ArrayList<String> qtyKebutuhan;
    private ArrayList<String> infoSektor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.list_kebutuhan, container, false);

        tvQtyKerusakan = myView.findViewById(R.id.tvQtyKerusakan);
        tvQtyKorban = myView.findViewById(R.id.tvQtyKorban);
        llListKebutuhan = myView.findViewById(R.id.llListKebutuhan);
        lvKebutuhan = myView.findViewById(R.id.lvKebutuhan);
        fabMenu = myView.findViewById(R.id.fabMenu);

        final String keyBencana = getArguments().getString("keyBencana");
        namaSektor = getArguments().getString("sektor");
        namaUser = getArguments().getString("nama");

        listKebutuhan = new ArrayList<>();
        qtyKebutuhan = new ArrayList<>();
        infoSektor = new ArrayList<>();

        final ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, listKebutuhan);
        final CustomAdapter customAdapter = new CustomAdapter(getContext(), listKebutuhan, qtyKebutuhan, 0, namaUser, namaSektor);

        DatabaseReference root = FirebaseDatabase.getInstance().getReference().child("Bencana").child(keyBencana).child("Sektor").child(namaSektor);
        DatabaseReference rootQtyKebutuhan = FirebaseDatabase.getInstance().getReference().child("Bencana").child(keyBencana).child("Sektor").child(namaSektor).child("List Kebutuhan");

        ((MainActivity)getActivity()).getSupportActionBar().setTitle(namaSektor);

        fabMenu.setMenuListener(new FabSpeedDial.MenuListener() {
            @Override
            public boolean onPrepareMenu(NavigationMenu navigationMenu) {
                return true;
            }

            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                if (menuItem.getTitle().equals("Edit Kebutuhan")) {
                    System.out.println("NAMAAAAAAAA DI KEBUTUHAN" + namaUser);
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("infoSektor", infoSektor);
                    bundle.putStringArrayList("qtyKebutuhan", qtyKebutuhan);
                    bundle.putStringArrayList("listKebutuhan", listKebutuhan);
                    bundle.putString("keyBencana", keyBencana);
                    bundle.putString("namaSektor", namaSektor);
                    bundle.putString("nama", namaUser);
                    Fragment fragment = new EditSektor();
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame, fragment);
                    fragmentTransaction.addToBackStack("tag");
                    fragmentTransaction.commit();
                } else if (menuItem.getTitle().equals("Location")){
                    Bundle bundle = new Bundle();
                    bundle.putString("lokasiSektor", lokasiSektor);
                    Fragment fragment = new SektorGMap();
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame, fragment);
                    fragmentTransaction.addToBackStack("tag");
                    fragmentTransaction.commit();
                } else if (menuItem.getTitle().equals("History Edit")){
                    Bundle bundle = new Bundle();
                    bundle.putString("namaSektor", namaSektor);
                    bundle.putString("keyBencana", keyBencana);
                    Fragment fragment = new HistorySektorFragment();
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame, fragment);
                    fragmentTransaction.addToBackStack("tag");
                    fragmentTransaction.commit();
                }
                return true;
            }

            @Override
            public void onMenuClosed() {

            }
        });

        rootQtyKebutuhan.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterator i = dataSnapshot.getChildren().iterator();
                qtyKebutuhan.clear();
                while (i.hasNext()) {
                    qtyKebutuhan.add(((DataSnapshot)i.next()).getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterator iterator = dataSnapshot.getChildren().iterator();
                infoSektor.clear();
                while (iterator.hasNext()) {
                    infoSektor.add(((DataSnapshot)iterator.next()).getValue().toString());
                    infoSektor.add(((DataSnapshot)iterator.next()).getValue().toString());
                    Iterator iterator1 = ((DataSnapshot)iterator.next()).getChildren().iterator();
                    while (iterator1.hasNext()) {
                        listKebutuhan.add(((DataSnapshot)iterator1.next()).getKey().toString());
                    }
                    lokasiSektor = ((DataSnapshot)iterator.next()).getValue().toString();
                }
                tvQtyKerusakan.setText(infoSektor.get(0) + " Bangunan");
                tvQtyKorban.setText(infoSektor.get(1) + " Orang");
                adapter.notifyDataSetChanged();
                customAdapter.notifyDataSetChanged();
                lvKebutuhan.setAdapter(customAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return myView;
    }


}
