package com.suchalak.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.SphericalUtil;
import com.suchalak.R;
import com.suchalak.Utils;
import com.suchalak.adapters.GetAllpickupsAdapter;
import com.suchalak.api.ApiService;
import com.suchalak.api.RetroClient;
import com.suchalak.model.DriversLoc;
import com.suchalak.model.GetAllPickupsPojo;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    List<DriversLoc> listDriverLoc;
    LatLng Hyd = new LatLng(17.287709, 78.49614);
    LatLng Vij = new LatLng(17.849592, 79.115166);
    LatLng Medchal = new LatLng(18.25022, 78.899431);
    LatLng unko = new LatLng(17.905569, 77.840366);

    double my_lat=17.361719,my_lng=78.475166;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SharedPreferences sharedPreferences = getSharedPreferences(Utils.SHREF, Context.MODE_PRIVATE);
        my_lat =Double.parseDouble(sharedPreferences.getString("lat","0"));
        my_lng =Double.parseDouble(sharedPreferences.getString("lng","0"));
        //Toast.makeText(getApplicationContext(),""+sharedPreferences.getString("lat","0"),Toast.LENGTH_SHORT).show();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

       // arrayList.add(new DriversLoc(new LatLng(17.287709, 78.49614),""));
        //arrayList.add(Hyd);
        //arrayList.add(Vij);
        //arrayList.add(Medchal);
       // arrayList.add(unko);

    }
    ProgressDialog progressDialog;
    public void serverData(){
        progressDialog = new ProgressDialog(MapsActivity.this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();

        ApiService service = RetroClient.getRetrofitInstance().create(ApiService.class);
        Call<List<DriversLoc>> call = service.getDriverDetails(""+my_lat,""+my_lng);
        call.enqueue(new Callback<List<DriversLoc>>() {
            @Override
            public void onResponse(Call<List<DriversLoc>> call, Response<List<DriversLoc>> response) {
                progressDialog.dismiss();
                if(response.body()==null){
                    Toast.makeText(MapsActivity.this,"No data found",Toast.LENGTH_SHORT).show();
                }else {
                    listDriverLoc = response.body();
                    if(listDriverLoc!=null){
                        //Toast.makeText(MapsActivity.this,""+listDriverLoc.size(),Toast.LENGTH_SHORT).show();
                       if(listDriverLoc.size()>0){
                           for(int i=0;i<listDriverLoc.size();i++){
                              // if( getDistance(new LatLng(my_lat,my_lng),new LatLng(Double.parseDouble(listDriverLoc.get(i).getLat()),Double.parseDouble(listDriverLoc.get(i).getLat())))<100){
                                   Marker m=mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(listDriverLoc.get(i).getLat()),Double.parseDouble(listDriverLoc.get(i).getLng()))).title(listDriverLoc.get(i).getFirstname()));
                                   m.setTag(listDriverLoc.get(i).getUid());
                              // }

                           }
                           mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(my_lat,my_lng)));
                           mMap.animateCamera( CameraUpdateFactory.zoomTo( 10.0f ) );
                       }

                    }
                    //list_view.setAdapter(new GetAllpickupsAdapter(getAllPickupsPojos, GetAllPickupsActivity.this));
                }
            }
            @Override
            public void onFailure(Call<List<DriversLoc>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(MapsActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    DriversLoc dl;
    DriversLoc selDLoc,duname;
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
       Marker mr= mMap.addMarker(new MarkerOptions().position(new LatLng(my_lat,my_lng)).title("My Location")
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
       mr.setTag("me");
        serverData();

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                //int position = (int)(marker.getTag());
                if(marker.getTag()!=null) {
                    for (int i = 0; i < listDriverLoc.size(); i++) {
                        if (listDriverLoc.get(i).getUid().equals("" + marker.getTag())) {
                             //Toast.makeText(getApplicationContext(),""+listDriverLoc.get(i).getEmail(),Toast.LENGTH_SHORT).show();
                            selDLoc = listDriverLoc.get(i);
                            break;
                        }
                    }
                }
                /*Intent intent=new Intent(getApplicationContext(),AddPickUpDetailsActivity.class);
                intent.putExtra("driver_uname",selDLoc.getEmail());
                intent.putExtra("user_lat",""+my_lat);
                intent.putExtra("user_lng",""+my_lng);
                startActivity(intent);*/
                Intent intent = new Intent(getApplicationContext(), DriverDetailsActivity.class);
                intent.putExtra("driver", selDLoc);
                startActivity(intent);
                return false;
            }
        });

    }

    public double getDistance(LatLng my_latlong, LatLng frnd_latlong) {
        double d=SphericalUtil.computeDistanceBetween(my_latlong, frnd_latlong);
        Location l1 = new Location("One");
        l1.setLatitude(my_latlong.latitude);
        l1.setLongitude(my_latlong.longitude);

        Location l2 = new Location("Two");
        l2.setLatitude(frnd_latlong.latitude);
        l2.setLongitude(frnd_latlong.longitude);

        float distance = l1.distanceTo(l2);
        float dist=0;

        if (distance > 1000.0f) {
            distance = distance / 1000.0f;
            dist = distance ;
        }
        Toast.makeText(getApplicationContext(),""+(d/1000),Toast.LENGTH_SHORT).show();

        return d;
    }


}