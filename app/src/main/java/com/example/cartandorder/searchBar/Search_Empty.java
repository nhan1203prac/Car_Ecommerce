package com.example.cartandorder.searchBar;

import android.annotation.SuppressLint;
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

import com.example.cartandorder.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class Search_Empty extends AppCompatActivity {

    public String text1;
    SearchBar sb = new SearchBar();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.search_empty);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageView imgViewBack = findViewById(R.id.imgBack);
        imgViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(Search_Empty.this, SearchBar.class));
                finish();
            }
        });

//        filter
        EditText sBar = findViewById(R.id.search_bar);
        sBar.setOnTouchListener(new View.OnTouchListener() {
            Drawable[] drawables = sBar.getCompoundDrawablesRelative();
            Drawable drawable = drawables[2];
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getRawX() >= (sBar.getRight() - drawable.getBounds().width() - sBar.getPaddingEnd())) {
                    sBar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            BottomDialog();
                            sBar.setOnClickListener(null);
                        }
                    });
                }
                return false;
            }
        });
//        fill
        sBar.setSingleLine(true);
        sBar.setImeOptions(EditorInfo.IME_ACTION_DONE);
        sBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE) {
                    String searchText = sBar.getText().toString().trim();
                    if (searchText.equals("Mercedes-Benz")) {
                        startActivity(new Intent(Search_Empty.this, Search_Item.class));
                    } else {
                        startActivity(new Intent(Search_Empty.this, Search_Empty.class));
                    }
                    return true;
                }
                return false;
            }
        });

    }

    private void BottomDialog() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.search_filter);
        bottomSheetDialog.setCancelable(true);
        bottomSheetDialog.setCanceledOnTouchOutside(true);

        LinearLayout linearCondition = bottomSheetDialog.findViewById(R.id.linearcondition);
        for (int i = 0; i < linearCondition.getChildCount(); i++) {
            Button btn = (Button)linearCondition.getChildAt(i);
            btn.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ResourceType")
                @Override
                public void onClick(View view) {
                    chooseButton(btn, linearCondition);
                }
            });
        }

        LinearLayout linearSort = bottomSheetDialog.findViewById(R.id.linearsort);
        for (int i = 0; i < linearSort.getChildCount(); i++) {
            Button btn = (Button)linearSort.getChildAt(i);
            btn.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ResourceType")
                @Override
                public void onClick(View view) {
                    chooseButton(btn, linearSort);
                }
            });
        }

        LinearLayout linearRating = bottomSheetDialog.findViewById(R.id.linearrating);
        for (int i = 0; i < linearRating.getChildCount(); i++) {
            Button btn = (Button)linearRating.getChildAt(i);
            btn.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ResourceType")
                @Override
                public void onClick(View view) {
                    for (int j = 0; j < linearRating.getChildCount(); j++) {
                        ((Button)linearRating.getChildAt(j)).setBackgroundResource(R.drawable.n_btn_search_filter);
                        ((Button)linearRating.getChildAt(j)).setTextColor(getResources().getColor(R.color.black));
                    }
                    btn.setBackgroundResource(R.drawable.n_btn_search_filter_choose);
                    btn.setTextColor(getResources().getColor(R.color.white));
                }
            });
        }

        Button btnReset = bottomSheetDialog.findViewById(R.id.reset);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText num1 = bottomSheetDialog.findViewById(R.id.editTextNumber);
                EditText num2 = bottomSheetDialog.findViewById(R.id.editTextNumber2);
                num1.setText("");
                num2.setText("");
                for (int j = 0; j < linearSort.getChildCount(); j++) {
                    ((Button)linearSort.getChildAt(j)).setBackgroundResource(R.drawable.n_btn_search_filter);
                    ((Button)linearSort.getChildAt(j)).setTextColor(getResources().getColor(R.color.black));
                }
                for (int j = 0; j < linearCondition.getChildCount(); j++) {
                    ((Button)linearCondition.getChildAt(j)).setBackgroundResource(R.drawable.n_btn_search_filter);
                    ((Button)linearCondition.getChildAt(j)).setTextColor(getResources().getColor(R.color.black));
                }
                for (int j = 0; j < linearRating.getChildCount(); j++) {
                    ((Button)linearRating.getChildAt(j)).setBackgroundResource(R.drawable.n_btn_search_filter);
                    ((Button)linearRating.getChildAt(j)).setTextColor(getResources().getColor(R.color.black));
                }
            }
        });

        Button btnApply = bottomSheetDialog.findViewById(R.id.apply);
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Apply", Toast.LENGTH_LONG).show();
                bottomSheetDialog.cancel();
            }
        });
        bottomSheetDialog.show();
    }

    private void chooseButton(Button btn, LinearLayout linear) {
        for (int j = 0; j < linear.getChildCount(); j++) {
            ((Button)linear.getChildAt(j)).setBackgroundResource(R.drawable.n_btn_search_filter);
            ((Button)linear.getChildAt(j)).setTextColor(getResources().getColor(R.color.black));
        }
        btn.setBackgroundResource(R.drawable.n_btn_search_filter_choose);
        btn.setTextColor(getResources().getColor(R.color.white));
    }
}
