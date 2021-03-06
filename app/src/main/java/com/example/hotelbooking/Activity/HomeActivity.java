package com.example.hotelbooking.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.hotelbooking.Adapter.ViewPagerAdapter;
import com.example.hotelbooking.Fragment.FragmentAccount;
import com.example.hotelbooking.Fragment.FragmentHome;
import com.example.hotelbooking.Fragment.FragmentMenu;
import com.example.hotelbooking.Fragment.FragmentProfile;
import com.example.hotelbooking.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {
    ViewPager viewPager;
    FragmentHome fragmentHome;
    FragmentMenu fragmentMenu;
    FragmentProfile fragmentProfile;
    MenuItem prevMenuItem;
    BottomNavigationView navigation;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_menu:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_home:
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_profile:
                    viewPager.setCurrentItem(2);
                    return true;
            }
            return false;


        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setItemIconTintList(null);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        navigation.setSelectedItemId(R.id.navigation_home);

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 2) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null)
                        navigation.getMenu().getItem(position).setChecked(true);
                    else {
//                        Toast.makeText(this,"user nullaaaa",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(HomeActivity.this, StartLoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    }
                } else {
                    navigation.getMenu().getItem(position).setChecked(true);
                    prevMenuItem = navigation.getMenu().getItem(position);
                }

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        setupViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        fragmentMenu = new FragmentMenu();
        fragmentHome = new FragmentHome();
        fragmentProfile = new FragmentProfile();

        viewPagerAdapter.addFragment(fragmentMenu);  //index 0
        viewPagerAdapter.addFragment(fragmentHome);
        viewPagerAdapter.addFragment(fragmentProfile);

        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setCurrentItem(1);
    }

}