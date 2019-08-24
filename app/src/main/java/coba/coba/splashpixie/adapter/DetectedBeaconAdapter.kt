package coba.coba.splashpixie.adapter


import android.content.Context
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import coba.coba.splashpixie.R
import coba.coba.splashpixie.model.Bookshelf
import coba.coba.splashpixie.model.DetectedBeacon

class DetectedBeaconAdapter(private val mContext: Context) : RecyclerView.Adapter<DetectedBeaconAdapter.BookViewHolder>() {

    private var onItemClickListener: ((DetectedBeacon)->Unit)? = null
    private var onBookshelfClickListener: ((Bookshelf) -> Unit)? = null
    private var nearestPosition : DetectedBeacon? = null
    private var listNearbyBeacon = listOf<DetectedBeacon>()
    private var listBookshelf = listOf<Bookshelf>()
    private var headerCount = 3

    override fun getItemCount(): Int {
        return listNearbyBeacon.size + listBookshelf.size + headerCount
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0 || position == listNearbyBeacon.size + 1) {
            VIEW_TYPE_HEADER
        } else if (position < listNearbyBeacon.size + 1) {
            VIEW_TYPE_DEVICE
        } else if (position > listNearbyBeacon.size + 1){
            VIEW_TYPE_BOOKSHELF
        } else {
            VIEW_TYPE_ELSE
        }
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): BookViewHolder {
        val itemView: View
        if (viewType == VIEW_TYPE_HEADER) {
            itemView = LayoutInflater.from(mContext).inflate(R.layout.item_header, viewGroup, false)
        } else if (viewType == VIEW_TYPE_DEVICE){
            itemView = LayoutInflater.from(mContext).inflate(R.layout.item_device, viewGroup, false)
        } else if (viewType == VIEW_TYPE_BOOKSHELF){
            itemView = LayoutInflater.from(mContext).inflate(R.layout.item_bookself, viewGroup, false)
        } else {
            itemView = LayoutInflater.from(mContext).inflate(android.R.layout.simple_list_item_1, viewGroup, false)
        }

        return BookViewHolder(itemView, viewType)
    }

    override fun onBindViewHolder(viewHolder: BookViewHolder, i: Int) {
        if (viewHolder.viewType == VIEW_TYPE_DEVICE) {
            val beacon = listNearbyBeacon[i - 1]
            val book = beacon.bookshelf
            viewHolder.nameText.text = book!!.name
            viewHolder.rssiText.text = beacon.rssi.toString()
            viewHolder.minorText.text = beacon.minor.toString()
            viewHolder.numberText.text = book!!.code
            val distance = getDistance(10, beacon!!.rssi)
            viewHolder.distanceText.text = distance.toString()

            viewHolder.root.setOnClickListener {
                if (onItemClickListener != null) {
                    onItemClickListener!!.invoke(beacon)
                }
            }
        } else if (viewHolder.viewType == VIEW_TYPE_BOOKSHELF) {
            if (i - listNearbyBeacon.size > listBookshelf.size) return
            val bookshelf = listBookshelf[i - listNearbyBeacon.size -1]
            viewHolder.nameText.text = bookshelf.name
            viewHolder.codeText.text = bookshelf.code
            viewHolder.root.setOnClickListener {
                onBookshelfClickListener?.invoke(bookshelf)
            }
        } else if (viewHolder.viewType == VIEW_TYPE_HEADER) {
            if (i == 0) {
                viewHolder.headerText.text = "Rak buku terdekat"
            }
            else if (i == listNearbyBeacon.size + 1){
                viewHolder.headerText.text = "Rak buku lainnya"
            }
        }
    }

    fun getDistance(txPower: Int, rssi: Int): Double {

        return if (rssi >= -63) 1.0
        else if (rssi >= -72) 2.0
        else if (rssi >= -75) 3.0
        else if(rssi >= -79) 4.0
        else if(rssi >= -83) 5.0
        else if(rssi >= -86) 6.0
        else if (rssi >= -88) 7.0
        else if(rssi >= - 91) 8.0
        else if(rssi >= -94) 9.0
        else if(rssi >= -97) 10.0

        else -1.0
    }


    fun submitList(list: List<DetectedBeacon>) {

        listNearbyBeacon = list
        notifyDataSetChanged()
    }

    fun submitBookshelf(list: List<Bookshelf>) {
        listBookshelf = list
        notifyDataSetChanged()
    }

    fun submitPosition(detectedBeacon: DetectedBeacon) {
        nearestPosition = detectedBeacon
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(onItemClickListener: (DetectedBeacon)->Unit) {
        this.onItemClickListener = onItemClickListener
    }

    fun setOnBookshelClickListener(onBookshelfListener: (Bookshelf)->Unit) {
        this.onBookshelfClickListener = onBookshelfClickListener
    }

    public inner class BookViewHolder(var root: View, var viewType: Int) : RecyclerView.ViewHolder(root) {
        // Device
        lateinit var nameText: TextView
        lateinit var numberText: TextView
        lateinit var rssiText : TextView
        lateinit var minorText: TextView
        lateinit var distanceText : TextView

        // Bookshelf
        lateinit var codeText: TextView

        //Header
        lateinit var headerText: TextView


        init {
            if (viewType == VIEW_TYPE_HEADER) {
                headerText = root as TextView
            } else if (viewType == VIEW_TYPE_DEVICE) {
                nameText = root.findViewById(R.id.name_bookself)
                numberText = root.findViewById(R.id.number_bookshelf)
                rssiText = root.findViewById(R.id.rssi_text)
                minorText = root.findViewById(R.id.minor_text)
                distanceText = root.findViewById(R.id.distance_text)

            } else if (viewType == VIEW_TYPE_BOOKSHELF) {
                nameText = root.findViewById(R.id.name)
                codeText = root.findViewById(R.id.code)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(bookshelf: Bookshelf?)
    }

    companion object {

        private val VIEW_TYPE_HEADER = 358
        private val VIEW_TYPE_DEVICE = 359
        private val VIEW_TYPE_BOOKSHELF = 360
        private val VIEW_TYPE_ELSE = 361

        internal var itemCallback: DiffUtil.ItemCallback<DetectedBeacon> = object : DiffUtil.ItemCallback<DetectedBeacon>() {
            override fun areItemsTheSame(detectedBeacon: DetectedBeacon, t1: DetectedBeacon): Boolean {
                return detectedBeacon.minor == t1.minor
            }

            override fun areContentsTheSame(detectedBeacon: DetectedBeacon, t1: DetectedBeacon): Boolean {
                return detectedBeacon.rssi == t1.rssi
            }
//            override fun (detectedBeacon: DetectedBeacon,t1: DetectedBeacon): Boolean {
//                return detectedBeacon.distance == t1.distance
//            }
        }
    }
}

