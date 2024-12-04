package com.example.cartandorder.notification;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cartandorder.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class NotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notification);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button viewDetailButton1 = findViewById(R.id.view_detail_button1);
        Button viewDetailButton2 = findViewById(R.id.view_detail_button2);
        Button viewDetailButton3 = findViewById(R.id.view_detail_button3);

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Gọi phương thức hiển thị BottomSheet
                showBottomSheet();
            }
        };

        viewDetailButton1.setOnClickListener(clickListener);
        viewDetailButton2.setOnClickListener(clickListener);
        viewDetailButton3.setOnClickListener(clickListener);



    }
    private void showBottomSheet() {
        // Tạo BottomSheetDialog
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(NotificationActivity.this);

        // Gán layout cho BottomSheet
        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_layout, null);
        bottomSheetDialog.setContentView(bottomSheetView);

        // Hiển thị BottomSheet
        bottomSheetDialog.show();
    }
}