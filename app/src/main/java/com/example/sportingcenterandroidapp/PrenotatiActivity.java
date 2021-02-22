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

import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PrenotatiActivity extends AppCompatActivity {
    Context context=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prenotati);
        Bundle bundle = getIntent().getExtras();
        if(bundle.getInt("evento")!= 0)
        {
            SharedPreferences preferences= com.example.sportingcenterandroidapp.PrenotatiActivity.this.getSharedPreferences("sporting_center",Context.MODE_PRIVATE);
            String accessToken  = preferences.getString("token",null);//second parameter default value.
            Integer id = bundle.getInt("evento");

            Call<List<UserTwo>> call = com.example.sportingcenterandroidapp.RetrofitClient
                    .getInstance(com.example.sportingcenterandroidapp.RetrofitClient.CALENDAR_URL, accessToken)
                    .getAPI()
                    .findBookedFromEventId(id);

            call.enqueue(new Callback<List<UserTwo>>() {
                @Override
                public void onResponse(Call<List<com.example.sportingcenterandroidapp.UserTwo>> call, Response<List<UserTwo>> response) {
                    if(response!=null){
                        ListView listView = (ListView)findViewById(R.id.listViewDemo2);
                        List<com.example.sportingcenterandroidapp.UserTwo> list = new LinkedList<UserTwo>();
                        list=response.body();
                        PrenotatiActivity.CustomAdapter adapter = new PrenotatiActivity.CustomAdapter(context, R.layout.activity_list, list);
                        listView.setAdapter(adapter);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Intent intent = new Intent(context, RegisterActivity.class);
                                intent.putExtra("position", i);
                                startActivity(intent);
                            }
                        });

                    } else {
                        Toast.makeText(com.example.sportingcenterandroidapp.PrenotatiActivity.this, "Errore nel recupero dei dati!", Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onFailure(Call<List<com.example.sportingcenterandroidapp.UserTwo>> call, Throwable t) {
                    Toast.makeText(com.example.sportingcenterandroidapp.PrenotatiActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }
    public class CustomAdapter extends ArrayAdapter<UserTwo> {


        public CustomAdapter(Context context, int textViewResourceId,
                             List<com.example.sportingcenterandroidapp.UserTwo> objects) {
            super(context,
                    textViewResourceId,
                    objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.activity_list, null);
            TextView nome = (TextView)convertView.findViewById(R.id.textViewName);
            com.example.sportingcenterandroidapp.UserTwo c = getItem(position);
            nome.setText(c.getDisplayName()+"    "+c.getEmail());
            return convertView;
        }
    }
}

