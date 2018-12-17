package com.example.see.aisafe;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsFragment extends Fragment {
    View myView;
    private TextView tvEditNama, tvEditEmail;
    private EditText edtEditKotaAsal, edtEditFoto, edtEditNoHp, edtEditUmur;
    private Button btnUpdateProfile;
    private CircleImageView circleImageViewEditFoto;

    private String email;
    private DatabaseReference root;
    int fixPosition, count;

    ArrayList<String> userKey;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.settings_profile, container, false);

        tvEditNama = myView.findViewById(R.id.tvEditNamaUser);
        tvEditEmail = myView.findViewById(R.id.tvEditEmailUser);
        edtEditKotaAsal = myView.findViewById(R.id.edtEditKotaAsal);
        edtEditFoto = myView.findViewById(R.id.edtEditFoto);
        edtEditNoHp = myView.findViewById(R.id.edtEditNoHp);
        edtEditUmur = myView.findViewById(R.id.edtEditUmur);
        btnUpdateProfile = myView.findViewById(R.id.btnUpdateProfile);
        circleImageViewEditFoto = myView.findViewById(R.id.circleImageEditFoto);

        email = getArguments().getString("email");
        root = FirebaseDatabase.getInstance().getReference().child("user");

        tvEditEmail.setText(email);

        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterator iterator = dataSnapshot.getChildren().iterator();
                Iterator iteratorKey = dataSnapshot.getChildren().iterator();
                userKey = new ArrayList<>();
                count = 0;
                while (iteratorKey.hasNext()) {
                    userKey.add(((DataSnapshot)iteratorKey.next()).getKey());
                }
                while (iterator.hasNext()) {
                    Iterator iteratorUser = ((DataSnapshot)iterator.next()).getChildren().iterator();
                    while (iteratorUser.hasNext()) {
                        if (email.equals(((DataSnapshot) iteratorUser.next()).getValue().toString())) {
                            edtEditFoto.setText(((DataSnapshot) iteratorUser.next()).getValue().toString());
                            Glide.with(getContext())
                                    .load(edtEditFoto.getText().toString())
                                    .crossFade()
                                    .into(circleImageViewEditFoto);
                            edtEditKotaAsal.setText(((DataSnapshot) iteratorUser.next()).getValue().toString());
                            tvEditNama.setText(((DataSnapshot) iteratorUser.next()).getValue().toString());
                            edtEditNoHp.setText(((DataSnapshot) iteratorUser.next()).getValue().toString());
                            edtEditUmur.setText(((DataSnapshot) iteratorUser.next()).getValue().toString());
                            fixPosition = count;
                        }
                    }
                    count++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference userRoot = root.child(userKey.get(fixPosition));
                if (TextUtils.isEmpty(edtEditFoto.getText().toString())) {
                    Toast.makeText(getContext(), "Please fill all the Field", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(edtEditKotaAsal.getText().toString())) {
                    Toast.makeText(getContext(), "Please fill all the Field", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(edtEditNoHp.getText().toString())) {
                    Toast.makeText(getContext(), "Please fill all the Field", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(edtEditUmur.getText().toString())) {
                    Toast.makeText(getContext(), "Please fill all the Field", Toast.LENGTH_SHORT).show();
                } else {
                    userRoot.child("Foto").setValue(edtEditFoto.getText().toString());
                    userRoot.child("Kota Asal").setValue(edtEditKotaAsal.getText().toString());
                    userRoot.child("No Hp").setValue(edtEditNoHp.getText().toString());
                    userRoot.child("Umur").setValue(edtEditUmur.getText().toString());
                    Toast.makeText(getContext(), "Update Profile Succesfull", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return myView;
    }
}
