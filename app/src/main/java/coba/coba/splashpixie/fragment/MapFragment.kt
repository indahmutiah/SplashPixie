package coba.coba.splashpixie.fragment

import android.content.Context
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.GroundOverlayOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.nexenio.bleindoorpositioning.location.Location

import java.util.ArrayList

import coba.coba.splashpixie.R
import coba.coba.splashpixie.activity.HomeActivity
import coba.coba.splashpixie.adapter.BookshelfAdapter
import coba.coba.splashpixie.adapter.DetectedBeaconAdapter
import coba.coba.splashpixie.data.BookshelfRepository
import coba.coba.splashpixie.model.Bookshelf
import coba.coba.splashpixie.model.DetectedBeacon
import coba.coba.splashpixie.model.SavedBeacon
import kotlin.math.min

class MapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMapClickListener, HomeActivity.OnDetectedBeaconsListener, BookshelfAdapter.OnItemClickListener {

    private var mMapView: MapView? = null
    private var mAdapter: DetectedBeaconAdapter? = null
    private val bookshelfList = ArrayList<Bookshelf>()
    private var mGoogleMap: GoogleMap? = null
    private var mActivity: HomeActivity? = null
    private val mLastLocation: Location? = null
    private var mBottomSheet: BottomSheetBehavior<*>? = null

    private val mMarker: Marker? = null

    private var isMapReady: Boolean? = false

    private var lastDetectedBookshelf = HashMap<Int, DetectedBeacon>()
    private val isSubmitted = false

    private var markers = HashMap<Int, Marker>()

    private var positionViewHolder: PositionViewHolder? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mActivity = activity as HomeActivity?
        if (mActivity != null) {
            mActivity!!.setOnDetectedBeaconsListener(this)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mMapView = view.findViewById(R.id.mapview)
        mMapView!!.onCreate(savedInstanceState)
        val listBookself = view.findViewById<RecyclerView>(R.id.recyclerView)
        listBookself.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        mAdapter = DetectedBeaconAdapter(requireContext())
        mBottomSheet = BottomSheetBehavior.from(view.findViewById<View>(R.id.bottom_sheet))
        positionViewHolder = PositionViewHolder(view)
        listBookself.adapter = mAdapter
        mAdapter?.submitBookshelf(BookshelfRepository.bookshelfList)
        mAdapter!!.setOnItemClickListener { bookshelf ->
            mBottomSheet!!.state = BottomSheetBehavior.STATE_HALF_EXPANDED
            mGoogleMap?.moveCamera(CameraUpdateFactory.newLatLng(bookshelf.bookshelf!!.latLng))
            mGoogleMap!!.addMarker(MarkerOptions().position(bookshelf.bookshelf!!.latLng))
//            Toast.makeText(requireContext(), bookshelf.name, Toast.LENGTH_SHORT).show()

//            mGoogleMap!!.addMarker(MarkerOptions().position(bookshelf.bookshelf.latLng))
//            mMarker?.remove()
        }
        mAdapter!!.setOnBookshelClickListener {bookshelf ->
            mGoogleMap!!.addMarker(MarkerOptions().position(bookshelf!!.latLng))
        }
        //        initList();
        mMapView!!.getMapAsync(this)
    }


    override fun onResume() {
        super.onResume()
        mMapView!!.onResume()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mMapView!!.onLowMemory()
    }

    override fun onPause() {
        mMapView!!.onPause()
        super.onPause()
    }

    override fun onStop() {
        mMapView!!.onStop()
        super.onStop()
    }

