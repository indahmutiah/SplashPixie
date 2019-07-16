package coba.coba.splashpixie.model;

import com.google.android.gms.maps.model.LatLng;

public class Bookshelf {
    public Bookshelf(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public Bookshelf(String name, String code, LatLng latLng) {
        this.name = name;
        this.code = code;
        this.latLng = latLng;
    }

    public String name;
    public String code;
    public LatLng latLng;
}
