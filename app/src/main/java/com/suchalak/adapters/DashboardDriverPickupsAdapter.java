package com.suchalak.adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.suchalak.R;
import com.suchalak.ResponseData;
import com.suchalak.activities.DriverDashboardActivity;
import com.suchalak.api.ApiService;
import com.suchalak.api.RetroClient;
import com.suchalak.model.UserPickupsPojo;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardDriverPickupsAdapter extends BaseAdapter {
    List<UserPickupsPojo> ar;
    Context cnt;

    public DashboardDriverPickupsAdapter(List<UserPickupsPojo> ar, Context cnt) {
        this.ar = ar;
        this.cnt = cnt;
    }

    @Override
    public int getCount() {
        return ar.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int pos, View view, ViewGroup viewGroup) {
        LayoutInflater obj1 = (LayoutInflater) cnt.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View obj2 = obj1.inflate(R.layout.adapter_driver_dash_board, null);

        TextView tv_from = (TextView) obj2.findViewById(R.id.tv_from);
        tv_from.setText("Pickup From  :" + ar.get(pos).getPick_from());

        TextView tv_to = (TextView) obj2.findViewById(R.id.tv_to);
        tv_to.setText("To  :" + ar.get(pos).getPick_to());

        TextView tv_driver_uname = (TextView) obj2.findViewById(R.id.tv_driver_uname);
        tv_driver_uname.setText("Username : "+ar.get(pos).getUname());

        TextView tv_status = (TextView) obj2.findViewById(R.id.tv_status);
        tv_status.setText("Status : "+ar.get(pos).getStatus());

        Button delete_pic_up=(Button)obj2.findViewById(R.id.delete_pic_up);
        delete_pic_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serverData(ar.get(pos).getId());

            }
        });

        Typeface custom_font = Typeface.createFromAsset(cnt.getAssets(), "fonts/Lato-Medium.ttf");
        tv_from.setTypeface(custom_font);
        tv_to.setTypeface(custom_font);
        tv_driver_uname.setTypeface(custom_font);
        delete_pic_up.setTypeface(custom_font);

        return obj2;
    }
    ProgressDialog progressDialog;
    public void serverData(String id){
        progressDialog = new ProgressDialog(cnt);
        progressDialog.setMessage("Loading....");
        progressDialog.show();
        ApiService service = RetroClient.getRetrofitInstance().create(ApiService.class);
        Call<ResponseData> call = service.deletepicup(id);
        call.enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                progressDialog.dismiss();
                if(response.body()==null){
                    Toast.makeText(cnt,"Server issue",Toast.LENGTH_SHORT).show();
                }else {
                    cnt.startActivity(new Intent(cnt, DriverDashboardActivity.class));
                    Toast.makeText(cnt,"Pickup Deleted successfully",Toast.LENGTH_SHORT).show();
                    ((Activity)cnt).finish();

                }
            }
            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(cnt, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}