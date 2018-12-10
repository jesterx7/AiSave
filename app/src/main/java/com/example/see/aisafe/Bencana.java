package com.example.see.aisafe;

import java.util.ArrayList;

/**
 * Created by user on 06/12/2018.
 */

public class Bencana {
    private String foto;
    private String namaBencana;
    private ArrayList<String> kebutuhan;
    private int jumlahKorbanJiwa;
    private long jumlahKerusakan;

    public String getNamaBencana() {
        return namaBencana;
    }

    public void setNamaBencana(String namaBencana) {
        this.namaBencana = namaBencana;
    }

    public int getJumlahKorbanJiwa() {
        return jumlahKorbanJiwa;
    }

    public void setJumlahKorbanJiwa(int jumlahKorbanJiwa) {
        this.jumlahKorbanJiwa = jumlahKorbanJiwa;
    }

    public long getJumlahKerusakan() {
        return jumlahKerusakan;
    }

    public void setJumlahKerusakan(long jumlahKerusakan) {
        this.jumlahKerusakan = jumlahKerusakan;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public ArrayList<String> getKebutuhan() {
        return kebutuhan;
    }

    public void setKebutuhan(ArrayList<String> kebutuhan) {
        this.kebutuhan = kebutuhan;
    }

}
