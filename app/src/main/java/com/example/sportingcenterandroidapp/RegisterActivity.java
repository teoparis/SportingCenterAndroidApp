package com.example.sportingcenterandroidapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sportingcenterandroidapp.R.id.etRPassword;

public class RegisterActivity extends AppCompatActivity {

    private EditText etUsername, etEmail, etPassword, etRepeatPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUsername = findViewById(R.id.etRUserName);
        etEmail =  findViewById(R.id.etEmail);
        etPassword = findViewById(etRPassword);
        etRepeatPassword = findViewById(R.id.RepeatPassword);
        findViewById(R.id.btnRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

        findViewById(R.id.tvLoginLink).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(com.example.sportingcenterandroidapp.RegisterActivity.this, com.example.sportingcenterandroidapp.LoginActivity.class));
            }
        });
    }

    private void registerUser() {
        String userName = etUsername.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String repeatPassword = etRepeatPassword.getText().toString().trim();

        if (userName.isEmpty()) {
            etUsername.setError("Inserisci l'Username");
            etUsername.requestFocus();
            return;
        } else if (password.isEmpty()) {
            etPassword.setError("Inserisci la Password");
            etPassword.requestFocus();
            return;
        }

        Call<ResponseBody> call = com.example.sportingcenterandroidapp.RetrofitClient
                .getInstance(com.example.sportingcenterandroidapp.RetrofitClient.CALENDAR_URL, null)
                .getAPI()
                .createUser(new com.example.sportingcenterandroidapp.User(userName, password));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String s = "";
                try {
                    s = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (s.equals("SUCCESS")) {
                    Toast.makeText(com.example.sportingcenterandroidapp.RegisterActivity.this, "Successfully registered. Please login", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(com.example.sportingcenterandroidapp.RegisterActivity.this, com.example.sportingcenterandroidapp.LoginActivity.class));
                } else {
                    Toast.makeText(com.example.sportingcenterandroidapp.RegisterActivity.this, "User already exists!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(com.example.sportingcenterandroidapp.RegisterActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }
}