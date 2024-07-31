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
import com.cs2340aG49.greenPlate.databinding.ActivityLoginViewBinding;
import com.cs2340aG49.greenPlate.ui.viewmodel.LoginViewModel;

public class LoginViewActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private ActivityLoginViewBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loginViewModel = new LoginViewModel();
        binding = ActivityLoginViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        final EditText usernameEditText = binding.username;
        final EditText passwordEditText = binding.password;
        final Button loginButton = binding.login;
        final Button createAccountButton = binding.goToRegistration;
        final Button quit = binding.quit;
        final ProgressBar loadingProgressBar = binding.loading;

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
                // Validate password
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

                if (loginViewModel.login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString())) {
                        Intent intent = new Intent(LoginViewActivity.this,
                                HomeActivity.class).putExtra("username",
                                usernameEditText.getText().toString());
                        startActivity(intent);
                        finish();
                } else {
                    usernameEditText.setText("");
                    passwordEditText.setText("");
                    Toast.makeText(getApplicationContext(),
                            "Login Failed.", Toast.LENGTH_SHORT).show();

                }
            }


        });

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginViewActivity.this, CreateAccountActivity.class);
                startActivity(intent);
                finish();
            }
        });

        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}