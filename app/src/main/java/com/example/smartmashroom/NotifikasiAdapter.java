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
    public NotifikasiAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotifikasiAdapter.ViewHolder holder, int position) {
        NotifikasiItem item = notifList.get(position);
        String tanggalFormatted = item.getTanggal() != null ?
                new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
                        .format(item.getTanggal().toDate()) :
                "Tanggal tidak tersedia";

        holder.checkBox.setText("[" + tanggalFormatted + "] " + item.getStatus());
        holder.checkBox.setChecked(item.isSelected());

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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.textTitle); // Sesuaikan dengan id checkbox di item_notification.xml
        }
    }
}
