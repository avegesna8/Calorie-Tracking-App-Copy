package com.cs2340aG49.greenPlate.ui.view;

import android.os.Bundle;

import com.cs2340aG49.greenPlate.databinding.ActivityHomeBinding;
import com.cs2340aG49.greenPlate.ui.NavigationBar;

public class HomeActivity extends NavigationBar {

    private ActivityHomeBinding activityHomeBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityHomeBinding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(activityHomeBinding.getRoot());
        allocatedActivityTitle("Home");

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String username = bundle.getString("username");
            NavigationBar.setUsername(username);
        }

    }
}