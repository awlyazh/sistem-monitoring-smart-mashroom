package com.example.smartmashroom;

import com.google.firebase.Timestamp;

public class NotifikasiItem {
    private String status;
    private Timestamp tanggal;
    private boolean selected = false; // untuk checkbox

    // Constructor kosong untuk Firebase
    public NotifikasiItem() {}

    public NotifikasiItem(String status, Timestamp tanggal) {
        this.status = status;
        this.tanggal = tanggal;
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

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
