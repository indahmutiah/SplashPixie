package coba.coba.splashpixie.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import coba.coba.splashpixie.R;
import coba.coba.splashpixie.fragment.MapFragment;
import coba.coba.splashpixie.fragment.ScanningFragment;
import coba.coba.splashpixie.model.Bookshelf;

public class HomeActivity extends AppCompatActivity {
    private ActionBar toolbar;

    private Fragment[] listFragments = new Fragment[]{
            new MapFragment(),
            new ScanningFragment()
    };



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        toolbar = getSupportActionBar();


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_nav);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        toolbar.setTitle("Map");
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.map:
                    toolbar.setTitle("Map");
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.container, listFragments[0])
                            .commit();
                    return true;
                case R.id.ble:
                    toolbar.setTitle("Bluetooth Scanning");
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.container, listFragments[1])
                            .commit();
                    return true;
            }
            return false;
        }
    };
    private void loadFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}


