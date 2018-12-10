package com.example.see.aisafe;

import java.util.ArrayList;

/**
 * Created by user on 07/12/2018.
 */

class Sektor {
    private String namaSektor;
    private String jumlahKorban;
    private String jumlahKerusakan;
    private ArrayList<String> listKebutuhan;

    public String getNamaSektor() {
        return namaSektor;
    }

    public void setNamaSektor(String namaSektor) {
        this.namaSektor = namaSektor;
    }

    public String getJumlahKorban() {
        return jumlahKorban;
    }

    public void setJumlahKorban(String jumlahKorban) {
        this.jumlahKorban = jumlahKorban;
    }

    public String getJumlahKerusakan() {
        return jumlahKerusakan;
    }

    public void setJumlahKerusakan(String jumlahKerusakan) {
        this.jumlahKerusakan = jumlahKerusakan;
    }

    public ArrayList<String> getListKebutuhan() {
        return listKebutuhan;
    }

    public void setListKebutuhan(ArrayList<String> listKebutuhan) {
        this.listKebutuhan = listKebutuhan;
    }
}

