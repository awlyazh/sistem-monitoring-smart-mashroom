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

        // Format tanggal
        String tanggalFormatted = item.getTanggal() != null ?
                new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
                        .format(item.getTanggal().toDate()) :
                "Tanggal tidak tersedia";

        // Set teks ke TextView, bukan ke CheckBox
        holder.textStatus.setText(item.getStatus());
        holder.textTanggal.setText("[" + tanggalFormatted + "]");

        // Set status CheckBox
        holder.checkBox.setChecked(item.isSelected());

        // Listener perubahan checkbox
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            item.setSelected(isChecked);
        });
    }

    @Override
    public int getItemCount() {
        return notifList.size();
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
