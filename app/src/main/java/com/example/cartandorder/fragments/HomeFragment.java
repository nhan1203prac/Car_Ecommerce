package com.example.cartandorder.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.cartandorder.ApiService.CarServiceApi;
import com.example.cartandorder.ReponseApi.CarCreateResponse;
import com.example.cartandorder.ReponseApi.CarResponse;
import com.example.cartandorder.ReponseApi.CarWatchlistResponse;
import com.example.cartandorder.detail.DetailActivity;
import com.example.cartandorder.R;
import com.example.cartandorder.favourite.Car;
import com.example.cartandorder.favourite.CarAdapter;
import com.example.cartandorder.home.HomeAdapter;
import com.example.cartandorder.home.Home_SpecialOffers;
import com.example.cartandorder.home.TopSaleActivity;
import com.example.cartandorder.searchBar.SearchBar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private HomeAdapter carAdapter;
    private List<CarResponse> carList;
    private TextView see_all;
    private TextView seeAll;
    private Button allButton, mercedesButton, teslaButton, acuraButton, bmwButton;
    private boolean isFetch=false;
    private String token;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireActivity(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        carList = new ArrayList<>();
//        carList.add(new Car("Mercedes Benz EQE", "Gray", "$171,250","Some desc","Mercedes"));
//        carList.add(new Car("Tesla Model S", "Black", "$200,000","Some desc","Tesla"));
//        carList.add(new Car("BMW i8", "Gray", "$140,000","Some desc","BMW"));
//        carList.add(new Car("BMW i8", "Black", "$140,000","Some desc","BMW"));
//        carList.add(new Car("Tesla Model S", "Black", "$200","Some desc","Tesla"));
//                carList.add(new Car("Mercedes Benz EQE", "Gray", "$171,250","Some desc","Mercedes"));

        carAdapter = new HomeAdapter(requireContext(), carList);
        recyclerView.setAdapter(carAdapter);


        allButton = view.findViewById(R.id.all);
        mercedesButton = view.findViewById(R.id.mercedes);
        teslaButton = view.findViewById(R.id.Tesla);
        acuraButton = view.findViewById(R.id.Acura);
        bmwButton = view.findViewById(R.id.BMW);

        token = "Bearer " + getTokenFromPrefs();
        displayAllCar(token,null);
        allButton.setOnClickListener(v -> {
            updateButtonStyle(allButton);
            displayAllCar(token,null);
        });
        mercedesButton.setOnClickListener(v -> {
            updateButtonStyle(mercedesButton);
            displayAllCar(token,mercedesButton.getText().toString());
        });
        teslaButton.setOnClickListener(v -> {
            updateButtonStyle(teslaButton);
            displayAllCar(token,teslaButton.getText().toString());
        });
        acuraButton.setOnClickListener(v -> {
            updateButtonStyle(acuraButton);
            displayAllCar(token,acuraButton.getText().toString());
        });
        bmwButton.setOnClickListener(v -> {
            updateButtonStyle(bmwButton);
            displayAllCar(token,bmwButton.getText().toString());
        });

        EditText sBar = view.findViewById(R.id.search_bar);
        sBar.setInputType(InputType.TYPE_NULL);
        sBar.setFocusable(false);
        sBar.setCursorVisible(false);
        sBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), SearchBar.class));
            }
        });

        seeAll = view.findViewById(R.id.seeall);
        seeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Home_SpecialOffers.class));
            }
        });

         see_all = view.findViewById(R.id.see_all);
        see_all.setOnClickListener(v->{
            Intent intent = new Intent(getActivity(), TopSaleActivity.class);
            startActivity(intent);
        });
        return view;
    }

    private void updateButtonStyle(Button selectedBtn) {
        resetAllButtonStyles();



        selectedBtn.setBackgroundTintList(ContextCompat.getColorStateList(requireActivity(), R.color.black));
        selectedBtn.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white));
    }

    private void resetAllButtonStyles() {
        resetButtonStyle(allButton);
        resetButtonStyle(mercedesButton);
        resetButtonStyle(teslaButton);
        resetButtonStyle(acuraButton);
        resetButtonStyle(bmwButton);
    }



    private void resetButtonStyle(Button button) {

        button.setBackgroundTintList(ContextCompat.getColorStateList(requireActivity(), R.color.white));
        button.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black));
    }
    private String getTokenFromPrefs() {
        return requireActivity().getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE)
                .getString("token", "");
    }

    private void displayAllCar(String token,String brand) {

        String[] colors = {"gray", "black","blue","white", "green"  };

        CarServiceApi.carServiceApi.getListCarBrand(token,brand).enqueue(new Callback<List<CarWatchlistResponse>>() {
            @Override
            public void onResponse(Call<List<CarWatchlistResponse>> call, Response<List<CarWatchlistResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {

                    carList.clear();


                    int colorIndex = 0;

                    for (CarWatchlistResponse carResponse : response.body()) {

                        String color = colors[colorIndex % colors.length];
                        Log.e("---------API", "carId: " + carResponse.getBrand());
                        Log.e("---------API", "carId: " + carResponse.getCarId());

                        carList.add(new CarResponse(
                                carResponse.getBrand(),
                                carResponse.getName(),
                                carResponse.getDescription(),
                                carResponse.getPrice() ,
                                carResponse.getImages(),
                                carResponse.getCarId(),
                                carResponse.getComments(),
                                carResponse.getUser(),
                                color
                        ));
                        isFetch = true;

                        colorIndex++;
                    }


                    carAdapter.notifyDataSetChanged();
                } else {

                    Log.e("API Error", "Response error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<CarWatchlistResponse>> call, Throwable t) {
                Log.e("API Error", "Request failed: " + t.getMessage());
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        if(isFetch){
            displayAllCar(token,null);
        }

    }




}