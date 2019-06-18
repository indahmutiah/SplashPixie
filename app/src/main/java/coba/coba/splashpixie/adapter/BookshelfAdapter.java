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
            return false;
        }
    };

    private Context mContext;

    public BookshelfAdapter(Context context) {
        super(itemCallback);
        mContext = context;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_bookself, viewGroup, false);
        return new BookViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder bookViewHolder, int i) {
        Bookshelf book = getItem(i);
        bookViewHolder.nameText.setText(book.name);
        bookViewHolder.codeText.setText(book.code);
    }

    class BookViewHolder extends RecyclerView.ViewHolder {
        TextView nameText;
        TextView codeText;
        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.name);
            codeText = itemView.findViewById(R.id.code);
        }
    }

}

