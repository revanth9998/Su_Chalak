package com.suchalak.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.suchalak.R;
import com.suchalak.Utils;
import com.suchalak.adapters.DriverAcceptedPickupsAdapters;
import com.suchalak.adapters.UserAcceptedPickupsAdapters;
import com.suchalak.api.ApiService;
import com.suchalak.api.RetroClient;
import com.suchalak.model.UserPickupsPojo;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DriverPickupsActivity  extends AppCompatActivity {
    ListView list_view;
    List<UserPickupsPojo> userPickups;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_all_pickups);

        getSupportActionBar().setTitle(getIntent().getStringExtra("name"));
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        list_view=(ListView)findViewById(R.id.list_view);
        //getAllPickupsPojos= new ArrayList<>();
        serverData();
    }
    ProgressDialog progressDialog;
    public void serverData(){
        SharedPreferences sharedPreferences = getSharedPreferences(Utils.SHREF, Context.MODE_PRIVATE);
        progressDialog = new ProgressDialog(DriverPickupsActivity.this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();
        ApiService service = RetroClient.getRetrofitInstance().create(ApiService.class);
        Call<List<UserPickupsPojo>> call = service.getDriverPickupDetails(sharedPreferences.getString("user_uname","-"),getIntent().getStringExtra("status"));
        call.enqueue(new Callback<List<UserPickupsPojo>>() {
            @Override
            public void onResponse(Call<List<UserPickupsPojo>> call, Response<List<UserPickupsPojo>> response) {
                progressDialog.dismiss();
                if(response.body()==null){
                    Toast.makeText(DriverPickupsActivity.this,"No data found",Toast.LENGTH_SHORT).show();
                }else {
                    userPickups = response.body();
                    list_view.setAdapter(new DriverAcceptedPickupsAdapters(userPickups, DriverPickupsActivity.this));
                }
            }
            @Override
            public void onFailure(Call<List<UserPickupsPojo>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(DriverPickupsActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
