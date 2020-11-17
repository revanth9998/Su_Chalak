package com.suchalak.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.suchalak.R;
import com.suchalak.ResponseData;
import com.suchalak.Utils;
import com.suchalak.api.ApiService;
import com.suchalak.api.RetroClient;
import com.suchalak.model.DriversLoc;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverDetailsActivity  extends AppCompatActivity {
    ImageView iv;
    TextView tv_name,tv_rating,tv_hours_charges,tv_license,tv_description;
    Button btn_book_driver;
    EditText et_from_add,et_to_addr;
    ProgressDialog pd;
    DriversLoc dl;
    SharedPreferences sharedPreferences;
    String session,lat,lang;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_details);
        iv =(ImageView) findViewById(R.id.iv);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Driver Details");

        tv_name =(TextView) findViewById(R.id.tv_name);
        tv_rating =(TextView) findViewById(R.id.tv_rating);
        tv_hours_charges =(TextView) findViewById(R.id.tv_hours_charges);
        tv_license =(TextView) findViewById(R.id.tv_license);
        tv_description =(TextView) findViewById(R.id.tv_description);
        et_from_add =(EditText) findViewById(R.id.et_from_add);
        et_to_addr =(EditText) findViewById(R.id.et_to_addr);

        sharedPreferences = getSharedPreferences(Utils.SHREF, Context.MODE_PRIVATE);
        session = sharedPreferences.getString("user_uname", "def-val");
        lat = sharedPreferences.getString("lat", "def-val");
        lang = sharedPreferences.getString("lng", "def-val");

        Intent intent = getIntent();
        dl= (DriversLoc) intent.getExtras().getSerializable("driver");
        Glide.with(this).load(dl.getProfile_pic()).into(iv);

        tv_name.setText("Name : "+dl.getFirstname());
        tv_rating.setText("Rating : "+dl.getRating());
        tv_hours_charges.setText("Hourly Charges : "+dl.getHours_charges());
        tv_license.setText("License : "+dl.getLicense());
        tv_description.setText("Description : "+dl.getDescription());

        btn_book_driver=(Button)findViewById(R.id.btn_book_driver);
        btn_book_driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                submitData();
            }
        });
    }

    ProgressDialog progressDialog;
    private void submitData () {
        progressDialog = new ProgressDialog(DriverDetailsActivity.this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();

        ApiService service = RetroClient.getRetrofitInstance().create(ApiService.class);
        Call<ResponseData> call = service.user_pickup(et_from_add.getText().toString(),et_to_addr.getText().toString(), session,dl.getEmail(),lat,lang);

        call.enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                if (response.body().status.equals("true")) {
                    Toast.makeText(DriverDetailsActivity.this, response.body().message, Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(DriverDetailsActivity.this, response.body().message, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(DriverDetailsActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }
    @Override                                                                                                                    //add this method in your program
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
