package com.example.mytouristapp;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.AsyncQueryHandler;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Landmark _mLandmark_Item;
    private Marker mMarker;
    boolean isinAsynctask = false ;
    ArrayList<Polyline> ListPolyline = new ArrayList<>() ;
    private ArrayList Location = new ArrayList();
    private String URL_direction ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        loaddata();

    }

    private void loaddata() {
        Intent intent = getIntent();
        _mLandmark_Item = new Landmark(intent.getStringExtra("Name"),
                intent.getStringExtra("Description"),
                intent.getIntExtra("Logoid", 0),
                new LatLng(intent.getDoubleExtra("Latitude", 0),
                        intent.getDoubleExtra("Longitude", 0)));
        URL_direction = intent.getStringExtra("URL_API");
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
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        }  else {
        ActivityCompat.requestPermissions(this, new String[] {
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION },
                0);
    }

        displayMarker() ;

    }

    private void displayMarker() {
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), _mLandmark_Item.get_imgid()) ;
        bmp = Bitmap.createScaledBitmap(bmp, bmp.getWidth()/4 , bmp.getHeight()/4,false) ;
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bmp) ;
        mMarker = mMap.addMarker(new MarkerOptions().position(_mLandmark_Item.get_latlng()).title(_mLandmark_Item.get_name())
                                .snippet(_mLandmark_Item.get_description())
                                .icon(bitmapDescriptor)) ;
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(_mLandmark_Item.get_latlng())     // Sets the center of the map to Mountain View
                .zoom(15)                           // Sets the zoom
                .bearing(90)                        // Sets the orientation of the camera to east
                .tilt(30)                           // Sets the tilt of the camera to 30 degrees
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
    public void btn_direct_onclick(View view) {
        if (isinAsynctask == false){
        MyDirection myDirection = new MyDirection();
        myDirection.execute() ;
        }
    }

    public void btn_remove_onclick(View view) {
        for (Polyline i : ListPolyline)
            i.remove() ;
        ListPolyline.clear();
    }
    private void drawdirection(ArrayList Location){
        PolylineOptions polylineOptions = new PolylineOptions() ;
        polylineOptions.addAll(Location) ;
        polylineOptions.width(12);
        polylineOptions.color(Color.RED);
        polylineOptions.geodesic(true);


        Polyline polyline = mMap.addPolyline(polylineOptions);
        ListPolyline.add(polyline);

    }
    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
    private class MyDirection extends AsyncTask<Void, Void, ArrayList >{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Location.clear();
        }

        @Override
        protected ArrayList doInBackground(Void... voids) {
            String inline = "" ;
            URL url = null ;
            isinAsynctask = true ;
            if (isNetworkConnected()) {
                try {
                    url = new URL(URL_direction);
                    HttpURLConnection conn = null;
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.connect();
                    Scanner sc = null;
                    sc = new Scanner(url.openStream());
                    while (sc.hasNext())
                        inline += sc.nextLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            try {
                JSONObject Jinline = new JSONObject(inline);
                JSONArray jsonarray_route = Jinline.getJSONArray("routes") ;
                JSONArray jsonArray_legs = jsonarray_route.getJSONObject(0).getJSONArray("legs");
                JSONArray jsonArray_steps = jsonArray_legs.getJSONObject(0).getJSONArray("steps")  ;



                for (int i = 0 ; i < jsonArray_steps.length(); i++){
                    JSONArray jsonArray_intersection = jsonArray_steps.getJSONObject(i).getJSONArray("intersections") ;
                    JSONArray jsonArray_location = jsonArray_intersection.getJSONObject(0).getJSONArray("location");
                    String lng = jsonArray_location.getString(0) ;
                    String lat = jsonArray_location.getString(1) ;
                    Location.add(new LatLng(Double.valueOf(lat), Double.valueOf(lng)));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }



            return Location ;
        }

        @Override
        protected void onPostExecute(ArrayList arrayList) {
            super.onPostExecute(arrayList);
            drawdirection(arrayList);
            isinAsynctask = false ;
        }
    }

}