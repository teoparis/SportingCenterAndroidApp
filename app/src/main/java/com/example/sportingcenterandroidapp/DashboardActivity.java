package com.example.sportingcenterandroidapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends AppCompatActivity {
    Context context=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        SharedPreferences preferences= com.example.sportingcenterandroidapp.DashboardActivity.this.getSharedPreferences("sporting_center",Context.MODE_PRIVATE);
        final String accessToken  = preferences.getString("token",null);//second parameter default value.


        Call<List<com.example.sportingcenterandroidapp.ResponseEvento>> call = com.example.sportingcenterandroidapp.RetrofitClient
                .getInstance(com.example.sportingcenterandroidapp.RetrofitClient.CALENDAR_URL, accessToken)
                .getAPI()
                .todayEvents();

        call.enqueue(new Callback<List<com.example.sportingcenterandroidapp.ResponseEvento>>() {
            @Override
            public void onResponse(Call<List<com.example.sportingcenterandroidapp.ResponseEvento>> call, Response<List<com.example.sportingcenterandroidapp.ResponseEvento>> response) {
                if(response!=null){
                    ListView listView = (ListView)findViewById(R.id.listViewDemo);
                    List<com.example.sportingcenterandroidapp.ResponseEvento> list = new LinkedList<com.example.sportingcenterandroidapp.ResponseEvento>();
                    list=response.body();
                    CustomAdapter adapter = new CustomAdapter(context, R.layout.activity_list, list);
                    listView.setAdapter(adapter);
                    final List<ResponseEvento> finalList = list;
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {


                            Call<List<UserTwo>> call = com.example.sportingcenterandroidapp.RetrofitClient
                                    .getInstance(com.example.sportingcenterandroidapp.RetrofitClient.CALENDAR_URL, accessToken)
                                    .getAPI()
                                    .findBookedFromEventId(finalList.get(i).getId());

                            call.enqueue(new Callback<List<UserTwo>>() {
                                @Override
                                public void onResponse(Call<List<com.example.sportingcenterandroidapp.UserTwo>> call, Response<List<UserTwo>> response) {
                                    if(response!=null){
                                        List<com.example.sportingcenterandroidapp.UserTwo> list = new LinkedList<UserTwo>();
                                        list=response.body();
                                        if(list.isEmpty())
                                        {
                                            Toast.makeText(com.example.sportingcenterandroidapp.DashboardActivity.this, "Non ci sono prenotati a cui prendere presenze! Seleziona un'altra attività.", Toast.LENGTH_LONG).show();
                                        }
                                        else{
                                            Intent intent = new Intent(context, PrenotatiActivity.class);
                                            intent.putExtra("evento", finalList.get(i).getId());
                                            startActivity(intent);
                                        }

                                    } else {
                                        Toast.makeText(com.example.sportingcenterandroidapp.DashboardActivity.this, "Errore nel recupero dei dati!", Toast.LENGTH_LONG).show();
                                    }

                                }

                                @Override
                                public void onFailure(Call<List<com.example.sportingcenterandroidapp.UserTwo>> call, Throwable t) {
                                    Toast.makeText(com.example.sportingcenterandroidapp.DashboardActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });


                        }
                    });

                } else {
                    Toast.makeText(com.example.sportingcenterandroidapp.DashboardActivity.this, "Errore nel recupero dei dati!", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<List<com.example.sportingcenterandroidapp.ResponseEvento>> call, Throwable t) {
                Toast.makeText(com.example.sportingcenterandroidapp.DashboardActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    public class CustomAdapter extends ArrayAdapter<com.example.sportingcenterandroidapp.ResponseEvento> {


        public CustomAdapter(Context context, int textViewResourceId,
                             List<com.example.sportingcenterandroidapp.ResponseEvento> objects) {
            super(context, textViewResourceId, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.activity_list, null);
            TextView nome = (TextView)convertView.findViewById(R.id.textViewName);
            TextView numero = (TextView)convertView.findViewById(R.id.textViewNumber);
            com.example.sportingcenterandroidapp.ResponseEvento c = getItem(position);
            nome.setText(c.getTitle());
            numero.setText(toStringData(c.getInizio()));

            return convertView;
        }
    }
    private String toStringData(String stringaData) {
        String[] arrayString = stringaData.split("T");
        String stringaDataAggiustata = "Data: "+aggiustaStringData(arrayString[0]) + "  Ora Inizio: " + arrayString[1].split(":")[0]+":"+arrayString[1].split(":")[1];
        return stringaDataAggiustata;
    }
    private String aggiustaStringData(String stringaData) {
        String[] arrayString = stringaData.split("-");
        String stringaDataAggiustata = arrayString[2] + "-" + arrayString[1] + "-" + arrayString[0];
        return stringaDataAggiustata;
    }
}