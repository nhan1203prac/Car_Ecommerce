package com.example.cartandorder.favourite;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cartandorder.ApiService.WatchlistServiceApi;
import com.example.cartandorder.R;
import com.example.cartandorder.ReponseApi.CarWatchlistResponse;
import com.example.cartandorder.ReponseApi.User;
import com.example.cartandorder.ReponseApi.WatchlistResponse;
import com.example.cartandorder.detail.DetailActivity;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.graphics.Color;
import android.content.res.ColorStateList;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.CarViewHolder> {
    private List<CarWatchlistResponse> carList;
    private CarWatchlistResponse carWatchlistResponse;
    private OnCarClickListener onCarClickListener;
    private Context context;
    private String token;
    public interface OnCarClickListener {
        void onCallClick(CarWatchlistResponse car);
        void onSendClick(CarWatchlistResponse car);
    }

    public CarAdapter(String token,Context context ,List<CarWatchlistResponse> carList,OnCarClickListener listener) {
        this.token = token;
        this.context = context;
        this.carList = carList;

        this.onCarClickListener = listener;
    }

    @NonNull
    @Override
    public CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_car, parent, false);
        return new CarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarViewHolder holder, int position) {
        CarWatchlistResponse car = carList.get(position);
        holder.carName.setText(car.getName());
        holder.textColor.setText(car.getColor());

        int colorResId = context.getResources().getIdentifier(car.getColor(), "color", context.getPackageName());

        if (colorResId != 0) {
            holder.carColor.setBackgroundTintList(ContextCompat.getColorStateList(context, colorResId));
        } else {
            Log.e("HomeAdapter", "Color not found in resources: " + car.getColor());
        }
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        String formattedAmount = decimalFormat.format(car.getPrice());
        holder.carPrice.setText(formattedAmount + " VND");
        holder.iconHeart.setOnClickListener(new View.OnClickListener() {
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
                                    holder.iconHeart.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.red));
                                } else {
                                    holder.iconHeart.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.black));
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
        holder.btnCall.setOnClickListener(v -> {
            if (onCarClickListener != null) {
                onCarClickListener.onCallClick(car);
            }
        });


        holder.btnSend.setOnClickListener(v -> {
            if (onCarClickListener != null) {
                onCarClickListener.onSendClick(car);
            }
        });
        holder.carImage.setOnClickListener(v->{
            Intent intent = new Intent(holder.itemView.getContext(), DetailActivity.class);
            intent.putExtra("carId",car.getCarId());
            intent.putExtra("color",car.getColor());
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return carList.size();
    }

    static class CarViewHolder extends RecyclerView.ViewHolder {
        ImageView carImage;
        TextView carName,textColor,  carPrice;
        Button carColor,btnCall,btnSend,iconHeart;

        public CarViewHolder(@NonNull View itemView) {
            super(itemView);

            carName = itemView.findViewById(R.id.carName);
            textColor = itemView.findViewById(R.id.textColor);
            carColor = (Button) itemView.findViewById(R.id.carColor);
            carPrice = itemView.findViewById(R.id.carPrice);
            btnCall = itemView.findViewById(R.id.btnCall);
            btnSend = itemView.findViewById(R.id.btnSend);
            carImage = itemView.findViewById(R.id.carImage);
            iconHeart = itemView.findViewById(R.id.iconHeart);
        }
    }
}
