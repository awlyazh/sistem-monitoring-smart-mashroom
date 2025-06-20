package com.example.smartmashroom;

import com.google.firebase.Timestamp;

public class NotifikasiItem {
    private String status;
    private Timestamp tanggal;
    private String suhu;
    private String kelembaban;
    private boolean selected = false; // untuk checkbox

    // Constructor kosong untuk Firebase
    public NotifikasiItem() {}

    // Constructor lengkap
    public NotifikasiItem(String status, Timestamp tanggal, String suhu, String kelembaban) {
        this.status = status;
        this.tanggal = tanggal;
        this.suhu = suhu;
        this.kelembaban = kelembaban;
    }

    // Getter dan Setter
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

    public String getSuhu() {
        return suhu;
    }

    public void setSuhu(String suhu) {
        this.suhu = suhu;
    }

    public String getKelembaban() {
        return kelembaban;
    }

    public void setKelembaban(String kelembaban) {
        this.kelembaban = kelembaban;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
