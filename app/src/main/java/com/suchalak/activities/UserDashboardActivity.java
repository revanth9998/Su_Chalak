package com.suchalak.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;
import com.suchalak.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.suchalak.Utils;
import com.suchalak.adapters.DriverDetailsAdapter;
import com.suchalak.adapters.GetAllpickupsAdapter;
import com.suchalak.api.ApiService;
import com.suchalak.api.RetroClient;
import com.suchalak.model.DriversLoc;
import com.suchalak.model.GetAllPickupsPojo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserDashboardActivity extends BaseActivity {
    private ActionBarDrawerToggle t;
    private NavigationView nv;
    private DrawerLayout dl;
    ListView list_view;
    double my_lat=17.361719,my_lng=78.475166;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);
        SharedPreferences sharedPreferences = getSharedPreferences(Utils.SHREF, Context.MODE_PRIVATE);
        my_lat =Double.parseDouble(sharedPreferences.getString("lat","0"));
        my_lng =Double.parseDouble(sharedPreferences.getString("lng","0"));
        navigationView();
        getSupportActionBar().setTitle("User Dashboard");
         list_view=(ListView)findViewById(R.id.list_view);
        serverData();
    }
    ProgressDialog progressDialog;
    List<DriversLoc> listDriverLoc;
    public void serverData(){
        progressDialog = new ProgressDialog(UserDashboardActivity.this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();
        ApiService service = RetroClient.getRetrofitInstance().create(ApiService.class);
        Call<List<DriversLoc>> call = service.getDriverDetails(""+my_lat,""+my_lng);
        call.enqueue(new Callback<List<DriversLoc>>() {
            @Override
            public void onResponse(Call<List<DriversLoc>> call, Response<List<DriversLoc>> response) {
                progressDialog.dismiss();
                if(response.body()==null){
                    Toast.makeText(UserDashboardActivity.this,"No data found",Toast.LENGTH_SHORT).show();
                }else {
                    listDriverLoc = response.body();
                    if(listDriverLoc!=null){
                        //Toast.makeText(MapsActivity.this,""+listDriverLoc.size(),Toast.LENGTH_SHORT).show();
                        if(listDriverLoc.size()>0){
                            list_view.setAdapter(new DriverDetailsAdapter(listDriverLoc, UserDashboardActivity.this));
                        }
                    }
                    //
                }
            }
            @Override
            public void onFailure(Call<List<DriversLoc>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(UserDashboardActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
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

                    case R.id.add_pickup_details:
                        /*Intent pickup=new Intent(getApplicationContext(), AddPickUpDetailsActivity.class);
                        startActivity(pickup);*/
                        Intent pickup=new Intent(getApplicationContext(), MapsActivity.class);
                        startActivity(pickup);
                        break;
                    case R.id.my_accepted_Pickups:
                        Intent accepted_Pickups=new Intent(getApplicationContext(), UserAcceptedPickupsActivity.class);
                        accepted_Pickups.putExtra("status","accept");
                        accepted_Pickups.putExtra("name","User Accepted Pickups");
                        startActivity(accepted_Pickups);
                        break;

                    case R.id.my_rejected_Pickups:
                        Intent rejected_Pickups=new Intent(getApplicationContext(), UserAcceptedPickupsActivity.class);
                        rejected_Pickups.putExtra("status","reject");
                        rejected_Pickups.putExtra("name","User Rejected Pickups");
                        startActivity(rejected_Pickups);
                        break;

                    case R.id.my_pending_Pickups:
                        Intent pending_Pickups=new Intent(getApplicationContext(), UserAcceptedPickupsActivity.class);
                        pending_Pickups.putExtra("status","pending");
                        pending_Pickups.putExtra("name","User Pending Pickups");
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