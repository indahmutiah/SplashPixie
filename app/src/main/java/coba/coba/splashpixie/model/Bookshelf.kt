package coba.coba.splashpixie.model

import com.google.android.gms.maps.model.LatLng

data class Bookshelf(
        var name: String,
        var code: String,
        var latLng: LatLng,
        var minor: Int = 0
)
