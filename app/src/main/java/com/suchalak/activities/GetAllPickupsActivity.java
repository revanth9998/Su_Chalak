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
import com.suchalak.adapters.GetAllpickupsAdapter;
import com.suchalak.api.ApiService;
import com.suchalak.api.RetroClient;
import com.suchalak.model.GetAllPickupsPojo;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetAllPickupsActivity extends AppCompatActivity {
    ListView list_view;
    ProgressDialog progressDialog;
    List<GetAllPickupsPojo> getAllPickupsPojos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_all_pickups);
        getSupportActionBar().setTitle("Pickup Details");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        list_view=(ListView)findViewById(R.id.list_view);
        getAllPickupsPojos= new ArrayList<>();
        serverData();
    }
    public void serverData(){
        SharedPreferences sharedPreferences = getSharedPreferences(Utils.SHREF, Context.MODE_PRIVATE);
        progressDialog = new ProgressDialog(GetAllPickupsActivity.this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();
        ApiService service = RetroClient.getRetrofitInstance().create(ApiService.class);
        Call<List<GetAllPickupsPojo>> call = service.getallpickups(sharedPreferences.getString("user_uname","-"));
        call.enqueue(new Callback<List<GetAllPickupsPojo>>() {
            @Override
            public void onResponse(Call<List<GetAllPickupsPojo>> call, Response<List<GetAllPickupsPojo>> response) {
                progressDialog.dismiss();
                if(response.body()==null){
                    Toast.makeText(GetAllPickupsActivity.this,"No data found",Toast.LENGTH_SHORT).show();
                }else {
                    getAllPickupsPojos = response.body();
                    list_view.setAdapter(new GetAllpickupsAdapter(getAllPickupsPojos, GetAllPickupsActivity.this));
                }
            }

            @Override
            public void onFailure(Call<List<GetAllPickupsPojo>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(GetAllPickupsActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
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
