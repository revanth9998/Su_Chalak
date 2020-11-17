package com.suchalak.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;
import com.suchalak.R;
import com.suchalak.Utils;
import com.suchalak.adapters.DashboardDriverPickupsAdapter;
import com.suchalak.adapters.DriverAcceptedPickupsAdapters;
import com.suchalak.api.ApiService;
import com.suchalak.api.RetroClient;
import com.suchalak.model.UserPickupsPojo;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverDashboardActivity extends BaseActivity {
    private ActionBarDrawerToggle t;
    private NavigationView nv;
    private DrawerLayout dl;
    ListView list_view;
    List<UserPickupsPojo> userPickups;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_driver_dashboard);
        navigationView();
        getSupportActionBar().setTitle("Driver Dashboard");
        list_view=(ListView)findViewById(R.id.list_view);
        serverData();
    }
    ProgressDialog progressDialog;
    public void serverData(){
        SharedPreferences sharedPreferences = getSharedPreferences(Utils.SHREF, Context.MODE_PRIVATE);
        progressDialog = new ProgressDialog(DriverDashboardActivity.this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();
        ApiService service = RetroClient.getRetrofitInstance().create(ApiService.class);
        Call<List<UserPickupsPojo>> call = service.getDriverPickupDetails(sharedPreferences.getString("user_uname","-"),"accept");
        call.enqueue(new Callback<List<UserPickupsPojo>>() {
            @Override
            public void onResponse(Call<List<UserPickupsPojo>> call, Response<List<UserPickupsPojo>> response) {
                progressDialog.dismiss();
                if(response.body()==null){
                    Toast.makeText(DriverDashboardActivity.this,"No data found",Toast.LENGTH_SHORT).show();
                }else {
                    userPickups = response.body();
                    list_view.setAdapter(new DashboardDriverPickupsAdapter(userPickups, DriverDashboardActivity.this));
                }
            }

            @Override
            public void onFailure(Call<List<UserPickupsPojo>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(DriverDashboardActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigationView(){
        dl = (DrawerLayout)findViewById(R.id.dl_main);
        t = new ActionBarDrawerToggle(this, dl,R.string.Open, R.string.Close);
        dl.addDrawerListener(t);
        t.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        nv = (NavigationView)findViewById(R.id.nv);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id)
                {
                    case R.id.myprofile:
                        Intent intent=new Intent(getApplicationContext(), EditProfileActivity.class);
                        startActivity(intent);
                        break;

                    case R.id.pickup_details:
                        Intent pickup_details=new Intent(getApplicationContext(), GetAllPickupsActivity.class);
                        startActivity(pickup_details);
                        break;
                    case R.id.my_accepted_Pickups:
                        Intent accepted_Pickups=new Intent(getApplicationContext(), DriverPickupsActivity.class);
                        accepted_Pickups.putExtra("status","accept");
                        accepted_Pickups.putExtra("name","Accepted Pickups");
                        startActivity(accepted_Pickups);
                        break;

                    case R.id.my_rejected_Pickups:
                        Intent rejected_Pickups=new Intent(getApplicationContext(), DriverPickupsActivity.class);
                        rejected_Pickups.putExtra("status","reject");
                        rejected_Pickups.putExtra("name","Rejected Pickups");
                        startActivity(rejected_Pickups);
                        break;

                    case R.id.my_pending_Pickups:
                        Intent pending_Pickups=new Intent(getApplicationContext(), DriverPickupsActivity.class);
                        pending_Pickups.putExtra("status","pending");
                        pending_Pickups.putExtra("name","Pending Pickups");
                        startActivity(pending_Pickups);
                        break;
                    case R.id.logout:
                        Intent logout=new Intent(getApplicationContext(), LoginActivity.class);
                        SharedPreferences sharedPreferences = getSharedPreferences(Utils.SHREF, Context.MODE_PRIVATE);
                        SharedPreferences.Editor et=sharedPreferences.edit();
                        et.putString("first_time_login","yes");
                        et.putString("role","");
                        et.commit();
                        startActivity(logout);
                        finish();
                        break;
                    default:
                        return true;
                }
                dl.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }
    @Override
    public void onBackPressed() {
        if (dl.isDrawerOpen(GravityCompat.START)) {
            dl.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (dl.isDrawerOpen(GravityCompat.START)) {
            dl.closeDrawer(GravityCompat.START);
        } else {
            dl.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }
}