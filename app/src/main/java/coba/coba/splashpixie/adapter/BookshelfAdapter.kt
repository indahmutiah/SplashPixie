package coba.coba.splashpixie.adapter

import android.content.Context
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.google.android.gms.maps.model.LatLng

import coba.coba.splashpixie.R
import coba.coba.splashpixie.model.Bookshelf

class BookshelfAdapter(private val mContext: Context) : ListAdapter<Bookshelf, BookshelfAdapter.BookViewHolder>(itemCallback) {
    private var onItemClickListener: ((Bookshelf)->Unit)? = null

    init {

        val bookshelfList = mutableListOf<Bookshelf>()
        //        bookshelfList.add(new Bookshelf("Ilmu Pengetahuan Umum, Ilmu Komputer", "1"));
        //        bookshelfList.add(new Bookshelf("Program Komputer, Pemrograman Sistem ", "3"));
        //        bookshelfList.add(new Bookshelf("Tax, Telecommunication ", "9A"));
        //        bookshelfList.add(new Bookshelf("Audit, Public Accounting", "14B"));
        //        bookshelfList.add(new Bookshelf("Managemen Project, Managemem Konflik, Managemen Produksi", "20"));
        bookshelfList.add(
                Bookshelf(
                        "Computer Graphic, Information Center, jurnalistik ",
                        "5",
                        LatLng(-6.971999611527834, 107.63278372585773),
                        5
                ))
        bookshelfList.add(Bookshelf(
                "Filosofi, Psikologi, Agama, Ilmu Sosial, Ekonomi Keuangan",
                "6A,6B",
                LatLng(-6.971505074987399, 107.63273108750582),
                10))

        bookshelfList.add(Bookshelf(
                "Signal Processing, Teknik Komputer ",
                "13",
                LatLng(-6.9715663096882885, 107.63247661292552),
                7))
        bookshelfList.add(Bookshelf(
                "Managemen, Managemen Keuangan",
                "17A",
                LatLng(-6.971828886657326, 107.63263184577228),
                3))
        bookshelfList.add(Bookshelf(
                "Managemen Pemasaran, Penelitian Pemasaran, Pemasaran",
                "22",
                LatLng(-6.971694103820868, 107.63284642249347),
                4))

        submitList(bookshelfList)
    }

    override fun getItemCount(): Int {
        return super.getItemCount()
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < 0) VIEW_TYPE_HEADER else VIEW_TYPE_BOOKSHELF
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): BookViewHolder {
        val itemView: View
        if (viewType == VIEW_TYPE_HEADER) {
            itemView = LayoutInflater.from(mContext).inflate(R.layout.item_header, viewGroup, false)
        } else {
            itemView = LayoutInflater.from(mContext).inflate(R.layout.item_bookself, viewGroup, false)
        }

        return BookViewHolder(itemView, viewType)
    }

    override fun onBindViewHolder(bookViewHolder: BookViewHolder, i: Int) {
        if (bookViewHolder.viewType == VIEW_TYPE_BOOKSHELF) {
            val book = getItem(i)

            bookViewHolder.nameText.text = book.name
            bookViewHolder.codeText.text = book.code
            bookViewHolder.root.setOnClickListener {
                if (onItemClickListener != null) {
                    onItemClickListener!!.invoke(book)
                }
            }
        } else if (bookViewHolder.viewType == VIEW_TYPE_HEADER) {
            if (i == 0) {
                bookViewHolder.headerText.text = "Rak buku terdekat"
            } else {
                bookViewHolder.headerText.text = "Rak buku lainnya"
            }
        }
    }

    override fun submitList(list: List<Bookshelf>?) {
        super.submitList(list)
    }

    fun setOnItemClickListener(onItemClickListener: ((Bookshelf) -> Unit)?) {
        this.onItemClickListener = onItemClickListener
    }

    public inner class BookViewHolder(var root: View, var viewType: Int) : RecyclerView.ViewHolder(root) {
        lateinit var nameText: TextView
        lateinit var codeText: TextView
        lateinit var headerText: TextView
        init {
            if (viewType == VIEW_TYPE_HEADER) {
                headerText = root as TextView
            } else if (viewType == VIEW_TYPE_BOOKSHELF) {
                nameText = root.findViewById(R.id.name)
                codeText = root.findViewById(R.id.code)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(bookshelf: Bookshelf)
    }

    companion object {

        private val VIEW_TYPE_HEADER = 1
        private val VIEW_TYPE_BOOKSHELF = 2

        internal var itemCallback: DiffUtil.ItemCallback<Bookshelf> = object : DiffUtil.ItemCallback<Bookshelf>() {
            override fun areItemsTheSame(book: Bookshelf, t1: Bookshelf): Boolean {
                return book.code == t1.code
            }

            override fun areContentsTheSame(book: Bookshelf, t1: Bookshelf): Boolean {
                return true
            }
        }
    }
}

