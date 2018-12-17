package com.example.see.aisafe;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends Activity {
    private EditText edtEmailRegister, edtPasswordRegister, edtNamaUser, edtFotoUser, edtKotaAsal, edtNoHp, edtUmur;
    private Button btnRegister;
    private RelativeLayout layout;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference root;
    ProgressBar progressBar;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edtEmailRegister = findViewById(R.id.edtEmailRegister);
        edtPasswordRegister = findViewById(R.id.edtPasswordRegister);
        btnRegister = findViewById(R.id.btnRegister);
        layout = findViewById(R.id.mainLayoutRegister);
        edtNamaUser = findViewById(R.id.edtNamaUser);
        edtFotoUser = findViewById(R.id.edtUrlFoto);
        edtKotaAsal = findViewById(R.id.edtKotaAsal);
        edtNoHp = findViewById(R.id.edtNoHP);
        edtUmur = findViewById(R.id.edtUmurUser);

        firebaseAuth = FirebaseAuth.getInstance();
        root = FirebaseDatabase.getInstance().getReference().child("user");

        progressBar = new ProgressBar(this);
        progressBar.setIndeterminateTintList(ColorStateList.valueOf(Color.parseColor("#028e00")));
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100,100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        layout.addView(progressBar,params);
        progressBar.setVisibility(View.INVISIBLE);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                registerUser();
            }
        });
    }

    private void registerUser() {
        final String email = edtEmailRegister.getText().toString().trim();
        String password = edtPasswordRegister.getText().toString().trim();

        if (TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(edtFotoUser.getText().toString())){
            Toast.makeText(this, "Please enter Foto URL", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(edtNamaUser.getText().toString())){
            Toast.makeText(this, "Please enter Nama", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(edtKotaAsal.getText().toString())){
            Toast.makeText(this, "Please enter Kota Asal", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(edtNoHp.getText().toString())){
            Toast.makeText(this, "Please enter No HP", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(edtUmur.getText().toString())){
            Toast.makeText(this, "Please enter Umur", Toast.LENGTH_SHORT).show();
            return;
        }
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "Registered Succesfully", Toast.LENGTH_SHORT).show();
                            if (firebaseAuth.getCurrentUser() != null){
                                finish();
                                Map<String, Object> map = new HashMap<>();
                                map.put("Email", email);
                                map.put("Foto", edtFotoUser.getText().toString());
                                map.put("Kota Asal", edtKotaAsal.getText().toString());
                                map.put("Nama", edtNamaUser.getText().toString());
                                map.put("No Hp", edtNoHp.getText().toString());
                                map.put("Umur", edtUmur.getText().toString());
                                String newKey = root.push().getKey();
                                DatabaseReference newRoot = root.child(newKey);
                                newRoot.updateChildren(map);
                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                intent.putExtra("email", email);
                                startActivity(intent);
                                progressBar.setVisibility(View.GONE);
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Could not register, please try again", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }
}
