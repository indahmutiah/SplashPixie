package coba.coba.splashpixie.activity;

import android.location.Location;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.google.android.gms.maps.LocationSource;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import coba.coba.splashpixie.R;
import coba.coba.splashpixie.fragment.MapFragment;
import coba.coba.splashpixie.fragment.ScanningFragment;
import coba.coba.splashpixie.model.SavedBeacon;


public class HomeActivity extends AppCompatActivity implements BeaconConsumer, LocationSource {

    private ActionBar toolbar;
    private BeaconManager mBeaconManager;
    OnLocationChangedListener onLocationChangedListener;

    public void setOnDetectedBeaconsListener(OnDetectedBeaconsListener onDetectedBeaconsListener) {
        this.onDetectedBeaconsListener = onDetectedBeaconsListener;
    }

    OnDetectedBeaconsListener onDetectedBeaconsListener;

    private static String LAYOUT_IBEACON = "m:0-3=4c000215,i:4-19,i:20-21,i:22-23,p:24-24";

    private MapFragment mapFragment = new MapFragment();
    private ScanningFragment scanningFragment = new ScanningFragment();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        toolbar = getSupportActionBar();
        mBeaconManager = BeaconManager.getInstanceForApplication(this);
        mBeaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(LAYOUT_IBEACON));
        mBeaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(BeaconParser.ALTBEACON_LAYOUT));
        mBeaconManager.bind(this);
        toolbar.setTitle("Map");
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, mapFragment)
                .commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
//        AndroidLocationProvider.stopRequestingLocationUpdates();
//        BluetoothClient.stopScanning();
        super.onPause();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.map:
                    toolbar.setTitle("Map");
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.container, mapFragment)
                            .commit();

                    return true;
                case R.id.ble:
                    toolbar.setTitle("Bluetooth Scanning");
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.container, scanningFragment)
                            .commit();
                    return true;
            }
            return false;
        }
    };

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBeaconManager.unbind(this);
    }

    @Override
    public void onBeaconServiceConnect() {
        mBeaconManager.removeAllMonitorNotifiers();
        mBeaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> collection, Region region) {
                ArrayList<Beacon> list = new ArrayList(collection);
                if (region.getUniqueId().equals("openlibrary")) {
                    processBeacon(filterBeacons(list));
                }
            }
        });

        try {
            UUID uuid = UUID.fromString("cde5defff-7027-76b9-fe46-1705529b7b41");
            mBeaconManager.startRangingBeaconsInRegion(
                    new Region(
                            "openlibrary",
                            Identifier.fromUuid(uuid),
                            Identifier.fromInt(100),
                            null)
            );
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    private ArrayList<Beacon> filterBeacons(ArrayList<Beacon> list) {
        ArrayList<Beacon> newList = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            Beacon beacon = list.get(i);
            String uuid = beacon.getId1().toString();
            if (uuid.equals("cde5deff-7027-76b9-fe46-1705529b7b41")) {
                if (beacon.getId2().toInt() == 100) {
                    if (beacon.getRssi() > -75 ){
                        newList.add(beacon);
                    }
                }
            }
        }
        return newList;
    }

    /**
     * Process scanned beacon
     * @param beaconList
     */
    private void processBeacon(List<Beacon> beaconList) {
        Collections.sort(beaconList, new Comparator<Beacon>() {
            @Override
            public int compare(Beacon left, Beacon right) {
                return Double.compare(left.getDistance(), right.getDistance());
            }
        });
        if (onDetectedBeaconsListener != null) {
            ArrayList<SavedBeacon> savedBeaconList = new ArrayList<>();
            for (int i = 0; i<beaconList.size(); i++) {
                Beacon beacon = beaconList.get(i);
                Location location = findLocationFromBeacon(beacon.getId3().toString());
                SavedBeacon savedBeacon = new SavedBeacon(beacon, location);
                savedBeaconList.add(savedBeacon);
            }
            onDetectedBeaconsListener.onDetectedBeacon(savedBeaconList);
        }


    }

    private Location findLocationFromBeacon(String minor) {
        Location location = new Location("");
        if (minor.equals("7")) {
            location.setLatitude(-6.9715663096882885);
            location.setLongitude(107.63247661292552);
//            mapFragment.updateLocation(new LatLng(-6.971649508997313, 107.63257451355457));

            if (onLocationChangedListener != null) {
                onLocationChangedListener.onLocationChanged(location);
            }

        }
        if (minor.equals("10")) {
            location.setLatitude(-6.971748349757242);
            location.setLongitude(107.63247359544039);
//            mapFragment.u pdateLocation(new LatLng(-6.971745354582996, 107.63247694820166));
            if (onLocationChangedListener != null) {
                onLocationChangedListener.onLocationChanged(location);
            }
        }
        if (minor.equals("3")) {
            location.setLatitude(-6.971506406176628);
            location.setLongitude(107.6327284052968);
//            mapFragment.updateLocation(new LatLng(-6.971753341714249, 107.63253796845675));
            if (onLocationChangedListener != null) {
                onLocationChangedListener.onLocationChanged(location);
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
//        if (minor.equals("6")) {
//            location.setLatitude(-6.971632869136695);
//            location.setLongitude(107.6328967139125);
////            mapFragment.updateLocation(new LatLng(-6.97165649773861, 107.63278003782034));
//            if (onLocationChangedListener != null) {
//                onLocationChangedListener.onLocationChanged(location);
//            }
//        }
        if (minor.equals("4")) {
            location.setLatitude(-6.971694103820868);
            location.setLongitude(107.63284642249347);
//            mapFragment.updateLocation(new LatLng(-6.971694103820868, 107.63284642249347));
            if (onLocationChangedListener != null) {
                onLocationChangedListener.onLocationChanged(location);
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
        if (minor.equals("5")) {
            location.setLatitude(-6.971999611527834);
            location.setLongitude(107.63278372585773);
//            mapFragment.updateLocation(new LatLng(-6.97152371163634, 107.63278372585773));
            if (onLocationChangedListener != null) {
                onLocationChangedListener.onLocationChanged(location);
            }
        }
        return location;
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        this.onLocationChangedListener = onLocationChangedListener;
    }

    @Override
    public void deactivate() {
        this.onLocationChangedListener = null;
    }

    public interface OnDetectedBeaconsListener {
        void onDetectedBeacon(List<SavedBeacon> beaconList);
    }

}


