package com.example.cartandorder;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.cartandorder.fragments.ViewPageAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    ViewPager viewPage;
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        viewPage = (ViewPager) findViewById(R.id.viewPager);
        bottomNavigationView= (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        ViewPageAdapter adapter = new ViewPageAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPage.setAdapter(adapter);
        viewPage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0: bottomNavigationView.getMenu().findItem(R.id.home_icon).setChecked(true);
                        break;

                    case 1: bottomNavigationView.getMenu().findItem(R.id.cart_icon).setChecked(true);
                        break;
                    case 2: bottomNavigationView.getMenu().findItem(R.id.chat_icon).setChecked(true);
                        break;
                    case 3: bottomNavigationView.getMenu().findItem(R.id.notification_icon).setChecked(true);
                        break;
                    case 4: bottomNavigationView.getMenu().findItem(R.id.profile_icon).setChecked(true);
                        break;


                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int id = item.getItemId();
            if(id == R.id.home_icon){
                viewPage.setCurrentItem(0);
            } else if (id == R.id.cart_icon) {
                viewPage.setCurrentItem(1);
            }else if(id == R.id.chat_icon){
                viewPage.setCurrentItem(2);
            }else if(id == R.id.notification_icon){
                viewPage.setCurrentItem(3);
            }else {
                viewPage.setCurrentItem(4);
            }
            return true;
        }
    });
    }
}