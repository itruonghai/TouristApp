package com.example.mytouristapp;

import com.google.android.gms.maps.model.LatLng;

public class Landmark {
    private String _name ;
    private int _imgid ;
    private String _description ;

    private LatLng _latlng  ;
    public Landmark(String _name, String _description, int _imgid, LatLng _latlng){
        this._name = _name ;
        this._imgid = _imgid ;
        this._description = _description ;
        this._latlng = _latlng;
    }
    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }



    public int get_imgid() {
        return _imgid;
    }

    public void set_imgid(int _imgid) {
        this._imgid = _imgid;
    }

    public String get_description() {
        return _description;
    }

    public void set_description(String _description) {
        this._description = _description;
    }
    public LatLng get_latlng() {
        return _latlng;
    }

    public void set_latlng(LatLng _latlng) {
        this._latlng = _latlng;
    }



}
