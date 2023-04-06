package com.nocial;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NavBaseActivity extends AppCompatActivity {

    @Override
    public void setContentView(View view) {

        super.setContentView(R.layout.activity_nav_base);

        FrameLayout activity_container = findViewById(R.id.activity_container);
        activity_container.addView(view);

        BottomNavigationView bottomNavView = findViewById(R.id.nav_view);

        if (this.title == "Home") {
            bottomNavView.selectedItemId = R.id.navigation_home;
        } else if (this.title == "Dashboard") {
            bottomNavView.selectedItemId = R.id.navigation_dashboard;
        } else if (this.title == "Notification") {
            bottomNavView.selectedItemId = R.id.navigation_notifications;
        }

        bottomNavView.setOnItemSelectedListener { item ->
            item.isChecked = true;
            val id = item.itemId;

            if (id == R.id.navigation_home) {
                startActivity(Intent(applicationContext, HomeActivity::class.java));
                overridePendingTransition(0, 0);
                finish();
            } else if (id == R.id.navigation_dashboard) {
                startActivity(Intent(applicationContext, DashboardActivity::class.java));
                overridePendingTransition(0, 0);
                finish();
            } else if (id == R.id.navigation_notifications) {
                startActivity(Intent(applicationContext, NotificationActivity::class.java));
                overridePendingTransition(0, 0);
                finish();
            }

            true;
        };

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_base);
    }
}