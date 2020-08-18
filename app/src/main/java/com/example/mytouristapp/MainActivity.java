package com.example.mytouristapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private GridView _gridview ;
    private GridViewAdapter _gridviewadapter ;
    private ArrayList<Landmark> _mLandmark ;
    private String [] URL_Api = {"https://api.mapbox.com/directions/v5/mapbox/cycling/106.6822%2C10.7629%3B",
            "?alternatives=true&geometries=polyline6&steps=true&access_token=pk.eyJ1IjoiaXRydW9uZ2hhaSIsImEiOiJja2R6dG5hMHUxYnlsMnludzVrdjVjOTlzIn0.UefUgb1EkM5o15nv2XQukg"};
    private GridView.OnItemClickListener _gridviewItemonclick = new GridView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent toMap = new Intent(MainActivity.this, MapsActivity.class) ;
            Landmark poslandmark = _mLandmark.get(i) ;
            toMap.putExtra("Name", poslandmark.get_name()) ;
            toMap.putExtra("Description", poslandmark.get_description()) ;
            toMap.putExtra("Logoid", poslandmark.get_imgid()) ;
            toMap.putExtra("Latitude", poslandmark.get_latlng().latitude) ;
            toMap.putExtra("Longitude", poslandmark.get_latlng().longitude) ;

            toMap.putExtra("URL_API", URL_Api[0] + String.valueOf(poslandmark.get_latlng().longitude)+"%2C"+String.valueOf(poslandmark.get_latlng().latitude)
                                        +URL_Api[1]) ;

            startActivity(toMap) ;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loaddata() ;
        initComponent() ; 
    }

    private void initComponent() {
        _gridview = findViewById(R.id.gridview_places) ;
        _gridviewadapter = new GridViewAdapter(this, R.layout.gridviewitem, _mLandmark) ; 
        _gridview.setAdapter(_gridviewadapter);
        _gridview.setOnItemClickListener(_gridviewItemonclick);
    }

    private void loaddata() {
        _mLandmark = new ArrayList<>() ;
        _mLandmark.add(new Landmark("Bến Nhà Rồng",
                "Nơi Bác Hồ ra đi tìm đường cứu nước năm 1911",
                R.drawable.logo_ben_nha_rong, new LatLng(10.768313, 106.706793))) ;
        _mLandmark.add(new Landmark("Chợ Bến Thành",
                "Địa danh nổi tiếng qua các thời kỳ của Sài Gòn",
                R.drawable.logo_cho_ben_thanh, new LatLng(10.771423, 106.698471))) ;
        _mLandmark.add(new Landmark("Nhà thờ Đức Bà",
                "Công trình kiến trúc độc đáo, nét đặc trưng của Sài Gòn",
                R.drawable.logo_nha_tho_duc_ba, new LatLng(10.779742, 106.699188))) ;
    }
    
}