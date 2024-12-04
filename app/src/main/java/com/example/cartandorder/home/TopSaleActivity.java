package com.example.cartandorder.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cartandorder.ApiService.CarServiceApi;
import com.example.cartandorder.R;
import com.example.cartandorder.ReponseApi.CarCreateResponse;
import com.example.cartandorder.ReponseApi.CarResponse;
import com.example.cartandorder.ReponseApi.CarWatchlistResponse;
import com.example.cartandorder.favourite.Car;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TopSaleActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private HomeAdapter carSaleAdapter;
    private List<CarResponse> carList;
    private Button btnBack, allButton, mercedesButton, teslaButton, bmwButton, acuraButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_top_sale);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        carList = new ArrayList<>();
//        carList.add(new Car("Mercedes Benz EQE", "Gray", "$171,250","Some desc","Mercedes"));
//        carList.add(new Car("Tesla Model S", "Black", "$200,000","Some desc","Tesla"));
//        carList.add(new Car("BMW i8", "Gray", "$140,000","Some desc","BMW"));
//        carList.add(new Car("BMW i8", "Black", "$140,000","Some desc","BMW"));
//        carList.add(new Car("Tesla Model S", "Black", "$200","Some desc","Tesla"));
//        carList.add(new Car("Mercedes Benz EQE", "Gray", "$171,250","Some desc","Mercedes"));


        carSaleAdapter = new HomeAdapter(this, carList);

        recyclerView.setAdapter(carSaleAdapter);

        String token = "Bearer " + getTokenFromPrefs();
        allButton = findViewById(R.id.All);
        mercedesButton = findViewById(R.id.Mercedes);
        teslaButton = findViewById(R.id.Tesla);
        bmwButton = findViewById(R.id.BMW);
        acuraButton = findViewById(R.id.Acura);

        displayAllCar(token, null);
        allButton.setOnClickListener(v -> {
            updateButtonStyle(allButton);
            displayAllCar(token, null);
        });
        mercedesButton.setOnClickListener(v -> {
            updateButtonStyle(mercedesButton);
            displayAllCar(token, mercedesButton.getText().toString());
        });
        teslaButton.setOnClickListener(v -> {
            updateButtonStyle(teslaButton);
            displayAllCar(token, teslaButton.getText().toString());
        });
        acuraButton.setOnClickListener(v -> {
            updateButtonStyle(acuraButton);
            displayAllCar(token, acuraButton.getText().toString());
        });
        bmwButton.setOnClickListener(v -> {
            updateButtonStyle(bmwButton);
            displayAllCar(token, bmwButton.getText().toString());
        });
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v->{
            finish();
        });

    }

    private void updateButtonStyle(Button selectedBtn) {
        resetAllButtonStyles();


        selectedBtn.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.black));
        selectedBtn.setTextColor(ContextCompat.getColor(this, R.color.white));
    }

    private void resetAllButtonStyles() {
        resetButtonStyle(allButton);
        resetButtonStyle(mercedesButton);
        resetButtonStyle(teslaButton);
        resetButtonStyle(acuraButton);
        resetButtonStyle(bmwButton);
    }

    private void resetButtonStyle(Button button) {

        button.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.white));
        button.setTextColor(ContextCompat.getColor(this, R.color.black));
    }

    private String getTokenFromPrefs() {
        return getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE)
                .getString("token", "");
    }

    private void displayAllCar(String token, String brand) {

        String[] colors = {"gray", "black","blue","white", "green"  };

        CarServiceApi.carServiceApi.getListCarBrand(token, brand).enqueue(new Callback<List<CarWatchlistResponse>>() {
            @Override
            public void onResponse(Call<List<CarWatchlistResponse>> call, Response<List<CarWatchlistResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {

                    carList.clear();


                    int colorIndex = 0;

                    for (CarWatchlistResponse carResponse : response.body()) {

                        String color = colors[colorIndex % colors.length];


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


                        colorIndex++;
                    }


                    carSaleAdapter.notifyDataSetChanged();
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
}