    override fun onDestroy() {
        mMapView!!.onDestroy()
        super.onDestroy()

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mGoogleMap = googleMap
        isMapReady = true

        // Add a marker in Sydney and move the camera
        val defaultCoor = LatLng(-6.97172472115997, 107.63273477554321)
        //        mMarker = googleMap.addMarker(new MarkerOptions().position(defaultCoor).title("Marker in Open Library"));

        val cameraUpdate = CameraUpdateFactory
                .newCameraPosition(
                        CameraPosition.builder().bearing(128f).zoom(20f).target(defaultCoor).build()
                )
        googleMap.moveCamera(cameraUpdate)

        googleMap.setMinZoomPreference(19.0f)

        val groundOverlayOption = GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.map_oplib))
                .position(LatLng(-6.971734, 107.632741), 100f, 100f)

        googleMap.addGroundOverlay(groundOverlayOption)
        googleMap.mapType = GoogleMap.MAP_TYPE_NONE

        googleMap.setOnMapClickListener(this)
        googleMap.setOnCameraMoveListener { Log.d("MapMove", googleMap.cameraPosition.toString()) }

        mGoogleMap!!.setLocationSource(mActivity)
        try {
            mGoogleMap!!.isMyLocationEnabled = true
        } catch (ex: SecurityException) {

        }

        BookshelfRepository.bookshelfList.forEach {
            val options = MarkerOptions()
            options.position(LatLng(it.latLng.latitude, it.latLng.longitude))
            options.icon(BitmapDescriptorFactory.fromResource(R.drawable.point))
            val marker = mGoogleMap?.addMarker(options)
            if (marker != null) markers[it.minor] = marker
        }
    }


    override fun onMapClick(latLng: LatLng) {
        //        mGoogleMap.addMarker(new MarkerOptions().position(latLng));
//                Log.d("OnMapClick", latLng.toString());
    }


    override fun onDetectedBeacon(beaconList: List<SavedBeacon>) {
        val bookshelfList = mutableListOf<Bookshelf>()
        beaconList.filter { it.beacon?.id3 != null }.forEach {
            val minor = it.beacon?.id3?.toInt()
            if (minor != null) {
                val bookshelf = BookshelfRepository.bookshelfList.find {
                    it.minor == minor
                }
                if (bookshelf != null) {
                    bookshelfList.add(bookshelf)
                    val detectedBeacon = DetectedBeacon()
                    detectedBeacon.bookshelf = bookshelf
                    detectedBeacon.rssi = it.beacon!!.rssi
                    detectedBeacon.minor = it.beacon!!.id3.toInt()
                    detectedBeacon.major = it.beacon!!.id2.toInt()
                    detectedBeacon.distance = it.beacon!!.distance.toDouble()


                    lastDetectedBookshelf[minor] = detectedBeacon
                }
            }
        }

        mAdapter?.submitList(lastDetectedBookshelf.values
                .toList()
                .filter { it.rssi > -75 }
                .sortedByDescending { it.rssi })

        markers.forEach {
            it.value.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.point))
        }

        lastDetectedBookshelf.values.forEach {
            markers[it.minor]?.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
        }

        lastDetectedBookshelf.values.toList()
                .sortedByDescending { it.rssi }
                .let {
                    if (!it.isEmpty()) {
                        val nearest = it.first()
                        positionViewHolder?.submitDetected(nearest)
                        markers[nearest.minor]?.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                    }
                }

//        val listMarker = arrayListOf<Marker>()
//        beaconList.forEach {
//            val location = it.location
//            if (location != null) {
//                val latLng = LatLng(location.latitude, location.longitude)
//                val options = MarkerOptions().position(latLng)
//                val marker = mGoogleMap?.addMarker(options.icon(BitmapDescriptorFactory.fromResource(R.drawable.posisi)))
//                if (marker != null) {
//                    marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
//                    listMarker.add(marker)
//                }
//
//            }
//        }
//        markers = listMarker

        //        if (!isMapReady) return;
        //        for (int i = 0; i<beaconList.size(); i++) {
        //            SavedBeacon savedBeacon = beaconList.get(i);
        //            LatLng latLng = new LatLng(
        //                    savedBeacon.getLocation().getLatitude(),
        //                    savedBeacon.getLocation().getLongitude()
        //            );
//        //
//                    Beacon beacon = savedBeacon.getBeacon();
//                    if (beacon.getRssi()>-75) {
//                        MarkerOptions options = new MarkerOptions().position(latLng).title(String.valueOf(bookshelfList))
//                        Marker marker = mGoogleMap.addMarker(options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)))
//                        marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
//                    }
        //            else{
        //                mMarker.remove();
        //            }
        //        }
    }


    override fun onItemClick(bookshelf: Bookshelf) {
        Toast.makeText(requireContext(), bookshelf.name, Toast.LENGTH_SHORT).show()
    }

    class PositionViewHolder(val item: View) {
        val name: TextView = item.findViewById(R.id.name_bookself)
        val rssi: TextView = item.findViewById(R.id.rssi_text)
        val distance : TextView = item.findViewById(R.id.distance_text)
        val minor : TextView = item.findViewById(R.id.minor_text)

        fun submitDetected(detectedBeacon: DetectedBeacon) {
            name.text = detectedBeacon.bookshelf?.name
            rssi.text = detectedBeacon.rssi.toString()
            distance.text = getDistance(detectedBeacon.rssi).toString()
            minor.text = detectedBeacon.minor.toString()
        }

        fun getDistance(rssi: Int): Double {

            return if (rssi >= -65) 1.0
            else if (rssi >= -73) 2.0
            else if (rssi >= -75) 3.0
            else if(rssi >= -80) 4.0
            else if(rssi >= -98) 5.0

            else -1.0
        }
    }
}
