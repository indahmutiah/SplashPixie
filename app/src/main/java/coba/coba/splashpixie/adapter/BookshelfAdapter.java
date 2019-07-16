package coba.coba.splashpixie.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import coba.coba.splashpixie.R;
import coba.coba.splashpixie.model.Bookshelf;

public class BookshelfAdapter extends ListAdapter<Bookshelf, BookshelfAdapter.BookViewHolder>{
    static DiffUtil.ItemCallback<Bookshelf> itemCallback = new DiffUtil.ItemCallback<Bookshelf>() {
        @Override
        public boolean areItemsTheSame(@NonNull Bookshelf book, @NonNull Bookshelf t1) {
            return book.code.equals(t1.code);
        }
        @Override
        public boolean areContentsTheSame(@NonNull Bookshelf book, @NonNull Bookshelf t1) {
            return true;
        }
    };

    private Context mContext;
    private OnItemClickListener onItemClickListener;

    public BookshelfAdapter(Context context) {
        super(itemCallback);
        mContext = context;

        ArrayList<Bookshelf> bookshelfList = new ArrayList();
//        bookshelfList.add(new Bookshelf("Ilmu Pengetahuan Umum, Ilmu Komputer", "1"));
//        bookshelfList.add(new Bookshelf("Program Komputer, Pemrograman Sistem ", "3"));
//        bookshelfList.add(new Bookshelf("Tax, Telecommunication ", "9A"));
//        bookshelfList.add(new Bookshelf("Audit, Public Accounting", "14B"));
//        bookshelfList.add(new Bookshelf("Managemen Project, Managemem Konflik, Managemen Produksi", "20"));
        bookshelfList.add(new Bookshelf("Computer Graphic, Information Center, jurnalistik ", "5", new LatLng(-6.971999611527834,107.63278372585773)));
        bookshelfList.add(new Bookshelf("Filosofi, Psikologi, Agama, Ilmu Sosial, Ekonomi Keuangan", "6A,6B", new LatLng(-6.971505074987399,107.63273108750582)));
        bookshelfList.add(new Bookshelf("Signal Processing, Teknik Komputer ", "13",new LatLng(-6.9715663096882885,107.63247661292552)));
        bookshelfList.add(new Bookshelf("Managemen, Managemen Keuangan", "17A", new LatLng(-6.971828886657326, 107.63263184577228)));
        bookshelfList.add(new Bookshelf("Managemen Pemasaran, Penelitian Pemasaran, Pemasaran", "22", new LatLng(-6.971694103820868,107.63284642249347)));

        submitList(bookshelfList);
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_bookself, viewGroup, false);
        return new BookViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder bookViewHolder, int i) {
        final Bookshelf book = getItem(i);
        bookViewHolder.nameText.setText(book.name);
        bookViewHolder.codeText.setText(book.code);
        bookViewHolder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(book);
                }
            }
        });
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    class BookViewHolder extends RecyclerView.ViewHolder {
        View root;
        TextView nameText;
        TextView codeText;
        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            root = itemView;
            nameText = itemView.findViewById(R.id.name);
            codeText = itemView.findViewById(R.id.code);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Bookshelf bookshelf);
    }
}

