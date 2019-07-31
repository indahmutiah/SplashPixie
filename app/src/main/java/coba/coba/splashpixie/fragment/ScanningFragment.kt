package coba.coba.splashpixie.fragment

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.util.DiffUtil
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import java.util.ArrayList
import java.util.HashMap

import coba.coba.splashpixie.R
import coba.coba.splashpixie.activity.HomeActivity
import coba.coba.splashpixie.adapter.DeviceAdapter
import coba.coba.splashpixie.model.SavedBeacon

class ScanningFragment : Fragment(), HomeActivity.OnDetectedBeaconsListener {

    lateinit var mBluetoothManager: BluetoothManager
    var mIsScanning = false

    private var mDeviceAdapter: DeviceAdapter? = null
    private var mActivity: HomeActivity? = null

    internal var mapDevice = HashMap<String, SavedBeacon>()

    private var mRecyclerView: RecyclerView? = null
    private val TxPower: Any? = null
    private val rssi: Double = 0.toDouble()

    private val itemCallback = object : DiffUtil.ItemCallback<SavedBeacon>() {
        override fun areItemsTheSame(savedBeacon: SavedBeacon, t1: SavedBeacon): Boolean {
            return savedBeacon.beacon!!.bluetoothAddress == t1.beacon!!.bluetoothAddress
        }

        override fun areContentsTheSame(savedBeacon: SavedBeacon, t1: SavedBeacon): Boolean {
            return savedBeacon.beacon!!.rssi == t1.beacon!!.rssi
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mActivity = activity as HomeActivity?
        if (mActivity != null) {
            mActivity!!.setOnDetectedBeaconsListener(this)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_scanning, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mRecyclerView = view.findViewById(R.id.recyclerView)
        //        mButtonBluetooth = view.findViewById(R.id.button_bluetooth);

        mBluetoothManager = requireActivity().getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = RecyclerView.VERTICAL
        layoutManager.reverseLayout = false
        mRecyclerView!!.layoutManager = layoutManager

        mDeviceAdapter = DeviceAdapter(requireContext(), itemCallback)
        mRecyclerView!!.adapter = mDeviceAdapter
    }

    internal fun enableBluetooth() {
        val bluetoothAdapter = mBluetoothManager.adapter
        if (!bluetoothAdapter.isEnabled) {
            bluetoothAdapter.enable()
            Toast.makeText(requireContext(), "Please turn on your bluetooth", Toast.LENGTH_SHORT).show()
        }
        checkCoarseLocationPermission()

    }

    private fun checkCoarseLocationPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                    REQUEST_ACCESS_COARSE_LOCATION)
            return false
        } else {
            return true
        }
    }

    //
    //    ScanCallback scanCallback = new ScanCallback() {
    //        @Override
    //        public void onScanResult(int callbackType, ScanResult result) {
    //            super.onScanResult(callbackType, result);
    //            ArrayList<Device> listName = new ArrayList<>();
    //            Device device = new Device();
    //            device.name = result.getDevice().getName();
    //            device.mac_add = result.getDevice().getAddress();
    //            device.rssi = result.getRssi();
    //
    //
    //
    //            listName.add(device);
    //
    //        }
    //
    //        @Override
    //        public void onBatchScanResults(List<ScanResult> results) {
    //            super.onBatchScanResults(results);
    //
    //            for (int i = 0; i< results.size(); i++) {
    //                ScanResult result = results.get(i);
    //                Device device = new Device();
    //
    //                device.name = result.getScanRecord().getDeviceName();
    //                device.mac_add = result.getDevice().getAddress();
    //                device.rssi = result.getRssi();
    //
    ////                listName.add(device);
    //                mapDevice.put(device.mac_add, device);
    //            }
    //
    //
    //            ArrayList<Device> list = new ArrayList(mapDevice.values());
    ////            mDeviceAdapter.submitList(list);
    //        }
    //
    //        @Override
    //        public void onScanFailed(int errorCode) {
    //            super.onScanFailed(errorCode);
    //            Toast.makeText(requireContext(), "Error scanning", Toast.LENGTH_LONG).show();
    //        }
    //    };

    //    void startscanning (){
    //        if (mIsScanning) {
    //            Toast.makeText(requireContext(), "Already scanning", Toast.LENGTH_SHORT).show();
    //            return;
    //        }
    //        BluetoothLeScannerCompat scannerCompat = BluetoothLeScannerCompat.getScanner();
    //        ScanSettings setting = new ScanSettings.Builder()
    //                .setLegacy(false)
    //                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
    //                .setReportDelay(1000)
    //                .setUseHardwareBatchingIfSupported(false)
    //                .build();
    //
    //        scannerCompat.startScan(null,setting,scanCallback);
    //        mIsScanning = true;
    //    }

    override fun onResume() {
        super.onResume()
        val isActive = mBluetoothManager.adapter.isEnabled
        //        if (isActive) {
        //            startscanning();
        //        } else {
        //            Toast.makeText(requireContext(), "Please turn on bluetooth", Toast.LENGTH_SHORT).show();
        //        }
    }

    //    static DiffUtil.ItemCallback itemCallback = new DiffUtil.ItemCallback<Device>() {
    //        @Override
    //        public boolean areItemsTheSame(@NonNull Device device, @NonNull Device t1) {
    //            return device.mac_add.equals(t1.mac_add);
    //        }
    //
    //        @Override
    //        public boolean areContentsTheSame(@NonNull Device device, @NonNull Device t1) {
    //            return device.rssi == t1.rssi;
    //        }
    //    };

    override fun onDetectedBeacon(results: List<SavedBeacon>) {
        //        for (int i = 0; i < beaconList.size(); i++) {
        //            SavedBeacon savedBeacon = beaconList.get(i);
        //            LatLng latLng = new LatLng(
        //                    savedBeacon.getLocation().getLatitude(),
        //                    savedBeacon.getLocation().getLongitude()
        //            );
        //        }
        for (i in results.indices) {
            val result = results[i]
            //            Device device = new Device();

            //            device.name = result.getScanRecord().getDeviceName();
            //            device.mac_add = result.getDevice().getAddress();
            //            device.rssi = result.getRssi();

            //                listName.add(device);
            mapDevice[result.beacon!!.bluetoothAddress] = result
        }


        val list = ArrayList(mapDevice.values)
        //            mDeviceAdapter.submitList(list);

        mDeviceAdapter!!.submitList(list)
    }

    companion object {

        val REQUEST_ACCESS_COARSE_LOCATION = 1
        val REQUEST_ENABLE_BLUETOOTH = 11
    }

    //    public class DeviceAdapter extends ListAdapter<Device, DeviceAdapter.ViewHolder> {
    //        Context mContext;
    ////        ArrayList<Device> mList;
    //
    //        DeviceAdapter(Context context) {
    //            super(itemCallback);
    //            mContext = context;
    ////            mList = list;
    //        }
    //
    //        @NonNull
    //        @Override
    //        public DeviceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    //            View view = LayoutInflater.from(mContext).inflate(R.layout.item_device,viewGroup, false);
    //            return new DeviceAdapter.ViewHolder(view);
    //        }
    //
    //        @Override
    //        public void onBindViewHolder(@NonNull DeviceAdapter.ViewHolder viewHolder, int position) {
    //            Device device = getItem(position);
    //            viewHolder.name_ble.setText(device.name);
    //            viewHolder.mac_add.setText(device.mac_add);
    //            viewHolder.rssi.setText(Integer.toString(device.rssi));
    //        }
    //
    //        class ViewHolder extends RecyclerView.ViewHolder {
    //            TextView name_ble;
    //            TextView mac_add;
    //            TextView rssi;
    //            public ViewHolder(@NonNull View itemView) {
    //                super(itemView);
    //                name_ble = itemView.findViewById(R.id.name_ble);
    //                mac_add = itemView.findViewById(R.id.mac_add);
    //                rssi = itemView.findViewById(R.id.rssi_text);
    //            }
    //        }
    //    }
    //    }

}
