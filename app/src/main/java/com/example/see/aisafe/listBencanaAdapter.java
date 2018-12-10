package com.example.see.aisafe;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by user on 06/12/2018.
 */

public class listBencanaAdapter extends RecyclerView.Adapter<listBencanaAdapter.BencanaViewHolder>{
    private Context context;
    private ArrayList<Bencana> listBencana;
    private static ClickListener clickListener;

    public interface ClickListener {
        void onItemClick(int position, View view);
    }

    public listBencanaAdapter(Context context) { this.context = context;}

    public ArrayList<Bencana> getListBencana() {return listBencana;}

    public void setListBencana(ArrayList<Bencana> listBencana) {this.listBencana = listBencana;}

    @NonNull
    @Override
    public listBencanaAdapter.BencanaViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemRow = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_bencana, viewGroup, false);
        return new BencanaViewHolder(itemRow);
    }

    @Override
    public void onBindViewHolder(@NonNull final listBencanaAdapter.BencanaViewHolder bencanaViewHolder, int i) {
        bencanaViewHolder.tvNamaBencana.setText(getListBencana().get(i).getNamaBencana());
        bencanaViewHolder.tvJumlahKorban.setText("Korban Jiwa " + String.valueOf(getListBencana().get(i).getJumlahKorbanJiwa()) + " Orang");
        bencanaViewHolder.tvJumlahKerusakan.setText("Kerugian Rp. " + NumberFormat.getNumberInstance(Locale.US).format(getListBencana().get(i).getJumlahKerusakan()));
        Glide.with(context)
                .load(getListBencana().get(i).getFoto())
                .crossFade()
                .into(bencanaViewHolder.imageBencana);
        Glide.with(context)
                .load(getListBencana().get(i).getFoto())
                .crossFade()
                .into(bencanaViewHolder.circleImageView);
        bencanaViewHolder.btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("keyBencana", bencanaViewHolder.listKey.get(bencanaViewHolder.getAdapterPosition()));
                Fragment fragment = new listSektorFragment();
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = ((FragmentActivity)v.getContext()).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack("tag");
                fragmentTransaction.commit();
            }
        });

        bencanaViewHolder.btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("keyBencana", bencanaViewHolder.listKey.get(bencanaViewHolder.getAdapterPosition()));
                Fragment fragment = new HistoryBencanaFragment();
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = ((FragmentActivity)v.getContext()).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack("tag");
                fragmentTransaction.commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return getListBencana().size();
    }

    public class BencanaViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView circleImageView;
        private ImageView imageBencana;
        private TextView tvJumlahKorban, tvJumlahKerusakan, tvNamaBencana;
        private Button btnInfo;
        private Button btnHistory;

        private ArrayList<String> listKey = new ArrayList<>();

        public BencanaViewHolder(@NonNull final View itemView) {
            super(itemView);

            imageBencana = itemView.findViewById(R.id.imageBencana);
            tvNamaBencana = itemView.findViewById(R.id.tvNamaBencana);
            tvJumlahKorban = itemView.findViewById(R.id.tvKorban);
            tvJumlahKerusakan = itemView.findViewById(R.id.tvKerusakan);
            circleImageView = itemView.findViewById(R.id.circleImage);
            btnInfo = itemView.findViewById(R.id.btnInfo);
            btnHistory = itemView.findViewById(R.id.btnHistory);

            DatabaseReference root = FirebaseDatabase.getInstance().getReference().child("Bencana");
            root.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Iterator iterator = dataSnapshot.getChildren().iterator();
                    while (iterator.hasNext()) {
                        listKey.add(((DataSnapshot)iterator.next()).getKey());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}
