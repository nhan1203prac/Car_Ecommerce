package com.example.cartandorder.searchBar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cartandorder.ApiService.CarServiceApi;
import com.example.cartandorder.R;
import com.example.cartandorder.ReponseApi.CarResponse;
import com.example.cartandorder.ReponseApi.CarWatchlistResponse;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Search_Item extends AppCompatActivity {
    private RecyclerView recyclerView;
    private OfferAdapter carSaleAdapter;
    private List<CarWatchlistResponse> carList;
    private TextView countResult,results;
    String[] colors = {"gray", "black","blue","white", "green"  };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.search_item);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        String token = "Bearer " + getTokenFromPrefs();
        Intent intent = getIntent();
        ArrayList<CarWatchlistResponse> list = intent.getParcelableArrayListExtra("carList");

        countResult = findViewById(R.id.textView21);
        countResult.setText(intent.getStringExtra("countResult"));

        results = findViewById(R.id.results);
        results.setText(intent.getStringExtra("nameSearch"));
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        carList = new ArrayList<>();

        int colorIndex = 0;
        for(CarWatchlistResponse carResponse:list){
            String color = colors[colorIndex % colors.length];

            carList.add(new CarWatchlistResponse(
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
        }

        carSaleAdapter = new OfferAdapter(carList,this);

        recyclerView.setAdapter(carSaleAdapter);


        ImageView imgViewBack = findViewById(R.id.imgBack);
        imgViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(Search_Item.this, SearchBar.class);
                startActivity(intent1);
                finish();
            }
        });

        EditText sBar = findViewById(R.id.search_bar);

        sBar.setSingleLine(true);
        sBar.setImeOptions(EditorInfo.IME_ACTION_DONE);
        sBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE) {
                    String searchText = sBar.getText().toString().trim();
                    CarServiceApi.carServiceApi.searchCar(token,searchText).enqueue(new Callback<List<CarWatchlistResponse>>() {
                        @Override
                        public void onResponse(Call<List<CarWatchlistResponse>> call, Response<List<CarWatchlistResponse>> response) {
                            if(response.isSuccessful() && response.body()!=null){

                                Intent inten = new Intent(Search_Item.this, Search_Item.class);
                                inten.putExtra("carList",new ArrayList<>(response.body()));
                                startActivity(inten);
                            }else{
                                startActivity(new Intent(Search_Item.this, Search_Empty.class));
                            }
                        }

                        @Override
                        public void onFailure(Call<List<CarWatchlistResponse>> call, Throwable t) {

                        }
                    });

                    return true;
                }
                return false;
            }
        });
    }

    private String getTokenFromPrefs() {
        return getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE)
                .getString("token", "");
    }


}