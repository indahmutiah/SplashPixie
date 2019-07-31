package coba.coba.splashpixie.activity

import android.content.Context
import android.location.Location
import android.os.Bundle
import android.os.RemoteException
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import coba.coba.splashpixie.R
import coba.coba.splashpixie.fragment.MapFragment
import coba.coba.splashpixie.fragment.ScanningFragment
import coba.coba.splashpixie.model.SavedBeacon
import com.google.android.gms.maps.LocationSource
import org.altbeacon.beacon.*
import java.util.*


class HomeActivity : AppCompatActivity(), BeaconConsumer, LocationSource {

    private var toolbar: ActionBar? = null
    private var mBeaconManager: BeaconManager? = null
    internal var onLocationChangedListener: LocationSource.OnLocationChangedListener? = null

    internal var onDetectedBeaconsListener: OnDetectedBeaconsListener? = null

    private val mapFragment = MapFragment()
    private val scanningFragment = ScanningFragment()

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.map -> {
                toolbar!!.title = "Map"
                supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.container, mapFragment)
                        .commit()

                return@OnNavigationItemSelectedListener true
            }
//            R.id.ble -> {
//                toolbar!!.title = "Bluetooth Scanning"
//                supportFragmentManager
//                        .beginTransaction()
//                        .replace(R.id.container, scanningFragment)
//                        .commit()
//                return@OnNavigationItemSelectedListener true
//            }
        }
        false
    }

    fun setOnDetectedBeaconsListener(onDetectedBeaconsListener: OnDetectedBeaconsListener) {
        this.onDetectedBeaconsListener = onDetectedBeaconsListener
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)
        toolbar = supportActionBar
        mBeaconManager = BeaconManager.getInstanceForApplication(this)
        mBeaconManager!!.beaconParsers.add(BeaconParser().setBeaconLayout(LAYOUT_IBEACON))
        mBeaconManager!!.beaconParsers.add(BeaconParser().setBeaconLayout(BeaconParser.ALTBEACON_LAYOUT))
        mBeaconManager!!.bind(this)
        toolbar!!.title = "Map"
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, mapFragment)
                .commit()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        //        AndroidLocationProvider.stopRequestingLocationUpdates();
        //        BluetoothClient.stopScanning();
        super.onPause()
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        mBeaconManager!!.unbind(this)
    }

    override fun onBeaconServiceConnect() {
        mBeaconManager!!.removeAllMonitorNotifiers()
        mBeaconManager!!.addRangeNotifier { collection, region ->
            val list = ArrayList(collection)
            if (region.uniqueId == "openlibrary") {
                processBeacon(filterBeacons(list))
            }
        }

        try {
            val uuid = UUID.fromString("cde5defff-7027-76b9-fe46-1705529b7b41")
            mBeaconManager!!.startRangingBeaconsInRegion(
                    Region(
                            "openlibrary",
                            Identifier.fromUuid(uuid),
                            Identifier.fromInt(100), null)
            )
        } catch (e: RemoteException) {
            e.printStackTrace()
        }

    }

    private fun filterBeacons(list: ArrayList<Beacon>): MutableList<Beacon> {
        val newList = mutableListOf<Beacon>()
        for (i in list.indices) {
            val beacon = list[i]
            val uuid = beacon.id1.toString()
            if (uuid == "cde5deff-7027-76b9-fe46-1705529b7b41") {
                if (beacon.id2.toInt() == 100) {
//                    if (beacon.rssi > -100) {
                        newList.add(beacon)
//                    }
                }
            }
        }
        return newList
    }

    /**
     * Process scanned beacon
     * @param beaconList
     */
    private fun processBeacon(beaconList: List<Beacon>) {
        Collections.sort(beaconList) { left, right -> java.lang.Double.compare(left.distance, right.distance) }
        if (onDetectedBeaconsListener != null) {
            val savedBeaconList = ArrayList<SavedBeacon>()
            for (i in beaconList.indices) {
                val beacon = beaconList[i]
                val location = findLocationFromBeacon(beacon.id3.toString())
                val savedBeacon = SavedBeacon(beacon, location)
                savedBeaconList.add(savedBeacon)
            }
            onDetectedBeaconsListener!!.onDetectedBeacon(savedBeaconList)
        }


    }

    private fun findLocationFromBeacon(minor: String): Location {
        val location = Location("")
        if (minor == "7") {
            location.latitude = -6.9715663096882885
            location.longitude = 107.63247661292552
            //            mapFragment.updateLocation(new LatLng(-6.971649508997313, 107.63257451355457));

            if (onLocationChangedListener != null) {
                onLocationChangedListener!!.onLocationChanged(location)
            }

        }
        if (minor == "10") {
            location.latitude = -6.971748349757242
            location.longitude = 107.63247359544039
            //            mapFragment.u pdateLocation(new LatLng(-6.971745354582996, 107.63247694820166));
            if (onLocationChangedListener != null) {
                onLocationChangedListener!!.onLocationChanged(location)
            }
        }
        if (minor == "3") {
            location.latitude = -6.971506406176628
            location.longitude = 107.6327284052968
            //            mapFragment.updateLocation(new LatLng(-6.971753341714249, 107.63253796845675));
            if (onLocationChangedListener != null) {
                onLocationChangedListener!!.onLocationChanged(location)
            }
        }
        //        if (minor.equals("1")) {
        //            location.setLatitude(-6.971748016960107);
        //            location.setLongitude(107.63247694820166);
        ////            mapFragment.updateLocation(new LatLng(-6.97150074862236, 107.63260468840599));
        //            if (onLocationChangedListener != null) {
        //                onLocationChangedListener.onLocationChanged(location);
        //            }
        //        }
        //        if (minor.equals("2")) {
        //            location.setLatitude(-6.971612901303168);
        //            location.setLongitude(107.63279747217895);
        ////            mapFragment.updateLocation(new LatLng(-6.971605912561222, 107.63275153934956));
        //            if (onLocationChangedListener != null) {
        //                onLocationChangedListener.onLocationChanged(location);
        //            }
        //        }
                if (minor.equals("6")) {
                    location.setLatitude(-6.971632869136695);
                    location.setLongitude(107.6328967139125);
        //            mapFragment.updateLocation(new LatLng(-6.97165649773861, 107.63278003782034))
                    if (onLocationChangedListener != null) {
                        onLocationChangedListener!!.onLocationChanged(location)
                    }
                }
        if (minor == "4") {
            location.latitude = -6.971694103820868
            location.longitude = 107.63284642249347
            //            mapFragment.updateLocation(new LatLng(-6.971694103820868, 107.63284642249347));
            if (onLocationChangedListener != null) {
                onLocationChangedListener!!.onLocationChanged(location)
            }
        }
        //        if (minor.equals("8")) {
        //            location.setLatitude(-6.971840867352016);
        //            location.setLongitude(107.63282865285872);
        ////            mapFragment.updateLocation(new LatLng(-6.971840867352016, 107.63282865285872));
        //            if (onLocationChangedListener != null) {
        //                onLocationChangedListener.onLocationChanged(location);
        //            }
        //        }
        //        if (minor.equals("9")) {
        //
        //            location.setLatitude(-6.971943368838568);
        //            location.setLongitude(107.63283871114254);
        ////            mapFragment.updateLocation(new LatLng(-6.971943368838568, 107.63283871114254));
        //            if (onLocationChangedListener != null) {
        //                onLocationChangedListener.onLocationChanged(location);
        //            }
        //        }
        if (minor == "5") {
            location.latitude = -6.971999611527834
            location.longitude = 107.63278372585773
            //            mapFragment.updateLocation(new LatLng(-6.97152371163634, 107.63278372585773));
            if (onLocationChangedListener != null) {
                onLocationChangedListener!!.onLocationChanged(location)
            }
        }
        return location
    }

    override fun activate(onLocationChangedListener: LocationSource.OnLocationChangedListener) {
        this.onLocationChangedListener = onLocationChangedListener
    }

    override fun deactivate() {
        this.onLocationChangedListener = null
    }

    interface OnDetectedBeaconsListener {
        fun onDetectedBeacon(beaconList: List<SavedBeacon>)
    }

    companion object {

        private val LAYOUT_IBEACON = "m:0-3=4c000215,i:4-19,i:20-21,i:22-23,p:24-24"
    }

}


