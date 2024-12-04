package com.example.cartandorder.searchBar;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cartandorder.ApiService.WatchlistServiceApi;
import com.example.cartandorder.R;
import com.example.cartandorder.ReponseApi.CarWatchlistResponse;
import com.example.cartandorder.ReponseApi.WatchlistResponse;
import com.example.cartandorder.detail.DetailActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.CarViewHolder> {

    private List<CarWatchlistResponse> originalCarList;
    private List<CarWatchlistResponse> filteredCarList;
    private Context context;
    private Long carId;
    private String token;
    public OfferAdapter(List<CarWatchlistResponse> carList, Context context) {
        this.originalCarList = carList;
        this.filteredCarList = new ArrayList<>(carList);
        this.context = context;
    }

    @NonNull
    @Override
    public CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new CarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarViewHolder holder, int position) {
        CarWatchlistResponse car = filteredCarList.get(position);
        token = "Bearer " + getTokenFromPrefs();
        Log.e("----token",token);
        String priceString = car.getPrice()+" VND";
        double price = 0.0;


        if (priceString != null && !priceString.isEmpty()) {
            try {

                String numericPrice = priceString.replaceAll("[^0-9.E]", "");
                price = Double.parseDouble(numericPrice);
            } catch (NumberFormatException e) {
                price = 0.0;
            }
        }


        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        String formattedAmount = decimalFormat.format(price);


        holder.carName.setText(car.getName());
        holder.carPrice.setText(formattedAmount + " VND");

        carId = car.getCarId();
        Glide.with(holder.itemView.getContext())
                .load(car.getImages().get(0).getUrl())
                .into(holder.imageView);

        holder.showMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("carId",car.getCarId());
                intent.putExtra("color",car.getColor());
                context.startActivity(intent);
            }
        });


        WatchlistServiceApi.watchlistServiceApi.getList(token).enqueue(new Callback<WatchlistResponse>() {
            @Override
            public void onResponse(Call<WatchlistResponse> call, Response<WatchlistResponse> response) {
                if (response.body() != null && response.isSuccessful()) {
                    boolean isCarInWatchlist = false;

                    // Duyệt qua danh sách cars trong response
                    for (CarWatchlistResponse watchlistCar : response.body().getCars()) {
                        if (watchlistCar.getCarId()==car.getCarId()) {
                            isCarInWatchlist = true;
                            break;
                        }
                    }

                    if (isCarInWatchlist) {
                        Log.e("inside", response.body().getUser().getEmail());
                        holder.iconView.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.red));
                    } else {
                        holder.iconView.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.black));
                    }
                } else {
                    Log.e("Data null", "Response data is null");
                }

            }

            @Override
            public void onFailure(Call<WatchlistResponse> call, Throwable t) {
                Log.e("call api failure",t.getMessage());
            }
        });

//        fetchApi(car,holder);
        holder.iconView.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                WatchlistServiceApi.watchlistServiceApi.addCarToList(token,car.getCarId()).enqueue(new Callback<WatchlistResponse>() {
                    @Override
                    public void onResponse(Call<WatchlistResponse> call, Response<WatchlistResponse> response) {
                        if(response.isSuccessful()&&response.body()!=null){
                            if (response.body() != null && response.isSuccessful()) {
                                boolean isCarInWatchlist = false;


                                for (CarWatchlistResponse watchlistCar : response.body().getCars()) {
                                    if (watchlistCar.getCarId()==car.getCarId()) {
                                        isCarInWatchlist = true;
                                        break;
                                    }
                                }

                                if (isCarInWatchlist) {
                                    Log.e("inside", response.body().getUser().getEmail());
                                    holder.iconView.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.red));
                                } else {
                                    holder.iconView.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.black));
                                }
                            } else {
                                Log.e("Data null", "Response data is null");
                            }


                        }else {
                            Toast.makeText(context,"Không có sản phẩm",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<WatchlistResponse> call, Throwable t) {
                        Log.e("API_FAILURE", t.getMessage());
                        Toast.makeText(context, "Lỗi kết nối tới server", Toast.LENGTH_SHORT).show();
                    }
                });






            }
        });

    }

    @Override
    public int getItemCount() {
        return filteredCarList.size();
    }

    public void filterByBrand(String brand) {
        filteredCarList.clear();
        if (brand.equals("All")) {
            filteredCarList.addAll(originalCarList);
        } else {
            for (CarWatchlistResponse car : originalCarList) {
                if (car.getBrand().equalsIgnoreCase(brand)) {
                    filteredCarList.add(car);
                }
            }
        }
        notifyDataSetChanged();
    }

    static class CarViewHolder extends RecyclerView.ViewHolder {
        TextView carName, carPrice;
        Button showMoreButton, iconView;
        ImageView imageView;
        public CarViewHolder(@NonNull View itemView) {
            super(itemView);
            carName = itemView.findViewById(R.id.car_name3);
            carPrice = itemView.findViewById(R.id.car_price3);
            showMoreButton = itemView.findViewById(R.id.show_more_button3);
            iconView = itemView.findViewById(R.id.iconView);
            imageView = itemView.findViewById(R.id.car_image3);
        }
    }
    private String getTokenFromPrefs() {
        return context.getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE)
                .getString("token", "");
    }
}
