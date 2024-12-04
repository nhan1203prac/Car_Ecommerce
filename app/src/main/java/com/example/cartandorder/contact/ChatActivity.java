package com.example.cartandorder.contact;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cartandorder.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerViewChat;
    private ChatAdapter chatAdapter;
    private List<Message> messages;
    private EditText editTextMessage;
    private Button send, btnBack;
    private DatabaseReference conversationsDatabase;
    private DatabaseReference messagesDatabase;
    private String conversationId;
    private Long userId1 ;
    private Long userId2  ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        recyclerViewChat = findViewById(R.id.recyclerViewChat);
        editTextMessage = findViewById(R.id.editTextMessage);
        send = findViewById(R.id.send);
        btnBack = findViewById(R.id.btnBack);

        Intent intent = getIntent();


        userId2 = intent.getLongExtra("userId2", -1);
//        conversationId = intent.getStringExtra("conversationId");

        SharedPreferences sharedPreferences = getSharedPreferences("AuthPrefs", MODE_PRIVATE);
        userId1 = sharedPreferences.getLong("userId", -1);


        messages = new ArrayList<>();
        chatAdapter = new ChatAdapter(messages, userId1);
        recyclerViewChat.setAdapter(chatAdapter);
        recyclerViewChat.setLayoutManager(new LinearLayoutManager(this));


        conversationsDatabase = FirebaseDatabase.getInstance().getReference("conversations");
        checkAndCreateConversation();



        send.setOnClickListener(v -> {
            Log.d("ChatActivity", "Send button clicked");
            sendMessage();
        });


        btnBack.setOnClickListener(v -> finish());
    }

    private void sendMessage() {
        String content = editTextMessage.getText().toString().trim();
        if (!content.isEmpty() && conversationId != null) {
            messagesDatabase = FirebaseDatabase.getInstance().getReference("messages").child(conversationId);
            String messageId = messagesDatabase.push().getKey();
            if (messageId != null) {
                Message message = new Message(
                        messageId,
                        userId1,
                        userId2,
                        content,
                        String.valueOf(System.currentTimeMillis()),
                        String.valueOf(System.currentTimeMillis())
                );

                messagesDatabase.child(messageId).setValue(message)
                        .addOnSuccessListener(aVoid -> {
                            editTextMessage.setText("");
                            loadMessagesFromFirebase(conversationId);
                        })
                        .addOnFailureListener(e -> Log.e("Firebase", "Failed to send message", e));
            }
        }
    }

    private void loadMessagesFromFirebase(String conversationId) {
        messagesDatabase = FirebaseDatabase.getInstance().getReference("messages").child(conversationId);
        messagesDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messages.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Message message = dataSnapshot.getValue(Message.class);
                    if (message != null) {
                        messages.add(message);
                    }
                }
                chatAdapter.notifyDataSetChanged();
                recyclerViewChat.scrollToPosition(messages.size() - 1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Failed to load messages", error.toException());
            }
        });
    }

    private void checkAndCreateConversation() {
        conversationsDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean conversationExists = false;
                Set<Long> currentParticipants = new HashSet<>(Arrays.asList(userId1, userId2));

                for (DataSnapshot conversationSnapshot : snapshot.getChildren()) {
                    Conversation conversation = conversationSnapshot.getValue(Conversation.class);
                    if (conversation != null && conversation.getParticipants() != null) {
                        Set<Long> participantsInFirebase = new HashSet<>(conversation.getParticipants());

                        // So sánh danh sách người tham gia
                        if (participantsInFirebase.equals(currentParticipants)) {
                            conversationExists = true;
                            conversationId = conversation.get_id();
                            Log.e("Conversation", "Found existing conversation: " + conversationId);
                            break;
                        }
                    }
                }

                if (conversationExists) {
                    Log.d("ChatActivity", "Conversation exists: " + conversationId);
                    loadMessagesFromFirebase(conversationId);
                } else {
                    createNewConversation();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Error checking conversations", error.toException());
            }
        });
    }



    private void createNewConversation() {
        conversationId = conversationsDatabase.push().getKey();
        if (conversationId != null) {
            List<Long> participants = Arrays.asList(userId1, userId2);
            Conversation conversation = new Conversation(
                    conversationId,
                    participants,
                    String.valueOf(System.currentTimeMillis()), // Thời gian tạo
                    String.valueOf(System.currentTimeMillis())  // Thời gian cập nhật
            );

            conversationsDatabase.child(conversationId).setValue(conversation)
                    .addOnSuccessListener(aVoid -> {
                        Log.d("Firebase", "New conversation created: " + conversationId);
                        loadMessagesFromFirebase(conversationId);
                    })
                    .addOnFailureListener(e -> Log.e("Firebase", "Failed to create conversation", e));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (conversationId != null) {
            // Tải lại tin nhắn từ Firebase
            checkAndCreateConversation();

        }
    }

}
