package com.example.cartandorder.contact.chatAndCall;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cartandorder.ApiService.CallServiceApi;
import com.example.cartandorder.R;
import com.example.cartandorder.ReponseApi.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CallListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CallListFragment extends Fragment {
    private RecyclerView recyclerViewChat;
    private CallListAdapter callListAdapter;
    private List<CallItem> callList;
    private String token;
    private Long userId;
//    String imageUrl = "https://static-00.iconduck.com/assets.00/mercedes-benz-alt-icon-2048x2048-ps1d95md.png";
    private  boolean isFetch = false;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CallListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CallListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CallListFragment newInstance(String param1, String param2) {
        CallListFragment fragment = new CallListFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_call_list, container, false);

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("AuthPrefs", requireActivity().MODE_PRIVATE);
        userId = sharedPreferences.getLong("userId", -1);
        recyclerViewChat = view.findViewById(R.id.recyclerViewChat);
        recyclerViewChat.setLayoutManager(new LinearLayoutManager(requireContext()));
        token = "Bearer " + getTokenFromPrefs();
        callList = new ArrayList<>();
//        CallItem call1 = new CallItem("John", LocalDateTime.now(), true, "123456789", imageUrl);
//        CallItem call2 = new CallItem("Jane", LocalDateTime.now().minusHours(2), false, "987654321", imageUrl);
//        CallItem call3 = new CallItem("Alex", LocalDateTime.now().minusDays(1), true, "456789123",imageUrl );
//        CallItem call4 = new CallItem("nhan", LocalDateTime.now().minusDays(5), true, "456789123",imageUrl );
//        CallItem call5 = new CallItem("nguyen", LocalDateTime.now().minusDays(3), true, "456789123",imageUrl );
//        CallItem call6 = new CallItem("nghia", LocalDateTime.now().minusDays(3), false, "456789123",imageUrl );



        callListAdapter = new CallListAdapter(userId,callList, new CallListAdapter.OnChatClickListener() {
            @Override
            public void onCallClick(CallItem callItem) {
                String phoneNumber = callItem.getSender().getUserId().equals(userId)?callItem.getReceiver().getPhoneNumber():callItem.getSender().getPhoneNumber();
                Intent intentCall = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+phoneNumber));
                if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(requireActivity(),new String[]{Manifest.permission.CALL_PHONE},1);
                    return;
                }
                startActivity(intentCall);
            }
        });
        recyclerViewChat.setAdapter(callListAdapter);
        CallApi(token);
        return view;
    }

    private void CallApi(String token){
        CallServiceApi.CallServiceApi.getUserCall(token).enqueue(new Callback<List<CallItem>>() {

            @Override
            public void onResponse(Call<List<CallItem>> call, Response<List<CallItem>> response) {
                if(response.isSuccessful()&&response.body()!=null){
                    callList.clear();
                    for(CallItem cal : response.body()){
                        CallItem callItem = new CallItem(cal.getCallId(),cal.getSender(),cal.getReceiver(),cal.getTime());
                        callList.add(callItem);
                    }
                    isFetch=true;
                    callListAdapter.notifyDataSetChanged();
                }else{
                    Log.e("Data null", "Response body is null");
                }
            }

            @Override
            public void onFailure(Call<List<CallItem>> call, Throwable t) {
                Log.e("API_FAILURE", t.getMessage());
            }
        });
    }

    private String getTokenFromPrefs() {
        return requireActivity().getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE)
                .getString("token", "");
    }

    @Override
    public void onResume() {
        super.onResume();
        if(isFetch){
            CallApi(token);
        }

    }

}