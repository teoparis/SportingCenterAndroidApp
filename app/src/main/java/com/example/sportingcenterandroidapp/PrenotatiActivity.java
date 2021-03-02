package com.example.sportingcenterandroidapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PrenotatiActivity extends AppCompatActivity {
    ArrayList<UserTwo> selectedItems;
    Context context=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prenotati);
        Bundle bundle = getIntent().getExtras();
        Integer id = bundle.getInt("evento");
        if(bundle.getInt("evento")!= 0)
        {
            selectedItems=new ArrayList<UserTwo>();
            SharedPreferences preferences= com.example.sportingcenterandroidapp.PrenotatiActivity.this.getSharedPreferences("sporting_center",Context.MODE_PRIVATE);
            String accessToken  = preferences.getString("token",null);//second parameter default value.


            Call<List<UserTwo>> call = com.example.sportingcenterandroidapp.RetrofitClient
                    .getInstance(com.example.sportingcenterandroidapp.RetrofitClient.CALENDAR_URL, accessToken)
                    .getAPI()
                    .findBookedFromEventId(id);

            call.enqueue(new Callback<List<UserTwo>>() {
                @Override
                public void onResponse(Call<List<com.example.sportingcenterandroidapp.UserTwo>> call, Response<List<UserTwo>> response) {
                    if(response!=null){
                        ListView listView = (ListView)findViewById(R.id.listViewDemo2);
                        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                        List<com.example.sportingcenterandroidapp.UserTwo> list = new LinkedList<UserTwo>();
                        list=response.body();
                        if(list.isEmpty())
                        {
                            Toast.makeText(com.example.sportingcenterandroidapp.PrenotatiActivity.this, "Non ci sono prenotati a cui prendere presenze! Seleziona un'altra attività.", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(context, DashboardActivity.class);
                            startActivity(intent);
                        }
                        PrenotatiActivity.CustomAdapter adapter = new PrenotatiActivity.CustomAdapter(context, R.layout.checkable_list_layout, list);
                        listView.setAdapter(adapter);
                        final List<UserTwo> finalList = list;
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                // selected item
                                UserTwo selectedItem = finalList.get(position);
                                if(selectedItems.contains(selectedItem))
                                    selectedItems.remove(selectedItem); //remove deselected item from the list of selected items
                                else
                                    selectedItems.add(selectedItem); //add selected item to the list of selected items
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
            convertView = inflater.inflate(R.layout.checkable_list_layout, null);
            TextView nome = (TextView)convertView.findViewById(R.id.txt_title);
            UserTwo c = getItem(position);
            nome.setText(c.getDisplayName());
            return convertView;
        }
    }

    public void showSelectedItems(View view){

        SharedPreferences preferences= com.example.sportingcenterandroidapp.PrenotatiActivity.this.getSharedPreferences("sporting_center",Context.MODE_PRIVATE);
        String accessToken  = preferences.getString("token",null);//second parameter default value.
        Bundle bundle = getIntent().getExtras();
        Integer id = bundle.getInt("evento");

        Call<ResponseBody> call = com.example.sportingcenterandroidapp.RetrofitClient
                .getInstance(com.example.sportingcenterandroidapp.RetrofitClient.CALENDAR_URL, accessToken)
                .getAPI()
                .setUsersPresence(id,selectedItems);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(com.example.sportingcenterandroidapp.PrenotatiActivity.this, "Dati salvati correttamente!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(context, DashboardActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(com.example.sportingcenterandroidapp.PrenotatiActivity.this, "Errore nel salvataggio dei dati!", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(com.example.sportingcenterandroidapp.PrenotatiActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}

