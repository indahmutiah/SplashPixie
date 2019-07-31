package coba.coba.splashpixie.data

import coba.coba.splashpixie.model.Bookshelf
import com.google.android.gms.maps.model.LatLng

object BookshelfRepository {
    val bookshelfList = mutableListOf<Bookshelf>(
            Bookshelf(
                    "Computer Graphic, Information Center, jurnalistik ",
                    "5",
                    LatLng(-6.971999611527834, 107.63278372585773),
                    5

            ),
            Bookshelf(
                    "Filosofi, Psikologi, Agama, Ilmu Sosial, Ekonomi Keuangan",
                    "6A,6B",
                    LatLng(-6.971748349757242, 107.63247359544039),
                    10

            ),
            Bookshelf(
                    "Signal Processing, Teknik Komputer ",
                    "13",
                    LatLng(-6.9715663096882885, 107.63247661292552),
                    7
            ),
         Bookshelf(
                    "Managemen, Managemen Keuangan",
                    "17A",
                    LatLng(-6.971505074987399, 107.63273108750582),
                    3
            ),
            Bookshelf(
                    "Managemen Pemasaran, Penelitian Pemasaran, Pemasaran",
                    "22",
                    LatLng(-6.971694103820868, 107.63284642249347),
                    4)
    )
    //        bookshelfList.add(new Bookshelf("Ilmu Pengetahuan Umum, Ilmu Komputer", "1"));
    //        bookshelfList.add(new Bookshelf("Program Komputer, Pemrograman Sistem ", "3"));
    //        bookshelfList.add(new Bookshelf("Tax, Telecommunication ", "9A"));
    //        bookshelfList.add(new Bookshelf("Audit, Public Accounting", "14B"));
    //        bookshelfList.add(new Bookshelf("Managemen Project, Managemem Konflik, Managemen Produksi", "20"));

}