package com.example.shrikrishna.coep;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.solver.widgets.Snapshot;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.AvoidType;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Route;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.SphericalUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    boolean bb=false;
    BitmapDescriptor icon123;
    List<MarkerOptions> ll=new ArrayList<MarkerOptions>();
    List<Marker> selected=new ArrayList<Marker>();
    List<Polyline> lines=new ArrayList<Polyline>();
    List<Double> dis=new ArrayList<Double>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
         icon123= BitmapDescriptorFactory.fromResource(R.drawable.unselected);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setTrafficEnabled(true);
        googleMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.setMyLocationEnabled(true);
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
/*            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.abc));

            if (!success) {
                Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show();
            }*/
        } catch (Resources.NotFoundException e) {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(18,73);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney").icon(icon123));
        FirebaseDatabase.getInstance().getReference().child("broadcasts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mMap.clear();
                ll.clear();
                for(DataSnapshot ss:dataSnapshot.getChildren())
                {
                    double lat=0;
                    double longi=0;
                    boolean ghyayachakanay=false;
                    String user=ss.getKey();
                    for(DataSnapshot dd:ss.getChildren())
                    {
                        if(dd.getKey().equals("ll"))
                        {
                            lat=Double.parseDouble(dd.getValue().toString());
                        }
                        if(dd.getKey().equals("ll2"))
                        {
                            longi=Double.parseDouble(dd.getValue().toString());
                        }
                        if(dd.getKey().equals("ll2"))
                        {
                            longi=Double.parseDouble(dd.getValue().toString());
                        }
                        if(dd.getKey().equals("timeslot"))
                        {
                            String times=dd.getValue().toString();
                            String a[]=times.split(" ");
                            int i=0;
                            if(a[1]=="PM")
                            {
                                i=Integer.parseInt(a[0]);
                            }else {

                                i=Integer.parseInt(a[0]);
                                Date currentTime = Calendar.getInstance().getTime();
                                int o=currentTime.getHours();

                                    ghyayachakanay=true;
                                    Toast.makeText(MapsActivity.this, "AM "+i, Toast.LENGTH_SHORT).show();

                            }
                        }
                    }
                    if(ghyayachakanay) {
                        Toast.makeText(MapsActivity.this, "marker added", Toast.LENGTH_SHORT).show();
                        LatLng abc = new LatLng(lat, longi);
                        ll.add(new MarkerOptions().position(abc).title(user).icon(icon123));
                    }
                }
                for(MarkerOptions m:ll)
                {
                    mMap.addMarker(m);
                }
                if(ll.size()>0) {
                    LocationManager locationManager = (LocationManager)
                            getSystemService(Context.LOCATION_SERVICE);
                    Criteria criteria = new Criteria();

                    //Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(ll.get(0).getPosition()));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ll.get(0).getPosition(), 12));
                }
                updateui();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                if(bb) {

                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(MapsActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(MapsActivity.this);

                    }
                    builder.setTitle("Pick Up")
                            .setMessage("Are you sure you want to Pick Up?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(MapsActivity.this, "informed", Toast.LENGTH_SHORT).show();
                                    BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.selected);
                                    marker.setIcon(icon);
                                    selected.add(marker);
                                    FirebaseDatabase.getInstance().getReference().child("broadcasts").child(marker.getTitle()).child("user").setValue(FirebaseAuth.getInstance().getCurrentUser().getEmail().replace('.','_'));
                                    FirebaseDatabase.getInstance().getReference().child("broadcasts").child(marker.getTitle()).child("picked").setValue(true);
                                    updateui();

                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    marker.setIcon(icon123);
                                    selected.remove(marker);
                                    updateui();
                                    //mMarker = googleMap.addMarker(markerOptions);
                                    //marker.setIcon();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .show();
                    bb=false;
                }else {
                    bb=true;
                }

                return false;
            }
        });
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, mMap.getMaxZoomLevel()));


    }
    int i=0;
    void updateui()
    {
        if(ll.size()>0) {
            for (Polyline l : lines) {
                l.remove();
            }
            dis.clear();
            FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(MapsActivity.this);
            mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(final Location location) {
                    Double distance = SphericalUtil.computeDistanceBetween(ll.get(0).getPosition(), new LatLng(location.getLatitude(), location.getLongitude()));
                    Toast.makeText(MapsActivity.this, "akash " + distance, Toast.LENGTH_SHORT).show();
                            /*AIzaSyB5Qu6NYr1IH33UUpPv5h-GbWN7kgI4GnY*/
                    final double colat = 18.5294;

                    final double colog = 73.8566;
                    if (selected.size() > 0) {
                        for (i = 0; i < selected.size(); i++) {

                            if (i == 0) {
                                Toast.makeText(MapsActivity.this, "i=0", Toast.LENGTH_SHORT).show();
                                getDirection(location.getLatitude(), location.getLongitude(), selected.get(i).getPosition().latitude, selected.get(i).getPosition().longitude);
                            } else if (i + 1 < selected.size()) {
                                getDirection(selected.get(i).getPosition().latitude, selected.get(i).getPosition().longitude, selected.get(i + 1).getPosition().latitude, selected.get(i + 1).getPosition().longitude);
                            }
                            if (i == selected.size() - 1) {
                                getDirection(selected.get(i).getPosition().latitude, selected.get(i).getPosition().longitude, colat, colog);
                            }
                        }

                    } else {
                        getDirection(location.getLatitude(), location.getLongitude(), colat, colog);
                    }
                    //getDirection(ll.get(0).getPosition().latitude,ll.get(1).getPosition().longitude,ll.get(1).getPosition().latitude,ll.get(1).getPosition().longitude);
                }
            });
        }
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
        urlString.append("&key=AIzaSyB5Qu6NYr1IH33UUpPv5h-GbWN7kgI4GnY");
        return urlString.toString();
    }
    void addlocation()
    {

    }
    private void getDirection(final double sourcelat, final double sourcelog, final double destlat, final double destlog){
        //Getting the URL
        String url = makeURL(sourcelat, sourcelog, destlat, destlog);

        //Showing a dialog till we get the route
        final ProgressDialog loading = ProgressDialog.show(this, "Getting Route", "Please wait...", false, false);

        //Creating a string request
        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        //Calling the method drawPath to draw the path
                        drawPath(response,sourcelat,sourcelog,destlat,destlog);
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
    }
    //The parameter is the server response
    public void drawPath(String  result,double sourcelat, double sourcelog, double destlat, double destlog) {
        //Getting both the coordinates
        LatLng from = new LatLng(sourcelat,sourcelog);
        LatLng to = new LatLng(destlat,destlog);

        //Calculating the distance in meters
        Double distance = SphericalUtil.computeDistanceBetween(from, to);
        dis.add(distance);
        //Displaying the distance
        //Toast.makeText(this,String.valueOf(distance+" Meters"),Toast.LENGTH_SHORT).show();


        try {
            //Parsing json
            final JSONObject json = new JSONObject(result);
            JSONArray routeArray = json.getJSONArray("routes");
            JSONObject routes = routeArray.getJSONObject(0);
            JSONObject overviewPolylines = routes.getJSONObject("overview_polyline");
            String encodedString = overviewPolylines.getString("points");
            Random rand = new Random();
            int r1 = rand.nextInt(255);
            int g = rand.nextInt(255);
            int b = rand.nextInt(255);
            int randomColor = Color.rgb(r1,g,b);
            List<LatLng> list = decodePoly(encodedString);
            Polyline line = mMap.addPolyline(new PolylineOptions()
                    .addAll(list)
                    .width(20)
                    .color(randomColor)
                    .geodesic(true)
            );
            lines.add(line);


        }
        catch (JSONException e) {

        }
    }

    private List<LatLng> decodePoly(String encoded) {
        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng( (((double) lat / 1E5)),
                    (((double) lng / 1E5) ));
            poly.add(p);
        }

        return poly;
    }
}
