package com.example.duan1.views;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import com.example.duan1.R;
import com.example.duan1.adapter.AboutSlideAdapter;
import com.example.duan1.model.MemberGroup2;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator3;

public class AboutActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private final Handler mHandler = new Handler(Looper.myLooper());
    private List<MemberGroup2> mListMem;
    private ViewPager2 mViewPager2About;
    private CircleIndicator3 mCircleIndicator3About;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        unitUi();
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("About Us");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        setSlideShow();
    }

    private void unitUi() {
        toolbar = findViewById(R.id.toolbar);
        mViewPager2About = findViewById(R.id.mViewPager2About);
        mCircleIndicator3About = findViewById(R.id.mCircleIndicator3About);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setSlideShow() {
        mListMem = new ArrayList<>();
        mListMem.add(new MemberGroup2("Mai Hoàng Đức Khải", "31/03/2003", "Thành Phố Hồ Chí Minh", "Leader", "Mobile Developer", R.drawable.rosi, "https://www.facebook.com/mugaviplam"));
        mListMem.add(new MemberGroup2("Huỳnh Hồng Vỹ", "10/12/2003", "Long An", "Member", "Mobile Developer", R.drawable.modric, "https://www.facebook.com/vy.huynhhong.90/"));
        mListMem.add(new MemberGroup2("Trương Đình Thảo", "12/7/2003", "Đồng Nai", "Member", "Mobile Developer", R.drawable.cr7, "https://www.facebook.com/truong.thao.921677?mibextid=ZbWKwL"));
        mListMem.add(new MemberGroup2("Nguyễn Văn Nhật Toàn", "20/03/2003", "Gia Lai", "Member", "Mobile Developer", R.drawable.n10, "https://www.facebook.com/nhattoann2003?mibextid=ZbWKwL"));
        mListMem.add(new MemberGroup2("Huỳnh Bùi Trọng Đạt", "06/11/2003", "Bình Định", "Member", "Mobile Developer", R.drawable.martinez, "https://www.facebook.com/O123456789X?mibextid=ZbWKwL"));
        AboutSlideAdapter aboutSlideAdapter = new AboutSlideAdapter(mListMem);
        mViewPager2About.setAdapter(aboutSlideAdapter);
        mCircleIndicator3About.setViewPager(mViewPager2About);
        mViewPager2About.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mHandler.removeCallbacks(mRun);
                mHandler.postDelayed(mRun, 5000);
            }
        });

    }

    private final Runnable mRun = new Runnable() {
        @Override
        public void run() {
            int currentPos = mViewPager2About.getCurrentItem();
            if (currentPos == mListMem.size() - 1) {
                mViewPager2About.setCurrentItem(0);
            } else {
                mViewPager2About.setCurrentItem(currentPos + 1);
            }
        }
    };
}