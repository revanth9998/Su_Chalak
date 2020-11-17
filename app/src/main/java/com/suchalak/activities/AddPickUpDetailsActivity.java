package com.suchalak.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.suchalak.R;
import com.suchalak.ResponseData;
import com.suchalak.Utils;
import com.suchalak.api.ApiService;
import com.suchalak.api.RetroClient;
import com.suchalak.model.EditProfilePojo;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddPickUpDetailsActivity extends AppCompatActivity {
    EditText et_from_addr,et_to_addr;
    Button btn_submit;
    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;
    String session;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pickup_details);


        sharedPreferences = getSharedPreferences(Utils.SHREF, Context.MODE_PRIVATE);
        session = sharedPreferences.getString("user_uname", "def-val");

        et_from_addr=(EditText)findViewById(R.id.et_from_addr);
        et_to_addr=(EditText)findViewById(R.id.et_to_addr);
        btn_submit=(Button) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et_from_addr.getText().toString().isEmpty()){
                    Toast.makeText(AddPickUpDetailsActivity.this, "Please enter From Address", Toast.LENGTH_SHORT).show();
                    return;
                }
               else if(et_to_addr.getText().toString().isEmpty()){
                    Toast.makeText(AddPickUpDetailsActivity.this, "Please enter To Address", Toast.LENGTH_SHORT).show();
                    return;
                }
               else {
                   submitData();
                }
            }
        });
    }
   private void submitData () {
       progressDialog = new ProgressDialog(AddPickUpDetailsActivity.this);
       progressDialog.setMessage("Loading....");
       progressDialog.show();

       ApiService service = RetroClient.getRetrofitInstance().create(ApiService.class);
       Call<ResponseData> call = service.user_pickup(et_from_addr.getText().toString(),et_to_addr.getText().toString(), session,getIntent().getStringExtra("driver_uname"),getIntent().getStringExtra("user_lat"),getIntent().getStringExtra("user_lng"));

       call.enqueue(new Callback<ResponseData>() {
           @Override
           public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
               if (response.body().status.equals("true")) {
                   Toast.makeText(AddPickUpDetailsActivity.this, response.body().message, Toast.LENGTH_LONG).show();
                   finish();
               } else {
                   Toast.makeText(AddPickUpDetailsActivity.this, response.body().message, Toast.LENGTH_LONG).show();
               }
           }

           @Override
           public void onFailure(Call<ResponseData> call, Throwable t) {
               progressDialog.dismiss();
               Toast.makeText(AddPickUpDetailsActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();

           }
       });

   }
}

