package com.example.shrikrishna.coep;

import android.*;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    Button pick;
    TextView tt;
    Handler h;
    int ii=0;
    String time,clg;
    boolean ee=false;
    SeekBar seekrad,seektime;
    TextView radtext,timetext;
    LinearLayout ll;
    private FusedLocationProviderClient mFusedLocationClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pick=(Button)findViewById(R.id.viewpickup);
        tt=(TextView)findViewById(R.id.timertext);
        radtext=(TextView)findViewById(R.id.radtext);
        ll=(LinearLayout)findViewById(R.id.mylinear);
        timetext=(TextView)findViewById(R.id.timett);
        seekrad=(SeekBar) findViewById(R.id.seek123);
        seektime=(SeekBar) findViewById(R.id.seektime);
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Please Turn On GPS");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Intent gpsOptionsIntent = new Intent(
                            android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(gpsOptionsIntent);
                }
            });
            builder.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            builder.show();
        }


        seekrad.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                radtext.setText(i+" KM");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seektime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                timetext.setText(i+" mins");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        SharedPreferences prefs = getSharedPreferences("COEP", Context.MODE_PRIVATE);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);
        final boolean myVariable = prefs.getBoolean("vehicle", false);
        time=prefs.getString("time","8 AM");
        clg=prefs.getString("clg","COEP");
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.INTERNET}, 1255);
            }
        }
        h=new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
             if(ee) {
                 if (ii < 15) {
                     ii++;
                     tt.setText("Requesting For Pick UP :"+ii);
                 }else {
                     tt.setText("No Pickups Available");
                 }
             }
                 h.postDelayed(this,1000);
            }
        },1000);
        if(!myVariable)
        {
            tt.setVisibility(View.VISIBLE);
            pick.setText("Request For Pick UP");
            FirebaseDatabase.getInstance().getReference().child("broadcasts").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot dd:dataSnapshot.getChildren())
                    {
                        if(dd.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail().replace('.','_'))) {
                            boolean b=false;
                            String user="";
                            for (DataSnapshot ds : dd.getChildren()) {
                                if (ds.getKey().equals("picked"))
                                {
                                    b=(boolean)ds.getValue();
                                }
                                if (ds.getKey().equals("user"))
                                {
                                    user=ds.getValue().toString();
                                }

                            }
                            if(b==true)
                            {
                                tt.setText("Hey ! "+user+" will Pick You Up From Your Location Soon");
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }else {
            ll.setVisibility(View.VISIBLE);
        }
        pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(myVariable) {
                    startActivity(new Intent(MainActivity.this, MapsActivity.class));
                }else {

                    //FirebaseDatabase.getInstance().getReference().child("aaa").push().setValue(new UserLoc(" "," ","abcd"));

                        mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                /*AIzaSyB5Qu6NYr1IH33UUpPv5h-GbWN7kgI4GnY*/
                                double lat=location.getLatitude();
                                double longi=location.getLongitude();
                                String s= FirebaseAuth.getInstance().getCurrentUser().getEmail().replace('.','_');
                                FirebaseDatabase.getInstance().getReference().child("broadcasts").child(s).setValue(new MyL(""+lat,""+longi,s,time,clg));
                                //String s=
                                Toast.makeText(MainActivity.this, lat+"-"+longi, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                }
            }
        );

    }
    public String makeURL (double sourcelat, double sourcelog, double destlat, double destlog ){
        StringBuilder urlString = new StringBuilder();
        urlString.append("https://maps.googleapis.com/maps/api/directions/json");
        urlString.append("?origin=");// from
        urlString.append(Double.toString(sourcelat));
        urlString.append(",");
        urlString
                .append(Double.toString( sourcelog));
        urlString.append("&destination=");// to
        urlString
                .append(Double.toString( destlat));
        urlString.append(",");
        urlString.append(Double.toString(destlog));
        urlString.append("&sensor=false&mode=driving&alternatives=true");
        urlString.append("&key=SERVER-KEY");
        return urlString.toString();
    }
/*    private void getDirection(){
        //Getting the URL
        String url = makeURL(fromLatitude, fromLongitude, toLatitude, toLongitude);

        //Showing a dialog till we get the route
        final ProgressDialog loading = ProgressDialog.show(this, "Getting Route", "Please wait...", false, false);

        //Creating a string request
        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>().Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        //Calling the method drawPath to draw the path
                        //drawPath(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                    }
                });

        //Adding the request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }*/
}
