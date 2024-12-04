package com.example.cartandorder.contact.chatAndCall;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cartandorder.R;
import com.example.cartandorder.ReponseApi.User;


import java.time.format.DateTimeFormatter;
import java.util.List;

public class CallListAdapter extends RecyclerView.Adapter<CallListAdapter.CallViewHolder> {
    private List<CallItem> callList;
    private OnChatClickListener onChatClickListener;
    private Long userId;
    public interface OnChatClickListener {
        void onCallClick(CallItem callItem);

    }
    public CallListAdapter(Long userId,List<CallItem> callList,OnChatClickListener onChatClickListener) {
        this.callList = callList;
        this.onChatClickListener = onChatClickListener;
        this.userId = userId;
    }

    @NonNull
    @Override
    public CallViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_call, parent, false);
        return new CallViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CallViewHolder holder, int position) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        CallItem callItem = callList.get(position);

        User  user = callItem.getSender().getUserId().equals(userId)? callItem.getReceiver():callItem.getSender();
        holder.callName.setText(user.getFullName());
        String direction = callItem.getSender().getUserId().equals(userId) ? "Outgoing" : "Incoming";
        String formattedTime = callItem.getTime().format(formatter);
        holder.time.setText(direction + " | " + formattedTime);
        Log.d("CallListAdapter", "Time: " + callItem.getTime());


        Glide.with(holder.itemView.getContext())
                .load(user.getAvatar())
                .into(holder.callImage);

        holder.btnCall.setOnClickListener(v -> {
            if (onChatClickListener != null) {
                onChatClickListener.onCallClick(callItem);
            }
        });
    }


    @Override
    public int getItemCount() {
        return callList.size();
    }

    static class CallViewHolder extends RecyclerView.ViewHolder {
        ImageView callImage;
        TextView callName,time;
        Button btnCall;

        public CallViewHolder(@NonNull View itemView) {
            super(itemView);
            callImage = itemView.findViewById(R.id.imageChat);
            callName = itemView.findViewById(R.id.chatName);
            time = itemView.findViewById(R.id.chatMessage);
            btnCall = itemView.findViewById(R.id.btnMessage);
        }
    }
}
