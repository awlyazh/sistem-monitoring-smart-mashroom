package com.example.smartmashroom;

import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class NotifikasiAdapter extends RecyclerView.Adapter<NotifikasiAdapter.ViewHolder> {

    private List<NotifikasiItem> notifList;
    private boolean showCheckboxes = false;
    private long lastClickTime = 0;

    public NotifikasiAdapter(List<NotifikasiItem> notifList) {
        this.notifList = notifList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NotifikasiItem item = notifList.get(position);
        long waktuMillis = item.getTanggal();

        // Zona waktu WITA (Kalimantan Selatan)
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Makassar");
        Calendar cal = Calendar.getInstance(timeZone);
        cal.setTimeInMillis(waktuMillis);

        Calendar now = Calendar.getInstance(timeZone);
        Calendar nowCopy = (Calendar) now.clone(); // ➕ Untuk perbandingan

        String tanggalFormatted;
        SimpleDateFormat jamFormatter = new SimpleDateFormat("HH:mm", new Locale("id", "ID"));
        jamFormatter.setTimeZone(timeZone);

        if (isSameDay(cal, now)) {
            tanggalFormatted = "Hari ini, " + jamFormatter.format(cal.getTime());
        } else if (isYesterday(cal, nowCopy)) {
            tanggalFormatted = "Kemarin, " + jamFormatter.format(cal.getTime());
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy, HH:mm", new Locale("id", "ID"));
            sdf.setTimeZone(timeZone);
            tanggalFormatted = sdf.format(cal.getTime());
        }

        holder.textStatus.setText(item.getStatus());
        holder.textTanggal.setText(tanggalFormatted);
        holder.textSuhu.setText("Suhu: " + item.getSuhu());
        holder.textKelembaban.setText("Kelembaban: " + item.getKelembaban());

        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(item.isSelected());
        holder.checkBox.setVisibility(showCheckboxes ? View.VISIBLE : View.GONE);

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            item.setSelected(isChecked);
        });

        holder.itemView.setOnClickListener(v -> {
            long clickTime = SystemClock.elapsedRealtime();
            if (clickTime - lastClickTime < 400) {
                showCheckboxes = !showCheckboxes;
                notifyDataSetChanged();
            }
            lastClickTime = clickTime;
        });
    }

    private boolean isSameDay(Calendar cal1, Calendar cal2) {
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }

    private boolean isYesterday(Calendar cal, Calendar reference) {
        Calendar yesterday = (Calendar) reference.clone();
        yesterday.add(Calendar.DAY_OF_YEAR, -1);
        return isSameDay(cal, yesterday);
    }

    @Override
    public int getItemCount() {
        return notifList.size();
    }

    public void setShowCheckboxes(boolean show) {
        this.showCheckboxes = show;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        TextView textStatus, textTanggal, textSuhu, textKelembaban;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkBox);
            textStatus = itemView.findViewById(R.id.textStatus);
            textTanggal = itemView.findViewById(R.id.textTanggal);
            textSuhu = itemView.findViewById(R.id.textSuhu);
            textKelembaban = itemView.findViewById(R.id.textKelembaban);
        }
    }
}
