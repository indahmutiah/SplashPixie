package coba.coba.splashpixie.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.concurrent.TimeoutException;

import coba.coba.splashpixie.R;
import coba.coba.splashpixie.model.SavedBeacon;

public class DeviceAdapter extends ListAdapter<SavedBeacon, DeviceAdapter.ViewHolder> {

    private Context mContext;

    public DeviceAdapter(Context context, DiffUtil.ItemCallback<SavedBeacon> itemCallback){
        super(itemCallback);
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_device, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        SavedBeacon beacon = getItem(i);

        viewHolder.name.setText(beacon.getBeacon().getBluetoothName());
        viewHolder.rssi.setText(String.valueOf(beacon.getBeacon().getRssi()));
        viewHolder.minor.setText(String.valueOf(beacon.getBeacon().getId3()));

        double distance = getDistance(-55, beacon.getBeacon().getRssi());
        viewHolder.distance.setText(String.valueOf(distance));
    }

    public double getDistance(int txPower, int rssi){
        /*
         * RSSI = TxPower - 10 * n * lg(d)
         * n = 2 (in free space)
         *
         * d = 10 ^ ((TxPower - RSSI) / (10 * n))
         */
        return Math.pow(10d, ((double)txPower - rssi)/(10*2));
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView rssi;
        TextView minor;
        TextView distance;

        ViewHolder(View view){
            super(view);
            name = view.findViewById(R.id.name_bookself);
            rssi =  view.findViewById(R.id.rssi_text);
            minor = view.findViewById(R.id.minor_text);
            distance = view.findViewById(R.id.distance_text);
        }
    }
}
