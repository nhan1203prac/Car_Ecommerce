package com.example.cartandorder.contact.chatAndCall;

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
import com.example.cartandorder.contact.ChatAdapter;
import com.example.cartandorder.favourite.Car;
import com.example.cartandorder.favourite.CarAdapter;

import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatViewHolder> {
    private List<ChatItem> chatList;
    private OnChatClickListener onChatClickListener;

    public interface OnChatClickListener {
        void onCallClick(ChatItem chartItem);

    }
    public ChatListAdapter(List<ChatItem> chatList,OnChatClickListener onChatClickListener) {
        this.chatList = chatList;
        this.onChatClickListener = onChatClickListener;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {


        ChatItem chatItem = chatList.get(position);
        holder.chatName.setText(chatItem.getName());
        holder.chatMessage.setText(chatItem.getMessage());
        Glide.with(holder.itemView.getContext())
                .load(chatItem.getImage())
                .into(holder.chatImage);

        holder.btnMessage.setOnClickListener(v -> {
            if (onChatClickListener != null) {
                onChatClickListener.onCallClick(chatItem);
            }
        });
    }



    @Override
    public int getItemCount() {
        return chatList.size();
    }

    static class ChatViewHolder extends RecyclerView.ViewHolder {
        ImageView chatImage;
        TextView chatName, chatMessage;
        Button btnMessage;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            chatImage = itemView.findViewById(R.id.imageChat);
            chatName = itemView.findViewById(R.id.chatName);
            chatMessage = itemView.findViewById(R.id.chatMessage);
            btnMessage = itemView.findViewById(R.id.btnMessage);
        }
    }
}
