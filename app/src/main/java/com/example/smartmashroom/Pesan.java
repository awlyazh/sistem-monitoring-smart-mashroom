package com.example.smartmashroom;

import com.google.firebase.Timestamp;

public class Pesan {
    private String status;
    private Timestamp tanggal;

    public Pesan() {}

    public Pesan(String status, Timestamp tanggal) {
        this.status = status;
        this.tanggal = tanggal;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getTanggal() {
        return tanggal;
    }

    public void setTanggal(Timestamp tanggal) {
        this.tanggal = tanggal;
    }
}
