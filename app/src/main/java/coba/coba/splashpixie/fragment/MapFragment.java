package coba.coba.splashpixie.fragment;

import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import coba.coba.splashpixie.R;
import coba.coba.splashpixie.adapter.BookshelfAdapter;
import coba.coba.splashpixie.model.Bookshelf;

public class MapFragment extends Fragment implements OnMapReadyCallback {
    private MapView mMapView;
    private BookshelfAdapter mAdapter;
    private List<Bookshelf> bookshelfList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        mMapView = view.findViewById(R.id.mapview);
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView listBookself = view.findViewById(R.id.listBookshelf);
        listBookself.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        mAdapter = new BookshelfAdapter(getContext());
        listBookself.setAdapter(mAdapter);
        initList();
    }

    private void initList() {
        bookshelfList.add(new Bookshelf("Ilmu Pengetahuan Umum, Ilmu Komputer", "1"));
        bookshelfList.add(new Bookshelf("Computer Graphic, Information Center, jurnalistik ", "5"));
        bookshelfList.add(new Bookshelf("Tax, Telecommunication ", "9A"));
        bookshelfList.add(new Bookshelf("Management, Financial Management", "17A"));
        bookshelfList.add(new Bookshelf("Signal Processing, Teknik Komputer ", "13"));
        bookshelfList.add(new Bookshelf(" Marketing Management, Marketing Reserch, Marketing, channls", "22"));




        mAdapter.submitList(bookshelfList);
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
    public void onMapReady(GoogleMap googleMap) {

        // Add a marker in Sydney and move the camera
        LatLng defaultCoor = new LatLng(-6.9717929, 107.6320263);
        googleMap.addMarker(new MarkerOptions().position(defaultCoor).title("Marker in Open Library"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(defaultCoor));
        googleMap.setMinZoomPreference(19.0f);

        GroundOverlayOptions groundOverlayOption = new GroundOverlayOptions()
            .image(BitmapDescriptorFactory.fromResource(R.drawable.map_oplib))
            .position(new LatLng(-6.971734f, 107.632741f), 100f, 100f);

        googleMap.addGroundOverlay(groundOverlayOption);

        googleMap.setMapType(GoogleMap.MAP_TYPE_NONE);
    }

}
