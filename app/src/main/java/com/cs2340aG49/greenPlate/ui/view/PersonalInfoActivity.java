package com.cs2340aG49.greenPlate.ui.view;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.util.Log;

import com.cs2340aG49.greenPlate.databinding.ActivityPersonalInfoBinding;
import com.cs2340aG49.greenPlate.ui.NavigationBar;
import com.cs2340aG49.greenPlate.ui.model.Database;
import com.cs2340aG49.greenPlate.ui.model.User;


public class PersonalInfoActivity extends NavigationBar {

    private ActivityPersonalInfoBinding activityPersonalInfoBinding;
    private Database database;
    private User user;
    private EditText heightEditText;
    private EditText weightEditText;
    private EditText genderEditText;
    private Button submitInfoButton;
    private String userName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        activityPersonalInfoBinding = activityPersonalInfoBinding.inflate(getLayoutInflater());
        setContentView(activityPersonalInfoBinding.getRoot());
        allocatedActivityTitle("Personal Information");


        heightEditText = activityPersonalInfoBinding.heightEditText;
        weightEditText = activityPersonalInfoBinding.weightEditText;
        genderEditText = activityPersonalInfoBinding.genderEditText;
        submitInfoButton = activityPersonalInfoBinding.submitInfoButton;
        submitInfoButton.setEnabled(true);


        database = Database.getInstance();
        userName = NavigationBar.getUsername();
        try {
            user = database.getUser(userName);

        } catch (Exception e) {
            Log.e("TAG", e.getMessage(), e);
        }
        submitInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!heightEditText.getText().toString().equals("")) {
                    double height = Double.parseDouble(heightEditText.getText().toString());
                    user.setHeight(height);
                    database.updatePersonalInfo(userName, height, user.getWeight(),
                            user.getGender());
                }

                if (!weightEditText.getText().toString().equals("")) {
                    double weight = Double.parseDouble(weightEditText.getText().toString());
                    user.setWeight(weight);
                    database.updatePersonalInfo(userName, user.getHeight(), weight,
                            user.getGender());

                }

                if (!genderEditText.getText().toString().equals("")) {
                    String gender = genderEditText.getText().toString();
                    user.setGender(gender);
                    database.updatePersonalInfo(userName, user.getHeight(),
                            user.getWeight(), gender);
                }


                heightEditText.setText("");
                weightEditText.setText("");
                genderEditText.setText("");

            }
        });


    }
}