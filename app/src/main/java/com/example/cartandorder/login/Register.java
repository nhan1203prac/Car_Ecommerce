package com.example.cartandorder.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cartandorder.ApiService.AuthServiceApi;
import com.example.cartandorder.R;
import com.example.cartandorder.ReponseApi.LoginResponse;
import com.example.cartandorder.ReponseApi.User;
import com.example.cartandorder.RequestApi.RegisterRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register extends AppCompatActivity {
    EditText fullname, password, phone,email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.register_main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        fullname = findViewById(R.id.fullName);
        password = findViewById(R.id.pass);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        TextView tviewRegister = findViewById(R.id.tvSignIn);
        tviewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Register.this, SignInByPass.class));
            }
        });

        ImageView imgViewBack = findViewById(R.id.imgBack);
        imgViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Register.this, Welcome.class));
            }
        });

        Button btnSignUp = findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = fullname.getText().toString().trim();
                String pass = password.getText().toString().trim();
                String phoneNumber = phone.getText().toString().trim();
                String emailAddress = email.getText().toString().trim();


                if (name.isEmpty() || pass.isEmpty() || phoneNumber.isEmpty() || emailAddress.isEmpty()) {
                    Toast.makeText(Register.this, "All fields must be filled", Toast.LENGTH_SHORT).show();
                    return;
                }

                RegisterRequest registerRequest = new RegisterRequest(name,emailAddress,phoneNumber,pass);
                AuthServiceApi.authServiceApi.register(registerRequest).enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful()) {

                            Toast.makeText(getApplicationContext(), "Register success. Please login.", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(Register.this, SignInByPass.class)); // Chuyển hướng đến màn hình đăng nhập
                        } else {

                            Toast.makeText(Register.this, "Register failed. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Toast.makeText(Register.this, "Network error. Please try again.", Toast.LENGTH_SHORT).show();
                        Log.e("RegisterError", t.getMessage());
                    }
                });

            }
        });
    }
}
