package com.example.cartandorder.notification;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cartandorder.R;
import com.example.cartandorder.home.HomeAdapter;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class NotiAdapter extends RecyclerView.Adapter<NotiAdapter.NotificationViewHolder> {
    private List<Notification> listNotice;


    public NotiAdapter(List<Notification> listnote){
        this.listNotice = listnote;
    }
    @NonNull
    @Override
    public NotiAdapter.NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notice, parent, false);
        return new NotiAdapter.NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotiAdapter.NotificationViewHolder holder, int position) {
        Notification notice = listNotice.get(position);
        holder.title.setText(notice.getTitle());
        holder.message.setText(notice.getMessage());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");


        String formattedDate = notice.getCreatedAt().format(formatter);


        holder.timeNotice.setText(formattedDate);
    }

    @Override
    public int getItemCount() {
        return listNotice.size();
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder{

        TextView title,message,timeNotice;
        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            message = itemView.findViewById(R.id.message);
            timeNotice = itemView.findViewById(R.id.timeNotice);
        }
    }
}
