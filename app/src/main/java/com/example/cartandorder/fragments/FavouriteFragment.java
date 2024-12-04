package com.example.cartandorder.fragments;

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
import android.widget.Button;

import com.example.cartandorder.ApiService.CallServiceApi;
import com.example.cartandorder.ApiService.WatchlistServiceApi;
import com.example.cartandorder.R;
import com.example.cartandorder.ReponseApi.CarResponse;
import com.example.cartandorder.ReponseApi.CarWatchlistResponse;
import com.example.cartandorder.ReponseApi.User;
import com.example.cartandorder.ReponseApi.WatchlistResponse;
import com.example.cartandorder.RequestApi.CallRequest;
import com.example.cartandorder.contact.ChatActivity;
import com.example.cartandorder.contact.chatAndCall.CallItem;
import com.example.cartandorder.favourite.Car;
import com.example.cartandorder.favourite.CarAdapter;
import com.example.cartandorder.searchBar.SearchBar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavouriteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavouriteFragment extends Fragment {
    private RecyclerView recyclerView;
    private CarAdapter carAdapter;
    private List<CarWatchlistResponse> carList;
    Button btnCall, btnSend;
    String numberPhone = "0867867652";
    Button btnSearch;
    String token;
    boolean isFetch = false;
    Long userId;
//    private CarWatchlistResponse carWatchlistResponse;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FavouriteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FavouriteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FavouriteFragment newInstance(String param1, String param2) {
        FavouriteFragment fragment = new FavouriteFragment();
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
        View view =  inflater.inflate(R.layout.fragment_favourite, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
         btnSearch = view.findViewById(R.id.search);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("AuthPrefs", requireActivity().MODE_PRIVATE);
        userId = sharedPreferences.getLong("userId", -1);
        token = "Bearer " + getTokenFromPrefs();
        carList = new ArrayList<>();
//        carList.add(new Car("Mercedes Benz EQE", "Gray", "$171,250","Some desc","Mercedes"));
//        carList.add(new Car("Tesla Model S", "Black", "$200,000","Some desc","Tesla"));
//        carList.add(new Car("BMW i8", "Gray", "$140,000","Some desc","BMW"));
//        carList.add(new Car("BMW i8", "Black", "$140,000","Some desc","BMW"));
        carAdapter = new CarAdapter(token,requireContext(),carList, new CarAdapter.OnCarClickListener() {
            @Override
            public void onCallClick(CarWatchlistResponse car) {
                Intent intentCall = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+car.getUser().getPhoneNumber()));
                if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(requireActivity(),new String[]{Manifest.permission.CALL_PHONE},1);
                    return;
                }
                fetchApiCall(userId,car.getUser().getUserId());
                startActivity(intentCall);
            }

            @Override
            public void onSendClick(CarWatchlistResponse car) {
                Intent intentSend = new Intent(requireContext(), ChatActivity.class);
                intentSend.putExtra("userId2",car.getUser().getUserId());
                startActivity(intentSend);
            }


        });

        callApi(token);
//        WatchlistServiceApi.watchlistServiceApi.getList(token).enqueue(new Callback<WatchlistResponse>() {
//            @Override
//            public void onResponse(Call<WatchlistResponse> call, Response<WatchlistResponse> response) {
//                if(response.body()!=null && response.isSuccessful()){
//                    String[] colors = {"gray", "black","blue","white", "green"  };
//                    int colorIndex = 0;
//                    for(CarWatchlistResponse car : response.body().getCars()){
//                        String color = colors[colorIndex % colors.length];
//                        car.setColor(color);
//                        carList.add(car);
//                        colorIndex++;
//                    }
//
////                    carWatchlistResponse = response.body().getCars();
//                    carAdapter.notifyDataSetChanged();
//
//                }else{
//                    Log.e("API Error", "Response error: " + response.message());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<WatchlistResponse> call, Throwable t) {
//                Log.e("API Error", "Request failed: " + t.getMessage());
//            }
//        });

        recyclerView.setAdapter(carAdapter);


        btnSearch.setOnClickListener(v->{
            startActivity(new Intent(getActivity(), SearchBar.class));
        });

         return view;
    }


    private String getTokenFromPrefs() {
        return requireActivity().getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE)
                .getString("token", "");
    }

    private void callApi(String token){
        WatchlistServiceApi.watchlistServiceApi.getList(token).enqueue(new Callback<WatchlistResponse>() {
            @Override
            public void onResponse(Call<WatchlistResponse> call, Response<WatchlistResponse> response) {
                if(response.body()!=null && response.isSuccessful()){
                    carList.clear();
                    String[] colors = {"gray", "black","blue","white", "green"  };
                    int colorIndex = 0;
                    for(CarWatchlistResponse car : response.body().getCars()){
                        String color = colors[colorIndex % colors.length];
                        car.setColor(color);
                        carList.add(car);
                        colorIndex++;
                    }
                    isFetch = true;

//                    carWatchlistResponse = response.body().getCars();
                    carAdapter.notifyDataSetChanged();

                }else{
                    Log.e("API Error", "Response error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<WatchlistResponse> call, Throwable t) {
                Log.e("API Error", "Request failed: " + t.getMessage());
            }
        });
    }

    private void fetchApiCall(Long senderId, Long receiverId){
        CallRequest callRequest = new CallRequest(senderId,receiverId);
        CallServiceApi.CallServiceApi.createCall(callRequest).enqueue(new Callback<CallItem>() {
            @Override
            public void onResponse(Call<CallItem> call, Response<CallItem> response) {
                if(response.isSuccessful() && response.body()!=null){
                    Log.e("Call success", "Perform call");
                }
                else{
                    Log.e("API Error", "Response error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<CallItem> call, Throwable t) {
                Log.e("API Error", "Request failed: " + t.getMessage());
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();

        if(isFetch){
            callApi(token);
        }
    }

}