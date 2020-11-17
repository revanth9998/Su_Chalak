package com.suchalak.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.suchalak.R;
import com.suchalak.ResponseData;
import com.suchalak.Utils;
import com.suchalak.activities.DriverDashboardActivity;
import com.suchalak.activities.LoginActivity;
import com.suchalak.api.ApiService;
import com.suchalak.api.RetroClient;
import com.suchalak.model.GetAllPickupsPojo;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetAllpickupsAdapter extends BaseAdapter {
    List<GetAllPickupsPojo> ar;
    Context cnt;

    public GetAllpickupsAdapter(List<GetAllPickupsPojo> ar, Context cnt) {
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
        View obj2 = obj1.inflate(R.layout.list_get_all_pickups, null);

        TextView tv_from = (TextView) obj2.findViewById(R.id.tv_from);
        tv_from.setText("Pickup From  :" + ar.get(pos).getPick_from());

        TextView tv_to = (TextView) obj2.findViewById(R.id.tv_to);
        tv_to.setText("To  :" + ar.get(pos).getPick_to());

        TextView tv_uname = (TextView) obj2.findViewById(R.id.tv_uname);
        tv_uname.setText(ar.get(pos).getUname());

        Button btn_accept = (Button) obj2.findViewById(R.id.btn_accept);
        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateStatus(ar.get(pos).getId(),"accept");
            }
        });
        Button btn_reject = (Button) obj2.findViewById(R.id.btn_reject);
        btn_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateStatus(ar.get(pos).getId(),"reject");
            }
        });

        Button btn_location = (Button) obj2.findViewById(R.id.btn_location);
        btn_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = cnt.getSharedPreferences(Utils.SHREF, Context.MODE_PRIVATE);
                String lat=sharedPreferences.getString("lat","-");
                String lng=sharedPreferences.getString("lng","-");
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?saddr="+lat+","+lng+"&daddr="+ar.get(pos).getUser_lat()+","+ar.get(pos).getUser_lng()));
                cnt.startActivity(intent);
                //Toast.makeText(cnt,""+ar.get(pos).getUser_lat(),Toast.LENGTH_SHORT).show();
            }
        });


        Typeface custom_font = Typeface.createFromAsset(cnt.getAssets(), "fonts/Lato-Medium.ttf");
        tv_from.setTypeface(custom_font);
        tv_to.setTypeface(custom_font);
        tv_uname.setTypeface(custom_font);
        return obj2;
    }
    ProgressDialog pd;
    public void updateStatus(String id,String status){
        pd= new ProgressDialog(cnt);
        pd.setTitle("Loading...");
        pd.show();
        ApiService apiService = RetroClient.getRetrofitInstance().create(ApiService.class);
        Call<ResponseData> call = apiService.updatePickupStatus(status,id);
        call.enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                pd.dismiss();
                if (response.body().status.equals("true")) {
                    Toast.makeText(cnt, "Status updated Successfully...", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(cnt, "OOPs,Some thing went wrong...try again.", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(cnt, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
