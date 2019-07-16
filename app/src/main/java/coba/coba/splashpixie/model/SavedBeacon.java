package coba.coba.splashpixie.model;

import android.location.Location;

import org.altbeacon.beacon.Beacon;

public class SavedBeacon {
    public SavedBeacon(Beacon mBeacon, Location mLocation) {
        this.mBeacon = mBeacon;
        this.mLocation = mLocation;
    }

    private Beacon mBeacon;
    private Location mLocation;

    public Location getLocation() {
        return mLocation;
    }

    public void setLocation(Location mLocation) {
        this.mLocation = mLocation;
    }

    public Beacon getBeacon() {
        return mBeacon;
    }

    public void setBeacon(Beacon mBeacon) {
        this.mBeacon = mBeacon;
    }
}
