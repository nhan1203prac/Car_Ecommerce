package com.example.cartandorder.home;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cartandorder.R;
import com.example.cartandorder.detail.DetailActivity;
import com.example.cartandorder.favourite.Car;

import java.util.ArrayList;
import java.util.List;
public class CarSaleAdapter extends RecyclerView.Adapter<CarSaleAdapter.CarViewHolder> {

    private List<Car> originalCarList;
    private List<Car> filteredCarList;
    private Context context;

    public CarSaleAdapter(List<Car> carList, Context context) {
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
        Car car = filteredCarList.get(position);
        holder.tvName.setText(car.getName());
        holder.tvPrice.setText(car.getPrice());



        holder.showMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                context.startActivity(intent);
            }
        });

        holder.iconView.setOnClickListener(new View.OnClickListener() {
            boolean isToggled = false;

            @Override
            public void onClick(View v) {
                if (isToggled) {

                    holder.iconView.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.black));
                } else {

                    holder.iconView.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.red));
                }

                isToggled = !isToggled;
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
            for (Car car : originalCarList) {
                if (car.getBrand().equalsIgnoreCase(brand)) {
                    filteredCarList.add(car);
                }
            }
        }
        notifyDataSetChanged();
    }

    static class CarViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice;
        Button showMoreButton, iconView;
        public CarViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.car_name3);
            tvPrice = itemView.findViewById(R.id.car_price3);
            iconView = itemView.findViewById(R.id.iconView);
            showMoreButton = itemView.findViewById(R.id.show_more_button3);
        }
    }
}
