package com.example.duan1.views;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.duan1.R;
import com.example.duan1.fragment.CartFragment;
import com.example.duan1.fragment.FavoritesFragment;
import com.example.duan1.fragment.HomeFragment;
import com.example.duan1.fragment.UserFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeScreenActivity extends AppCompatActivity {
    private BottomNavigationView mbnv;
    //    private ViewPager mViewPager;
    private FrameLayout frameHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        mbnv = findViewById(R.id.bnv);
        frameHome = findViewById(R.id.frameHome);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameHome, HomeFragment.newInstance(), null)
                .setReorderingAllowed(true)
                .addToBackStack(HomeFragment.TAG) // name can be null
                .commit();
        setUpView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpView();
    }


    private void setUpView() {
//        mbnv.setSelectedItemId(R.id.item_home);
        mbnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                switch (item.getItemId()) {
                    case R.id.item_home: {
                        fragmentManager.beginTransaction()
                                .replace(R.id.frameHome, HomeFragment.newInstance(), null)
                                .setReorderingAllowed(true)
                                .addToBackStack(HomeFragment.TAG) // name can be null
                                .commit();
                        break;
                    }
                    case R.id.item_favorite: {
                        fragmentManager.beginTransaction()
                                .replace(R.id.frameHome, FavoritesFragment.newInstance(), null)
                                .setReorderingAllowed(true)
                                .addToBackStack(FavoritesFragment.TAG) // name can be null
                                .commit();
                        break;
                    }
                    case R.id.item_cart: {
                        fragmentManager.beginTransaction()
                                .replace(R.id.frameHome, CartFragment.newInstance(), null)
                                .setReorderingAllowed(true)
                                .addToBackStack(CartFragment.TAG) // name can be null
                                .commit();
                        break;
                    }
                    case R.id.item_user: {
                        fragmentManager.beginTransaction()
                                .replace(R.id.frameHome, UserFragment.newInstance(), null)
                                .setReorderingAllowed(true)
                                .addToBackStack(UserFragment.TAG) // name can be null
                                .commit();
                        break;
                    }
                    default:
                        fragmentManager.beginTransaction()
                                .replace(R.id.frameHome, HomeFragment.newInstance(), null)
                                .setReorderingAllowed(true)
                                .addToBackStack("name") // name can be null
                                .commit();
                        break;
                }
                return true;

            }
        });
    }
}