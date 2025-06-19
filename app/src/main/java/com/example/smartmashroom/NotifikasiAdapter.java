package com.example.smartmashroom;

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

        // Reset listener checkbox saat view direcycle
        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(item.isSelected());

        // Atur visibilitas checkbox
        holder.checkBox.setVisibility(showCheckboxes ? View.VISIBLE : View.GONE);

        // Pasang listener checkbox
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            item.setSelected(isChecked);
        });

        // Klik status untuk toggle checkbox mode
        holder.textStatus.setOnClickListener(v -> {
            showCheckboxes = !showCheckboxes;

            // Reset semua dan centang yang diklik
            for (int i = 0; i < notifList.size(); i++) {
                notifList.get(i).setSelected(false);
            }

            if (showCheckboxes) {
                item.setSelected(true);
            }

            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return notifList.size();
    }

    // Optional: method tambahan kalau kamu mau atur dari luar
    public void setShowCheckboxes(boolean show) {
        this.showCheckboxes = show;
        notifyDataSetChanged();
    }

    public List<NotifikasiItem> getNotifList() {
        return notifList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        TextView textStatus, textTanggal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkBox);
            textStatus = itemView.findViewById(R.id.textStatus);
            textTanggal = itemView.findViewById(R.id.textTanggal);
        }
    }
}
