package com.example.smartmashroom;

public class Pesan {
    private String status;
    private Object tanggal; // ✅ Disimpan sebagai Object agar bisa handle Long atau Double dari Firebase

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

    // ✅ Getter fleksibel: bisa handle jika tanggal berupa Long atau Double
    public long getTanggal() {
        if (tanggal instanceof Long) {
            return (Long) tanggal;
        } else if (tanggal instanceof Double) {
            return ((Double) tanggal).longValue();
        } else {
            return System.currentTimeMillis(); // fallback kalau tidak diketahui
        }
    }

    // ✅ Setter: masih menerima long agar fleksibel saat membuat objek
    public void setTanggal(long tanggal) {
        this.tanggal = tanggal;
    }

    // (Opsional) Jika kamu butuh ambil raw-nya sebagai Object
    public Object getRawTanggal() {
        return tanggal;
    }
}
