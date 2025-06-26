package com.example.smartmashroom;

public class Pesan {
    private String status;
    private long tanggal; // timestamp dalam bentuk long (Unix time millis)

    public Pesan() {}

    public Pesan(String status, long tanggal) {
        this.status = status;
        this.tanggal = tanggal;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getTanggal() {
        return tanggal;
    }

    public void setTanggal(long tanggal) {
        this.tanggal = tanggal;
    }
}
