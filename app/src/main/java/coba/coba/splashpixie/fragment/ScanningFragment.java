package coba.coba.splashpixie.fragment;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import coba.coba.splashpixie.model.Device;
import coba.coba.splashpixie.R;
import no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerCompat;
import no.nordicsemi.android.support.v18.scanner.ScanCallback;
import no.nordicsemi.android.support.v18.scanner.ScanResult;
import no.nordicsemi.android.support.v18.scanner.ScanSettings;

public class ScanningFragment extends Fragment{

    public static final int REQUEST_ACCESS_COARSE_LOCATION = 1;
    public static final int REQUEST_ENABLE_BLUETOOTH = 11;
    BluetoothManager mBluetoothManager;
    boolean mIsScanning = false;

    private RecyclerView mRecyclerView;
    private FloatingActionButton mButtonBluetooth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_scanning, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = view.findViewById(R.id.recyclerView);
        mButtonBluetooth = view.findViewById(R.id.button_bluetooth);

        mBluetoothManager = (BluetoothManager) requireActivity().getSystemService(Context.BLUETOOTH_SERVICE);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        layoutManager.setReverseLayout(false);
        mRecyclerView.setLayoutManager(layoutManager);

        mButtonBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isActive = mBluetoothManager.getAdapter().isEnabled();
                if (isActive) {
                    startscanning();
                } else {
                    Toast.makeText(requireContext(), "Please turn on bluetooth", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    void enableBluetooth(){
        BluetoothAdapter bluetoothAdapter = mBluetoothManager.getAdapter();
        if (!bluetoothAdapter.isEnabled()){
            bluetoothAdapter.enable();
            Toast.makeText(requireContext(), "Please turn on your bluetooth", Toast.LENGTH_SHORT).show();
        }
        checkCoarseLocationPermission();

    }

    private boolean checkCoarseLocationPermission() {
        if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                !=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(requireActivity(),new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_ACCESS_COARSE_LOCATION);
            return false;
        } else {
            return true;
        }
    }


    ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            ArrayList<Device> listName = new ArrayList<>();
            Device device = new Device();
            device.name = result.getDevice().getName();
            device.mac_add = result.getDevice().getAddress();
            device.rssi = result.getRssi();

            listName.add(device);

            DeviceAdapter adapter = new DeviceAdapter(requireContext(), listName);
            mRecyclerView.setAdapter(adapter);
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);

            ArrayList<Device> listName = new ArrayList<>();

            for (int i = 0; i< results.size(); i++) {
                ScanResult result = results.get(i);
                Device device = new Device();

                device.name = result.getDevice().getName();
                device.mac_add = result.getDevice().getAddress();
                device.rssi = result.getRssi();

                listName.add(device);
            }

            DeviceAdapter adapter = new DeviceAdapter(requireContext(), listName);
            mRecyclerView.setAdapter(adapter);
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
            Toast.makeText(requireContext(), "Error scanning", Toast.LENGTH_LONG).show();
        }
    };

    void startscanning (){
        if (mIsScanning) {
            Toast.makeText(requireContext(), "Already scanning", Toast.LENGTH_SHORT).show();
            return;
        }
        BluetoothLeScannerCompat scannerCompat = BluetoothLeScannerCompat.getScanner();
        ScanSettings setting = new ScanSettings.Builder()
                .setLegacy(false)
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .setReportDelay(1000)
                .setUseHardwareBatchingIfSupported(false)
                .build();

        scannerCompat.startScan(null,setting,scanCallback);
        mIsScanning = true;
    }
    public class DeviceAdapter extends RecyclerView.Adapter<ScanningFragment.DeviceAdapter.ViewHolder>{
        Context mContext;
        ArrayList<Device> mList;
        public DeviceAdapter(Context context, ArrayList<Device> list) {
            super();
            mContext = context;
            mList = list;
        }

        @NonNull
        @Override
        public DeviceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_device,viewGroup, false);
            return new DeviceAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull DeviceAdapter.ViewHolder viewHolder, int position) {
            Device device = mList.get(position);
            viewHolder.name_ble.setText(device.name);
            viewHolder.mac_add.setText(device.mac_add);
            viewHolder.rssi.setText(Integer.toString(device.rssi));
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView name_ble;
            TextView mac_add;
            TextView rssi;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                name_ble = itemView.findViewById(R.id.name_ble);
                mac_add = itemView.findViewById(R.id.mac_add);
                rssi = itemView.findViewById(R.id.rssi_text);
            }
        }
    }
}
