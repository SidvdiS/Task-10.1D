package com.example.task61d;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.task61d.data.UserDatabaseHelper;
import com.example.task61d.model.User;

import java.io.IOException;

public class SignupActivity extends AppCompatActivity {
    ImageView imgView;
    EditText fullName, username, password, confirmPassword, phoneNo;
    Button createAccount;

    Bitmap imgBitmap = null;
    private static final int GALLERY_REQ_CODE = 12;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        imgView = findViewById(R.id.user_img);
        fullName = findViewById(R.id.full_name);
        username = findViewById(R.id.username);
        password = findViewById(R.id.pswd);
        confirmPassword = findViewById(R.id.confirm_pswd);
        phoneNo = findViewById(R.id.phone_no);
        createAccount = findViewById(R.id.create_account);

        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iGallery = new Intent(Intent.ACTION_PICK);
                iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(iGallery, GALLERY_REQ_CODE);
            }
        });

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usernameText = username.getText().toString();
                String fullNameText = fullName.getText().toString();
                String passwordText = password.getText().toString();
                String confirmPswdText = confirmPassword.getText().toString();
                String phoneNoText = phoneNo.getText().toString();

                if(TextUtils.isEmpty(usernameText)){
                    showToast("Enter a username");
                    return;
                } else if (TextUtils.isEmpty(passwordText)) {
                    showToast("Enter a password");
                    return;
                } else if (!TextUtils.equals(passwordText,confirmPswdText)) {
                    showToast("Password and Confirm Password do not match");
                    return;
                }

                User user = new User(usernameText, fullNameText, passwordText, phoneNoText, imgBitmap);
                UserDatabaseHelper userDatabaseHelper = new UserDatabaseHelper(SignupActivity.this);
                long insert = userDatabaseHelper.insertUser(user);
                if(insert==-1){
                    showToast("Cannot create account");
                } else {
                    showToast("Account created successfully");
                    Intent i = new Intent(SignupActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_REQ_CODE && resultCode == RESULT_OK && data != null){
            try {
                imgBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),data.getData());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            imgView.setImageBitmap(imgBitmap);
        }
    }

    private void showToast(String message) {
        Toast.makeText(SignupActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}