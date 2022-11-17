package com.example.duan1.views;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.duan1.R;
import com.example.duan1.adapter.ViewPagerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeScreenActivity extends AppCompatActivity {
    private BottomNavigationView mbnv;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        mbnv = findViewById(R.id.bnv);
        mViewPager = findViewById(R.id.viewPager);
        setUpViewPager();
        mbnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_home: {
                        mViewPager.setCurrentItem(0);
                        break;
                    }
                    case R.id.item_favorite: {
                        mViewPager.setCurrentItem(1);
                        break;
                    }
                    case R.id.item_cart: {
                        mViewPager.setCurrentItem(2);
                        break;
                    }
                    case R.id.item_user: {
                        mViewPager.setCurrentItem(3);
                        break;
                    }
                    default:
                        mViewPager.setCurrentItem(0);
                        break;
                }
                return true;
            }
        });
    }

    private void setUpViewPager() {
        ViewPagerAdapter adaper = new ViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mViewPager.setAdapter(adaper);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0: {
                        mbnv.getMenu().findItem(R.id.item_home).setChecked(true);
                        break;
                    }
                    case 1: {
                        mbnv.getMenu().findItem(R.id.item_favorite).setChecked(true);
                        break;
                    }
                    case 2: {
                        mbnv.getMenu().findItem(R.id.item_cart).setChecked(true);
                        break;
                    }
                    case 3: {
                        mbnv.getMenu().findItem(R.id.item_user).setChecked(true);
                        break;
                    }
                    default:
                        mbnv.getMenu().findItem(R.id.item_home).setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}