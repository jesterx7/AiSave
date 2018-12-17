package com.example.see.aisafe;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class EditSektor extends Fragment {
    View myView;
    EditText edtJumlahKerusakan, edtJumlahKorban;
    ListView lvKebutuhanSektor;
    RelativeLayout mainLayoutEdit;
    Button btnAddList, btnUpdate;
    String jumlahKerusakan, jumlahKorban, keyBencana, namaSektor, namaUser, formattedDate, time;
    EditText edtInputKebutuhan, edtInputQty;
    ProgressBar progressBar;
    int statusUpdate = 1;

    private ArrayList<String> infoSektor;
    private ArrayList<String> listKebutuhan;
    private ArrayList<String> qtyKebutuhan;
    private ArrayList<String> listKebutuhanSebelum;
    ArrayList<String> detailUpdated;

    private DatabaseReference root;
    DatabaseReference rootHistory;
    DatabaseReference rootHistory2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.edit_sektor, container, false);

        edtJumlahKerusakan = myView.findViewById(R.id.edtQtyKerusakan);
        edtJumlahKorban = myView.findViewById(R.id.edtQtyKorban);
        lvKebutuhanSektor = myView.findViewById(R.id.lvKebutuhanSektor);
        btnAddList = myView.findViewById(R.id.btnAddList);
        btnUpdate = myView.findViewById(R.id.btnSubmit);
        mainLayoutEdit = myView.findViewById(R.id.mainLayoutEdit);

        infoSektor = new ArrayList<>();
        listKebutuhan = new ArrayList<>();
        qtyKebutuhan = new ArrayList<>();
        listKebutuhanSebelum = new ArrayList<>();
        detailUpdated = new ArrayList<>();

        infoSektor = getArguments().getStringArrayList("infoSektor");
        listKebutuhan = getArguments().getStringArrayList("listKebutuhan");
        qtyKebutuhan = getArguments().getStringArrayList("qtyKebutuhan");
        keyBencana = getArguments().getString("keyBencana");
        namaSektor = getArguments().getString("namaSektor");
        namaUser = getArguments().getString("nama");
        System.out.println("NAMAAAAAAAA DI EDIT" + namaUser);
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        formattedDate = df.format(c);
        SimpleDateFormat dfTime = new SimpleDateFormat("hh:mm aa");
        time = dfTime.format(c);

        listKebutuhanSebelum = listKebutuhan;

        progressBar = new ProgressBar(getContext());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100,100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        mainLayoutEdit.addView(progressBar,params);
        progressBar.setVisibility(View.INVISIBLE);

        jumlahKerusakan = infoSektor.get(0);
        jumlahKorban = infoSektor.get(1);

        edtJumlahKerusakan.setText(jumlahKerusakan);
        edtJumlahKorban.setText(jumlahKorban);

        root = FirebaseDatabase.getInstance().getReference().child("Bencana").child(keyBencana).child("Sektor").child(namaSektor);
        final DatabaseReference rootListKebutuhan = FirebaseDatabase.getInstance().getReference().child("Bencana").child(keyBencana).child("Sektor").child(namaSektor).child("List Kebutuhan");
        DatabaseReference rootBencana = FirebaseDatabase.getInstance().getReference().child("Bencana").child(keyBencana).child("History");
        String newKey = rootBencana.push().getKey();
        String newKey2 = rootBencana.push().getKey();
        rootHistory = rootBencana.child(newKey);
        rootHistory2 = rootBencana.child(newKey2);
        final CustomAdapter adapter = new CustomAdapter(getContext(), listKebutuhan, qtyKebutuhan, 1, namaUser, namaSektor);
        adapter.setRoot(root);
        adapter.setRootBencana(rootBencana);
        adapter.setRootListKebutuhan(rootListKebutuhan);
        lvKebutuhanSektor.setAdapter(adapter);

        btnAddList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (statusUpdate == 1){
                    rootListKebutuhan.addValueEventListener(new ValueEventListener() {
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
                            listKebutuhan.clear();
                            while (iterator.hasNext()) {
                                Iterator iterator1 = ((DataSnapshot)iterator.next()).getChildren().iterator();
                                while (iterator1.hasNext()) {
                                    listKebutuhan.add(((DataSnapshot)iterator1.next()).getKey().toString());
                                }

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    statusUpdate = 0;
                }
                inputKebutuhan();
                adapter.notifyDataSetChanged();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                try {
                    root.child("Aset Rusak").setValue(edtJumlahKerusakan.getText().toString());
                    root.child("Korban Jiwa").setValue(edtJumlahKorban.getText().toString());
                    updateListKebutuhan(rootListKebutuhan);
                    rootListKebutuhan.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Iterator i = dataSnapshot.getChildren().iterator();
                            Iterator i1 = dataSnapshot.getChildren().iterator();
                            listKebutuhan.clear();
                            qtyKebutuhan.clear();
                            while (i.hasNext()) {
                                qtyKebutuhan.add(((DataSnapshot)i.next()).getValue().toString());
                            }
                            while (i1.hasNext()) {
                                listKebutuhan.add(((DataSnapshot)i1.next()).getKey());
                            }
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    Toast.makeText(getContext(), "Update Successfull", Toast.LENGTH_SHORT).show();
                    statusUpdate = 1;
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Update Failed", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

        return myView;
    }

    private void updateListKebutuhan(DatabaseReference rootListKebutuhan) {
        int count = 0;
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> mapHistory = new HashMap<>();
        ArrayList<String> listCheck = new ArrayList<>();
        String detailUpdate = "";
        for (String s : listKebutuhanSebelum) {
            for (String s1 : listKebutuhan) {
                if (s.equals(s1)) {
                    rootListKebutuhan.child(s).setValue(qtyKebutuhan.get(count));
                    listCheck.add(s);
                    count++;
                }
            }
        }
        count = 0;
        for (String s2 : listKebutuhan) {
            Boolean check = true;
            for (String s3 : listCheck) {
                if (s2.equals(s3)) {
                    check = false;
                }
            }
            if (check) {
                map.put(s2.toString(), qtyKebutuhan.get(count).toString());
            }
            count++;
        }
        if (map != null) {
            for (String s: detailUpdated) {
                detailUpdate += s+" \n";
            }
            rootListKebutuhan.updateChildren(map);
            mapHistory.put("Username", namaUser);
            mapHistory.put("Tanggal", formattedDate);
            mapHistory.put("Waktu", time);
            mapHistory.put("Task", "Add List");
            mapHistory.put("Detail", detailUpdate);
            mapHistory.put("Kerusakan", edtJumlahKerusakan.getText().toString());
            mapHistory.put("Korban", edtJumlahKorban.getText().toString());
            mapHistory.put("Sektor", namaSektor);
            rootHistory.updateChildren(mapHistory);
        }
        listKebutuhanSebelum = listKebutuhan;
        listKebutuhan.clear();
        detailUpdated.clear();
    }

    private void inputKebutuhan() {
        LinearLayout layoutInput = new LinearLayout(getContext());
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Input Kebutuhan Baru");

        edtInputKebutuhan = new EditText(getContext());
        edtInputQty = new EditText(getContext());

        edtInputKebutuhan.setInputType(InputType.TYPE_CLASS_TEXT);
        edtInputQty.setInputType(InputType.TYPE_CLASS_NUMBER);

        edtInputKebutuhan.setHint("Input Kebutuhan");
        edtInputQty.setHint("Jumlah");

        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
        edtInputKebutuhan.setLayoutParams(p);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutInput.setLayoutParams(params);
        layoutInput.setOrientation(LinearLayout.HORIZONTAL);

        layoutInput.addView(edtInputKebutuhan);
        layoutInput.addView(edtInputQty);

        builder.setView(layoutInput);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listKebutuhan.add(edtInputKebutuhan.getText().toString());
                qtyKebutuhan.add(edtInputQty.getText().toString());
                detailUpdated.add(edtInputKebutuhan.getText().toString());
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}
