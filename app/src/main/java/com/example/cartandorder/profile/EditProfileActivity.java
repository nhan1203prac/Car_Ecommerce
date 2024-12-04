package com.example.cartandorder.profile;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cartandorder.ApiService.UserServiceApi;
import com.example.cartandorder.R;
import com.example.cartandorder.ReponseApi.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {
    Button btnSubmit,btnBack,updatedUser;
    Long userId;
    EditText btn_profile_name,btn_profile_lastname,btn_email,phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        btn_profile_name = findViewById(R.id.btn_profile_name);
        btn_profile_lastname = findViewById(R.id.btn_profile_lastname);
        btn_email = findViewById(R.id.btn_email);
        phone = findViewById(R.id.phone);
        updatedUser = findViewById(R.id.updatedUser);
        String token = "Bearer " + getTokenFromPrefs();
        btnSubmit = findViewById(R.id.btn_dob);
        btnBack = findViewById(R.id.backIcon);
        SharedPreferences sharedPreferences = getSharedPreferences("AuthPrefs", MODE_PRIVATE);
        userId = sharedPreferences.getLong("userId", -1);
        btnBack.setOnClickListener(v->{
            finish();
        });
        // Khởi tạo Calendar để lấy ngày hiện tại
        final Calendar calendar = Calendar.getInstance();

        btnSubmit.setOnClickListener(v -> {
            // Lấy năm, tháng, ngày hiện tại
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            // Tạo DatePickerDialog
            DatePickerDialog datePickerDialog = new DatePickerDialog(EditProfileActivity.this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        // Định dạng ngày tháng
                        String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        // Hiển thị ngày đã chọn lên Button
                        btnSubmit.setText(selectedDate);
                        // Hiển thị thông báo
                        Toast.makeText(EditProfileActivity.this, "Ngày đã chọn: " + selectedDate, Toast.LENGTH_SHORT).show();
                    }, year, month, day);

            // Hiển thị DatePickerDialog
            datePickerDialog.show();
        });


        UserServiceApi.userServiceApi.getUserId(userId,token).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()&&response.body()!=null){
                    btn_email.setText(response.body().getEmail());
                    String [] words = response.body().getFullName().split(" ");
                    List<String> name = new ArrayList<>();
                    for(int i=0;i<words.length;i++){
                        name.add(words[i]);
                    }

                    btn_profile_name.setText(response.body().getFullName());
                    btn_profile_lastname.setText(words[words.length-1]);
                    btnSubmit.setText(12 + "/" + 03 + "/" + 2004);
                    phone.setText(response.body().getPhoneNumber());
                }else{
                    Log.e("Don't have data", "Data is null");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("API Error", "Request failed: " + t.getMessage());
            }
        });
        updatedUser.setOnClickListener(v->{
            User user = new User();
            user.setFullName(btn_profile_name.getText().toString());
            user.setEmail(btn_email.getText().toString());
            user.setPhoneNumber(phone.getText().toString());

            UserServiceApi.userServiceApi.updateUser(token,user).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if(response.isSuccessful()&& response.body()!=null){
                        Toast.makeText(EditProfileActivity.this,"Updated success",Toast.LENGTH_SHORT);
                    }else{
                        Toast.makeText(EditProfileActivity.this,"Updated failure",Toast.LENGTH_SHORT);
                    }

                    finish();
                }


                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Log.e("API Error", "Request failed: " + t.getMessage());
                }
            });
        });
    }

    private String getTokenFromPrefs() {
        return getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE)
                .getString("token", "");
    }
}
