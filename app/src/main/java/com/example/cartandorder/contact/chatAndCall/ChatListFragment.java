package com.example.cartandorder.contact.chatAndCall;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cartandorder.ApiService.UserServiceApi;
import com.example.cartandorder.R;
import com.example.cartandorder.ReponseApi.User;
import com.example.cartandorder.contact.ChatActivity;
import com.example.cartandorder.contact.Conversation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatListFragment extends Fragment {

    private RecyclerView recyclerViewChat;
    private ChatListAdapter chatAdapter;
    private List<ChatItem> chatList;
    String imageUrl = "https://static-00.iconduck.com/assets.00/mercedes-benz-alt-icon-2048x2048-ps1d95md.png";
    Long userId;
    String token;
    String conversationId;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ChatListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatListFragment newInstance(String param1, String param2) {
        ChatListFragment fragment = new ChatListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_chat_list, container, false);
        recyclerViewChat = view.findViewById(R.id.recyclerViewChat);
        token = "Bearer " + getTokenFromPrefs();
        recyclerViewChat.setLayoutManager(new LinearLayoutManager(requireContext()));
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("AuthPrefs", requireActivity().MODE_PRIVATE);
        userId = sharedPreferences.getLong("userId", -1);
        chatList = new ArrayList<>();


        chatAdapter = new ChatListAdapter(chatList, new ChatListAdapter.OnChatClickListener() {
            @Override
            public void onCallClick(ChatItem chartItem) {
                Intent intentSend = new Intent(requireActivity(), ChatActivity.class);
                intentSend.putExtra("userId2",chartItem.getUserId2());
//                Log.e("otherpaticipant", otherParticipant.toString());
//                intentSend.putExtra("conversationId", conversationId);
                startActivity(intentSend);
            }
        });
        recyclerViewChat.setAdapter(chatAdapter);
        fetchConversation();

        return view;

    }


    private void fetchConversation(){
        DatabaseReference conversationRef = FirebaseDatabase.getInstance().getReference("conversations");
        String currentUserId = String.valueOf(userId);
        conversationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatList.clear();
                for(DataSnapshot conversationSnapshot : snapshot.getChildren()){
                    conversationId = conversationSnapshot.getKey();
                    List<Long> participants = new ArrayList<>();

                    for (DataSnapshot participantSnapshot : conversationSnapshot.child("participants").getChildren()){
                        Object participantValue = participantSnapshot.getValue();
//                        Log.e("datasnap", participantValue.toString());
//                        if (participantValue instanceof Long) {
//                            participants.add((Long) participantValue);
//                        } else if (participantValue instanceof String) {
//                            try {
//
//                                participants.add(Long.parseLong((String) participantValue));
//                            } catch (NumberFormatException e) {
//                                Log.e("ChatListFragment", "Failed to parse participant as Long", e);
//                            }
//                        }
                        participants.add((Long) participantValue);
                    }
//                    Log.e("userId", userId.toString());
//                    for (Long num:participants){
//                        Log.e("Paticipant",num.toString());
//                    }

                    if(participants.contains(userId)){
                        fetchLastMessage(conversationId, participants);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ChatListFragment", "Failed to fetch conversations", error.toException());
            }
        });


    }

    private void fetchLastMessage(String conversationId, List<Long> participants){
//        Log.e("inside fetchLastMaesgge", conversationId);
        DatabaseReference messageRef = FirebaseDatabase.getInstance().getReference("messages");
        DatabaseReference conversationRef = messageRef.child(conversationId);
        Query lastMessageQuery = conversationRef.orderByChild("updatedAt").limitToLast(1);
        lastMessageQuery.addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                Log.e("ínideEnvent", "insdie lastMessageQuery");
//                Log.e("DataSnapshot", "Data received: " + snapshot.getChildrenCount());
                for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                    String lastMessage = messageSnapshot.child("content").getValue(String.class);
//                    Log.e("FetchLastmessage", "lastMaessage");
//                    String updatedAt = messageSnapshot.child("updatedAt").getValue(String.class);


                    Long otherParticipant = getOtherParticipant(participants, userId);
//                    Log.e("otherParticipant", otherParticipant.toString());
//                    Log.e("token", token.toString());
                    UserServiceApi.userServiceApi.getUserId(otherParticipant,token).enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if(response.isSuccessful()&& response.body()!=null){

                                ChatItem chatItem = new ChatItem(response.body().getFullName(), lastMessage, response.body().getAvatar(),otherParticipant);
                                chatList.add(chatItem);
//                                Log.e("chatItem", response.body().getEmail());
                                chatAdapter.notifyDataSetChanged();
                            }else{
                                Log.e("Don't have data", "Data is null");
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Log.e("Call api failure",t.getMessage());
                        }
                    });


                }
                chatAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ChatListFragment", "Failed to fetch last message", error.toException());
            }
        });
    }
    private Long getOtherParticipant(List<Long> participants, Long currentUserId) {
        for (Long participant : participants) {
            if (!participant.equals(currentUserId)) {
                return participant;
            }
        }

        return 0L;
    }


    private String getTokenFromPrefs() {
        return requireActivity().getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE)
                .getString("token", "");
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!chatList.isEmpty()){
            fetchConversation();
        }
    }
}