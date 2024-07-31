package com.cs2340aG49.greenPlate.ui.view;


import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.cs2340aG49.greenPlate.databinding.ActivityCreateAccountBinding;
import com.cs2340aG49.greenPlate.ui.viewmodel.LoginViewModel;

public class CreateAccountActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private ActivityCreateAccountBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginViewModel = new LoginViewModel();

        binding = ActivityCreateAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        final EditText usernameEditText = binding.username;
        final EditText passwordEditText = binding.password;
        final Button loginButton = binding.createAccount;
        final ProgressBar loadingProgressBar = binding.loading;
        final Button backToLogin = binding.backToLogin;


        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                    // Validate username
                    String username = usernameEditText.getText().toString();
                    String password = passwordEditText.getText().toString();
                    if (username.trim().isEmpty()) {
                        usernameEditText.setError("Username cannot be empty");
                    } else if (username.length() < 5) {
                        usernameEditText.setError("Username must contain at least 5 characters");
                    } else if (username.contains(" ") || username.contains("\n")) {
                        usernameEditText.setError("No whitespace");
                    }

                    if (password.trim().isEmpty()) {
                        passwordEditText.setError("Password cannot be empty");
                    } else if (password.length() < 5) {
                        passwordEditText.setError("Password must be at least 5 characters long");
                    } else if (password.contains(" ") || username.contains("\n")) {
                        passwordEditText.setError("Password cannot contain spaces");
                    } else {
                        loginButton.setEnabled(true);
                    }
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loginViewModel.createAccount(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString())) {
                    Toast.makeText(getApplicationContext(),
                            "Account Created.", Toast.LENGTH_SHORT).show();
                    usernameEditText.setText("");
                    passwordEditText.setText("");
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Account Creation Failed.", Toast.LENGTH_SHORT).show();

                    usernameEditText.setText("");
                    passwordEditText.setText("");
                }
            }
        });

        backToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateAccountActivity.this, LoginViewActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}