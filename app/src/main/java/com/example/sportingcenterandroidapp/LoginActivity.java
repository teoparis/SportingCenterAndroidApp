package com.example.sportingcenterandroidapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    Context context=this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.etUserName);
        etPassword = findViewById(R.id.etPassword);

        findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });

    }

    private void loginUser() {
        final String userName = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (userName.isEmpty()) {
            etUsername.setError("Inserisci l'Username");
            etUsername.requestFocus();
            return;
        } else if (password.isEmpty()) {
            etPassword.setError("Inserisci la Password");
            etPassword.requestFocus();
            return;
        }

        Call<ResponseLogin> call = com.example.sportingcenterandroidapp.RetrofitClient
                .getInstance(com.example.sportingcenterandroidapp.RetrofitClient.BASE_URL, null)
                .getAPI()
                .checkUser(new LoginRequest(userName, password));

        call.enqueue(new Callback<ResponseLogin>() {
            @Override
            public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {
                if(response!=null){
                    String token = response.body().getAccessToken();
                    com.example.sportingcenterandroidapp.UserTwo user = response.body().getUser();
                    if(user.getRoles().contains("ROLE_ADMIN"))
                    {
                        SharedPreferences preferences = com.example.sportingcenterandroidapp.LoginActivity.this.getSharedPreferences("sporting_center", Context.MODE_PRIVATE);
                        preferences.edit().putString("token",token).apply();
                        Intent intent;
                        intent = new Intent(context, DashboardActivity.class);
                        startActivity(intent);
                    }
                    else
                        Toast.makeText(com.example.sportingcenterandroidapp.LoginActivity.this, "Non hai il ruolo per accedere!", Toast.LENGTH_LONG).show();
            } else {
                    Toast.makeText(com.example.sportingcenterandroidapp.LoginActivity.this, "Errore nel login!", Toast.LENGTH_LONG).show();
                }
            }


            @Override
            public void onFailure(Call<ResponseLogin> call, Throwable t) {
                Toast.makeText(com.example.sportingcenterandroidapp.LoginActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

}