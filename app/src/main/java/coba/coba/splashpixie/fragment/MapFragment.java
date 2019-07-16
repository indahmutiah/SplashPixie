package coba.coba.splashpixie.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.lemmingapex.trilateration.NonLinearLeastSquaresSolver;
import com.lemmingapex.trilateration.TrilaterationFunction;
import com.nexenio.bleindoorpositioning.location.Location;

import org.altbeacon.beacon.Beacon;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer;
import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import java.util.ArrayList;
import java.util.List;

import coba.coba.splashpixie.R;
import coba.coba.splashpixie.activity.HomeActivity;
import coba.coba.splashpixie.adapter.BookshelfAdapter;
import coba.coba.splashpixie.model.Bookshelf;
import coba.coba.splashpixie.model.SavedBeacon;

public class MapFragment extends Fragment implements
        OnMapReadyCallback, GoogleMap.OnMapClickListener, HomeActivity.OnDetectedBeaconsListener,
        BookshelfAdapter.OnItemClickListener {

    private MapView mMapView;
    private BookshelfAdapter mAdapter;
    private List<Bookshelf> bookshelfList = new ArrayList<>();
    private GoogleMap mGoogleMap;
    private HomeActivity mActivity;
    private Location mLastLocation;
    private BottomSheetBehavior mBottomSheet;

    private Marker mMarker;

    private Boolean isMapReady = false;

    private boolean isSubmitted = false;

//    @Override
//    protected LocationListener createDeviceLocationListener() {
//        return new LocationListener() {
//            @Override
//            public void onLocationUpdated(LocationProvider locationProvider, Location location) {
//                if (locationProvider instanceof IndoorPositioning) {
//                    updateLocation(IndoorPositioning.getLocationPredictor().getLocation());
//                }
//            }
//        };
//    }
//
//    @Override
//    protected BeaconUpdateListener createBeaconUpdateListener() {
//        return new BeaconUpdateListener() {
//            @Override
//            public void onBeaconUpdated(Beacon beacon) {
//                Log.i("BeaconUpdate", beacon.toString());
//            }
//        };
//    }

//    private void updateLocation(Location location) {
//
//    }
//
//    @Override
//    protected int getLayoutResourceId() {
//        return R.layout.fragment_map;
//    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (HomeActivity) getActivity();
        if (mActivity != null) {
            mActivity.setOnDetectedBeaconsListener(this);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMapView = view.findViewById(R.id.mapview);
        mMapView.onCreate(savedInstanceState);
        RecyclerView listBookself = view.findViewById(R.id.listBookshelf);
        listBookself.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        mAdapter = new BookshelfAdapter(getContext());
        mBottomSheet = BottomSheetBehavior.from(view.findViewById(R.id.bottom_sheet));
        listBookself.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BookshelfAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Bookshelf bookshelf) {
                mBottomSheet.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
                Toast.makeText(requireContext(), bookshelf.name, Toast.LENGTH_SHORT).show();
                mGoogleMap.addMarker(new MarkerOptions().position(bookshelf.latLng));
                if(mMarker!= null){
                    mMarker.remove();
                }
            }
        });
//        initList();
        mMapView.getMapAsync(this);
    }


    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onStop() {
        mMapView.onStop();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();

    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mGoogleMap = googleMap;
        isMapReady = true;

        // Add a marker in Sydney and move the camera
        LatLng defaultCoor = new LatLng(-6.97172472115997,107.63273477554321);
//        mMarker = googleMap.addMarker(new MarkerOptions().position(defaultCoor).title("Marker in Open Library"));

        CameraUpdate cameraUpdate = CameraUpdateFactory
                .newCameraPosition(
                        CameraPosition.builder().bearing(128).zoom(20).target(defaultCoor).build()
                );
        googleMap.moveCamera(cameraUpdate);

        googleMap.setMinZoomPreference(19.0f);

        GroundOverlayOptions groundOverlayOption = new GroundOverlayOptions()
            .image(BitmapDescriptorFactory.fromResource(R.drawable.map_oplib))
            .position(new LatLng(-6.971734f, 107.632741f), 100f, 100f);

        googleMap.addGroundOverlay(groundOverlayOption);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NONE);

        googleMap.setOnMapClickListener(this);
        googleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                Log.d("MapMove", googleMap.getCameraPosition().toString());
            }
        });

        mGoogleMap.setLocationSource(mActivity);
        try {
            mGoogleMap.setMyLocationEnabled(true);
        } catch (SecurityException ex) {

        }

    }

    private void buildGoogleApiClient() {
    }

    @Override
    public void onMapClick(LatLng latLng) {
        mGoogleMap.addMarker(new MarkerOptions().position(latLng));
        Log.d("OnMapClick", latLng.toString());
    }

    public void updateLocation(LatLng latLng) {

    }

    @Override
    public void onDetectedBeacon(List<SavedBeacon> beaconList) {
        if (!isMapReady) return;
        for (int i = 0; i<beaconList.size(); i++) {
            SavedBeacon savedBeacon = beaconList.get(i);
            LatLng latLng = new LatLng(
                    savedBeacon.getLocation().getLatitude(),
                    savedBeacon.getLocation().getLongitude()
            );

            Beacon beacon = savedBeacon.getBeacon();
            if (beacon.getRssi()>-75) {
                MarkerOptions options = new MarkerOptions().position(latLng).title(String.valueOf(bookshelfList));
                Marker marker = mGoogleMap.addMarker(options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            }
            else{
                mMarker.remove();
//                MarkerOptions options = new MarkerOptions().position(latLng);
//                Marker marker = mGoogleMap.addMarker(options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
//                marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
//
            }
        }
    }


    @Override
    public void onItemClick(Bookshelf bookshelf) {
        Toast.makeText(requireContext(), bookshelf.name, Toast.LENGTH_SHORT).show();
    }
//
//    double[][] positions = new double[][] { { 1.2, -10.0 }, {-4.5, -7.0 }, { 6.0, -6.5 }, { 5.0, -1.0 }, {-3.5, 1.0}, {6.0,6.0} };
//    double[] distances = new double[] { 8.06, 13.97, 23.32, 15.31 };
//
//    NonLinearLeastSquaresSolver solver = new NonLinearLeastSquaresSolver(new TrilaterationFunction(positions, distances), new LevenbergMarquardtOptimizer());
//    LeastSquaresOptimizer.Optimum optimum = solver.solve();
//
//    // the answer
//    double[] centroid = optimum.getPoint().toArray();
//
//    // error and geometry information; may throw SingularMatrixException depending the threshold argument provided
//    RealVector standardDeviation = optimum.getSigma(0);
//    RealMatrix covarianceMatrix = optimum.getCovariances(0);
}
