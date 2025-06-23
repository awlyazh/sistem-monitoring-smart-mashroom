// NotifikasiAdapter.java - Support Double Click Checkbox

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
import java.util.List;
import java.util.Locale;

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

        String tanggalFormatted = item.getTanggal() != null ?
                new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
                        .format(item.getTanggal().toDate()) :
                "Tanggal tidak tersedia";

        holder.textStatus.setText(item.getStatus());
        holder.textTanggal.setText("[" + tanggalFormatted + "]");
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
            if (clickTime - lastClickTime < 400) { // double click threshold
                showCheckboxes = !showCheckboxes;
                notifyDataSetChanged();
            }
            lastClickTime = clickTime;
        });
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
