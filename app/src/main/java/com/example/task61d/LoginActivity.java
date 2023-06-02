package com.example.task61d;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.task61d.data.UserDatabaseHelper;

public class LoginActivity extends AppCompatActivity {

    EditText username, password;
    Button login, signup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        signup = findViewById(R.id.signup);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tempUser, tempPswd;
                tempUser = username.getText().toString();
                tempPswd = password.getText().toString();
                if(TextUtils.isEmpty(username.getText().toString())){
                    showToast("Enter a username!");
                    username.setError("Username is compulsory");
                    return;
                } else if (TextUtils.isEmpty(password.getText().toString())) {
                    showToast("Enter a password!");
                    password.setError("Password is compulsory");
                    return;
                }
                UserDatabaseHelper userDatabaseHelper = new UserDatabaseHelper(LoginActivity.this);
                boolean isValidlogin = userDatabaseHelper.isValidLogin(tempUser, tempPswd);
                if(isValidlogin){
                    SharedPreferences sharedPreferences = getSharedPreferences("userLogin", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("isLoggedIn", true);
                    editor.apply();
                    Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(i);
                }else {
                    showToast("Username not registered. Click on Sign Up and create account.");
                }

            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(i);
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}