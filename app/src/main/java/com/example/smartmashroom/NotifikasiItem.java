package com.example.smartmashroom;

public class NotifikasiItem {
    private String id;
    private String status;
    private long tanggal; // Ganti Timestamp jadi long (Unix time in millis)
    private String suhu;
    private String kelembaban;
    private boolean selected = false;

    // Konstruktor kosong wajib untuk Firebase
    public NotifikasiItem() {
    }

    // Konstruktor utama
    public NotifikasiItem(String status, long tanggal, String suhu, String kelembaban) {
        this.status = status;
        this.tanggal = tanggal;
        this.suhu = suhu;
        this.kelembaban = kelembaban;
    }

    // Getter & Setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
