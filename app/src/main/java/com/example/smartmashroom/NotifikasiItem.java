package com.example.smartmashroom;

public class NotifikasiItem {
    private String id;
    private String status;
    private Object tanggal; // ✅ Ubah dari long → Object agar aman dari error Firebase
    private String suhu;
    private String kelembaban;
    private boolean selected = false;

    // Konstruktor kosong (wajib untuk Firebase)
    public NotifikasiItem() {
    }

    // Konstruktor utama
    public NotifikasiItem(String status, Object tanggal, String suhu, String kelembaban) {
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

    // Getter tanggal dengan pengecekan aman
    public long getTanggal() {
        if (tanggal instanceof Long) {
            return (Long) tanggal;
        } else if (tanggal instanceof Double) {
            return ((Double) tanggal).longValue(); // kalau Firebase simpan sebagai double
        } else {
            return 0L; // fallback aman
        }
    }

    // Setter tetap terima long
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
