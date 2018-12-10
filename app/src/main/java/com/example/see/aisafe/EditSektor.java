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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
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
    String jumlahKerusakan, jumlahKorban, keyBencana, namaSektor;
    EditText edtInputKebutuhan, edtInputQty;
    ProgressBar progressBar;

    private ArrayList<String> infoSektor = new ArrayList<>();
    private ArrayList<String> listKebutuhan = new ArrayList<>();
    private ArrayList<String> qtyKebutuhan = new ArrayList<>();
    private List<String> updateListKebutuhan = new ArrayList<>();
    private List<String> updateQtyKebutuhan = new ArrayList<>();

    private DatabaseReference root;

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

        infoSektor = getArguments().getStringArrayList("infoSektor");
        listKebutuhan = getArguments().getStringArrayList("listKebutuhan");
        qtyKebutuhan = getArguments().getStringArrayList("qtyKebutuhan");
        keyBencana = getArguments().getString("keyBencana");
        namaSektor = getArguments().getString("namaSektor");
        updateListKebutuhan = listKebutuhan;
        updateQtyKebutuhan = qtyKebutuhan;

        progressBar = new ProgressBar(getContext());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100,100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        mainLayoutEdit.addView(progressBar,params);
        progressBar.setVisibility(View.INVISIBLE);


        jumlahKerusakan = infoSektor.get(0);
        jumlahKorban = infoSektor.get(1);

        edtJumlahKerusakan.setText(jumlahKerusakan);
        edtJumlahKorban.setText(jumlahKorban);

        System.out.println("LIST KEBUTUHAN : " + listKebutuhan);
        System.out.println("QTY KEBUTUHAN :" + qtyKebutuhan);

        final CustomAdapter adapter = new CustomAdapter(getContext(), listKebutuhan, qtyKebutuhan);
        lvKebutuhanSektor.setAdapter(adapter);

        root = FirebaseDatabase.getInstance().getReference().child("Bencana").child(keyBencana).child("Sektor").child(namaSektor);
        final DatabaseReference rootListKebutuhan = FirebaseDatabase.getInstance().getReference().child("Bencana").child(keyBencana).child("Sektor").child(namaSektor).child("List Kebutuhan");

        btnAddList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputKebutuhan();
                adapter.notifyDataSetChanged();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                try {
                    System.out.println("BERHASIL SAMPAI");
                    System.out.println(root.child("Aset Rusak"));
                    root.child("Aset Rusak").setValue(edtJumlahKerusakan.getText().toString());
                    root.child("Korban Jiwa").setValue(edtJumlahKorban.getText().toString());
                    System.out.println("BERHASIL SAMPAI SINI");
                    rootListKebutuhan.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            updateListKebutuhan(dataSnapshot, rootListKebutuhan);
                            Toast.makeText(getContext(), "Update Success", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                } catch (Exception e) {
                    System.out.println("ERRORRR : " + e.getMessage());
                    Toast.makeText(getContext(), "Update Failed", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

        return myView;
    }

    private void updateListKebutuhan(DataSnapshot dataSnapshot, DatabaseReference rootListKebutuhan) {
        Iterator iterator = dataSnapshot.getChildren().iterator();
        int count = 0;
        Map<String, Object> map = new HashMap<>();
        ArrayList<String> listKebutuhanSebelum = new ArrayList<>();
        ArrayList<String> listCheck = new ArrayList<>();
        while (iterator.hasNext()) {
            listKebutuhanSebelum.add(((DataSnapshot)iterator.next()).getKey());
        }
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
            System.out.println(map);
            rootListKebutuhan.updateChildren(map);
        }
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
