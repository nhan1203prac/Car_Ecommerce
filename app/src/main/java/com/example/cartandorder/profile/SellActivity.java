package com.example.cartandorder.profile;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cartandorder.ApiService.CarServiceApi;
import com.example.cartandorder.R;
import com.example.cartandorder.ReponseApi.CarCreateResponse;
import com.example.cartandorder.RequestApi.CarCreateRequest;
import com.example.cartandorder.fragments.ProfileFragment;
import com.example.cartandorder.login.Register;
import com.example.cartandorder.profile.adapter_list_img.item_imgCarPostAdapter;
import com.example.cartandorder.searchBar.SearchBar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Url;

public class SellActivity extends AppCompatActivity {
    private item_imgCarPostAdapter imageSliderAdapter;
    private ArrayList<Uri> images;
    private RecyclerView recyclerView;
    private TextView txtPost;
    private Button btnBack;
    private Button Btn_Confirm;
    private EditText pTxt_Company,pTxt_nameCar,mTxt_Description,editTextNumber;
    private static final int PICK_IMG = 1;
    private static final int READ_PERMISSION = 101;
    private FirebaseApp secondaryApp;
    private FirebaseStorage secondaryStorage;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        String token = "Bearer " + getTokenFromPrefs();

        if (FirebaseApp.getApps(this).stream().noneMatch(app -> app.getName().equals("secondary"))) {
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setApiKey("AIzaSyCumQLhMs453tofSAtu3nvn5rpiSM574AI")
                    .setApplicationId("1:913193606005:android:4193572074919e3d45113c")
                    .setStorageBucket("netflix-c6275.appspot.com")
                    .build();

            secondaryApp = FirebaseApp.initializeApp(this, options, "secondary");
            secondaryStorage = FirebaseStorage.getInstance(secondaryApp);
        } else {
            secondaryApp = FirebaseApp.getInstance("secondary");
            secondaryStorage = FirebaseStorage.getInstance(secondaryApp);
        }




        images = new ArrayList<>();
        setContentView(R.layout.fragment_sell_car);
        pTxt_Company = findViewById(R.id.pTxt_Company);
        pTxt_nameCar = findViewById(R.id.pTxt_nameCar);
        mTxt_Description = findViewById(R.id.mTxt_Description);
        editTextNumber = findViewById(R.id.editTextNumber);

        recyclerView = findViewById(R.id.rcy_item_img);
        imageSliderAdapter = new item_imgCarPostAdapter(images);
        recyclerView.setLayoutManager(new LinearLayoutManager(SellActivity.this, RecyclerView.HORIZONTAL, false));
        recyclerView.setAdapter(imageSliderAdapter);

        txtPost = findViewById(R.id.txt_postImg);
        txtPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(SellActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 999);
                }
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                }
                //intent.setAction (Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMG);
            }
        });

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v-> {
            finish();
        });

        Btn_Confirm = findViewById(R.id.Btn_Confirm);
        Btn_Confirm.setOnClickListener(v->{


            String brand = pTxt_Company.getText().toString();
            String name = pTxt_nameCar.getText().toString();
            String desc = mTxt_Description.getText().toString();
            double price = Double.parseDouble(editTextNumber.getText().toString());
            List<String> stringImage = new ArrayList<>();
            for(Uri uri : images){
                stringImage.add(uri.toString());
            }
            CarCreateRequest carCreateRequest = new CarCreateRequest(brand,name,desc,price,stringImage);
            CarServiceApi.carServiceApi.create(token,carCreateRequest).enqueue(new Callback<CarCreateResponse>() {
                @Override
                public void onResponse(Call<CarCreateResponse> call, Response<CarCreateResponse> response) {
                    if(response.isSuccessful() && response.body()!=null){
                        Toast.makeText(getApplicationContext(), "Create success",Toast.LENGTH_LONG).show();
                    }else{
                        Log.e("API Error", response.errorBody().toString());
                        Toast.makeText(SellActivity.this, "Create failure",Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<CarCreateResponse> call, Throwable t) {
                    Toast.makeText(SellActivity.this, "Network error. Please try again.", Toast.LENGTH_SHORT).show();
                    Log.e("CreateError", t.getMessage());
                }
            });
        });

    }

    private String getTokenFromPrefs() {
        return getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE)
                .getString("token", "");
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMG && resultCode == RESULT_OK && data != null) {
            if (data.getClipData() != null) {
                int countOfImage = data.getClipData().getItemCount();
                for (int i = 0; i < countOfImage; i++) {
                    Uri imgUri = data.getClipData().getItemAt(i).getUri();
                    uploadImageToFirebase(imgUri);
                }
                imageSliderAdapter.notifyDataSetChanged();
            } else {
                Uri imgUri = data.getData();
                uploadImageToFirebase(imgUri);
                imageSliderAdapter.notifyDataSetChanged();
            }
        } else {
            Toast.makeText(this,"nothing", Toast.LENGTH_LONG).show();
        }
    }

    private void uploadImageToFirebase(Uri imageUri) {

        StorageReference storageRef = secondaryStorage.getReference();
        StorageReference imageRef = storageRef.child("car_images/" + System.currentTimeMillis() + ".jpg");


        imageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {

                    imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();
                        images.add(uri);
                        imageSliderAdapter.notifyDataSetChanged();
                        Toast.makeText(SellActivity.this, "Image uploaded successfully! URL: " + imageUrl, Toast.LENGTH_LONG).show();
                    }).addOnFailureListener(e -> {
                        Toast.makeText(SellActivity.this, "Failed to get image URL", Toast.LENGTH_SHORT).show();
                    });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(SellActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                });
    }
}