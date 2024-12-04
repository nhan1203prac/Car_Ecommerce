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
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cartandorder.ApiService.CarServiceApi;
import com.example.cartandorder.MainActivity;
import com.example.cartandorder.R;

import com.example.cartandorder.ReponseApi.CarWatchlistResponse;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchBar extends AppCompatActivity {

    LinearLayout linearLayout, linearSort;
    String [] array_brands = {"Mercedes-Benz", "VinFast", "Lotus", "Mclaren", "Maserati", "Mazda"};
    List<CarWatchlistResponse> listCar = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.search_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        String token = "Bearer " + getTokenFromPrefs();
        EditText sBar = findViewById(R.id.search_bar);
        linearLayout = findViewById(R.id.linearlayout);
        addListItem();
//        remove item
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            TextView tmp = (TextView)linearLayout.getChildAt(i);
            Drawable drawable = ContextCompat.getDrawable(linearLayout.getContext(), R.drawable.n_svg_x);
            tmp.setOnTouchListener(new View.OnTouchListener() {
                @SuppressLint("ClickableViewAccessibility")
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getRawX() >= (tmp.getRight() - drawable.getBounds().width() - tmp.getPaddingEnd())) {
                        tmp.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                RemoveItem(view);
                            }
                        });
                    } else {
                        sBar.setText(tmp.getText());
                    }
                    return false;
                }
            });
        }

        ImageView imgViewBack = findViewById(R.id.imgBack);

        imgViewBack.setOnClickListener(v-> {
            Intent intent = new Intent(SearchBar.this, MainActivity.class);
            startActivity(intent);
        });

        TextView clearAll = findViewById(R.id.ClearAll);
        clearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                while(linearLayout.getChildCount() != 0) linearLayout.removeView(linearLayout.getChildAt(0));
            }
        });




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
                                for(CarWatchlistResponse car: response.body()){
                                    listCar.add(car);
                                }
                                Intent inten = new Intent(SearchBar.this, Search_Item.class);
                                inten.putParcelableArrayListExtra("carList",new ArrayList<>(response.body()));
                                inten.putExtra("nameSearch",searchText);
                                inten.putExtra("countResult",String.valueOf(response.body().size()));
                                startActivity(inten);
                            }else{
                                startActivity(new Intent(SearchBar.this, Search_Empty.class));
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



    private void RemoveItem(View view) {
        int id = view.getId();
        String resourceNameID = getResources().getResourceEntryName(id);
        linearLayout.removeView(view);
        TextView tvTMP = new TextView(this);
        tvTMP.setText("");
        linearLayout.addView(tvTMP);
    }



    private void addListItem() {
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            TextView tmp = (TextView)linearLayout.getChildAt(i);
            tmp.setText(array_brands[i]);
        }

    }
    private String getTokenFromPrefs() {
        return getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE)
                .getString("token", "");
    }



}
