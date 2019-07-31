package coba.coba.splashpixie.adapter

import android.content.Context
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import java.util.concurrent.TimeoutException

import coba.coba.splashpixie.R
import coba.coba.splashpixie.model.SavedBeacon

class DeviceAdapter(private val mContext: Context, itemCallback: DiffUtil.ItemCallback<SavedBeacon>) : ListAdapter<SavedBeacon, DeviceAdapter.ViewHolder>(itemCallback) {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_device, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val beacon = getItem(i)

        viewHolder.name.text = beacon.beacon!!.bluetoothName
        viewHolder.rssi.text = beacon.beacon!!.rssi.toString()
        viewHolder.minor.text = beacon.beacon!!.id3.toString()

        val distance = getDistance(-55, beacon.beacon!!.rssi)
        viewHolder.distance.text = distance.toString()
    }

    fun getDistance(txPower: Int, rssi: Int): Double {
        return if (rssi >= -61) 1.0
        else if (rssi >= -73) 2.0
        else if (rssi >= -75) 3.0
        else if(rssi >= -80) 4.0
        else if(rssi >= -98) 5.0

        else -1.0
    }

    public inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView
        var rssi: TextView
        var minor: TextView
        var distance: TextView

        init {
            name = view.findViewById(R.id.name_bookself)
            rssi = view.findViewById(R.id.rssi_text)
            minor = view.findViewById(R.id.minor_text)
            distance = view.findViewById(R.id.distance_text)
        }
    }
}
