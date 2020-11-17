package com.suchalak.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.suchalak.R;
import com.suchalak.activities.DriverDetailsActivity;
import com.suchalak.model.DriversLoc;
import com.suchalak.model.UserPickupsPojo;

import java.util.List;


public class DriverDetailsAdapter extends BaseAdapter {
    List<DriversLoc> ar;
    Context cnt;

    public DriverDetailsAdapter(List<DriversLoc> ar, Context cnt) {
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
        View obj2 = obj1.inflate(R.layout.row_driver_details, null);
        final DriversLoc driver = ar.get(pos);
        TextView tv_name = (TextView) obj2.findViewById(R.id.tv_name);
        tv_name.setText("Name  :" + ar.get(pos).getFirstname());

        TextView tv_rating = (TextView) obj2.findViewById(R.id.tv_rating);
        tv_rating.setText("Rating  :" + ar.get(pos).getRating());

        TextView tv_hours_charges = (TextView) obj2.findViewById(R.id.tv_hours_charges);
        tv_hours_charges.setText("Hourly Charges : "+ar.get(pos).getHours_charges());

        TextView tv_more = (TextView) obj2.findViewById(R.id.tv_more);
        tv_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(cnt, DriverDetailsActivity.class);
                intent.putExtra("driver", driver);
                cnt.startActivity(intent);
            }
        });

        return obj2;
    }
}