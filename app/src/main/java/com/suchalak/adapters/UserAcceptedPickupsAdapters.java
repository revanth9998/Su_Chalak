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
import com.suchalak.R;
import com.suchalak.model.UserPickupsPojo;

import java.util.List;
public class UserAcceptedPickupsAdapters extends BaseAdapter {
    List<UserPickupsPojo> ar;
    Context cnt;

    public UserAcceptedPickupsAdapters(List<UserPickupsPojo> ar, Context cnt) {
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
        View obj2 = obj1.inflate(R.layout.row_user_pickups, null);

        TextView tv_from = (TextView) obj2.findViewById(R.id.tv_from);
        tv_from.setText("Pickup From  :" + ar.get(pos).getPick_from());

        TextView tv_to = (TextView) obj2.findViewById(R.id.tv_to);
        tv_to.setText("To  :" + ar.get(pos).getPick_to());

        TextView tv_driver_uname = (TextView) obj2.findViewById(R.id.tv_driver_uname);
        tv_driver_uname.setText("Driver Username : "+ar.get(pos).getDriver_uname());

        TextView tv_status = (TextView) obj2.findViewById(R.id.tv_status);
        tv_status.setText("Status : "+ar.get(pos).getStatus());

        Typeface custom_font = Typeface.createFromAsset(cnt.getAssets(), "fonts/Lato-Medium.ttf");
        tv_from.setTypeface(custom_font);
        tv_to.setTypeface(custom_font);
        tv_driver_uname.setTypeface(custom_font);

        return obj2;
    }
}