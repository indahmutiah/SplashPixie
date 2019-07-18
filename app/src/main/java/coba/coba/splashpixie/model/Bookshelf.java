package coba.coba.splashpixie.model;

import com.google.android.gms.maps.model.LatLng;

public class Bookshelf {

    public Bookshelf(String name, String code, int minor) {
        this.name = name;
        this.code = code;
        this.minor = minor;
    }

    public Bookshelf(String name, String code, LatLng latLng, int minor) {
        this.name = name;
        this.code = code;
        this.latLng = latLng;
        this.minor = minor;
    }

    public String name;
    public String code;
    public LatLng latLng;
    public int minor;
}
