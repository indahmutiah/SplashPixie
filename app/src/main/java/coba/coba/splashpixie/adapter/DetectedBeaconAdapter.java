package coba.coba.splashpixie.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import coba.coba.splashpixie.R;
import coba.coba.splashpixie.model.Bookshelf;
import coba.coba.splashpixie.model.DetectedBeacon;

public class DetectedBeaconAdapter extends ListAdapter<DetectedBeacon, DetectedBeaconAdapter.BookViewHolder>{

    private static final int VIEW_TYPE_HEADER = 358;
    private static final int VIEW_TYPE_BOOKSHELF = 360;

    static DiffUtil.ItemCallback<DetectedBeacon> itemCallback = new DiffUtil.ItemCallback<DetectedBeacon>() {
        @Override
        public boolean areItemsTheSame(@NonNull DetectedBeacon detectedBeacon, @NonNull DetectedBeacon t1) {
            return detectedBeacon.minor == t1.minor;
        }

        @Override
        public boolean areContentsTheSame(@NonNull DetectedBeacon detectedBeacon, @NonNull DetectedBeacon t1) {
            return detectedBeacon.rssi == t1.rssi;
        }
    };

    private Context mContext;
    private OnItemClickListener onItemClickListener;

    public DetectedBeaconAdapter(Context context) {
        super(itemCallback);
        mContext = context;
    }

    @Override
    public int getItemCount() {
        return super.getItemCount()+2;
    }

    @Override
    public int getItemViewType(int position) {
        return position<2?VIEW_TYPE_HEADER:VIEW_TYPE_BOOKSHELF;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView;
        if (viewType == VIEW_TYPE_HEADER) {
            itemView = LayoutInflater.from(mContext).inflate(R.layout.item_header, viewGroup, false);
        } else {
            itemView = LayoutInflater.from(mContext).inflate(R.layout.item_bookself, viewGroup, false);
        }

        return new BookViewHolder(itemView, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder bookViewHolder, int i) {
        if (bookViewHolder.viewType == VIEW_TYPE_BOOKSHELF) {
            DetectedBeacon beacon = getItem(i-2);
            final Bookshelf book = beacon.bookshelf;
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
        } else if (bookViewHolder.viewType == VIEW_TYPE_HEADER) {
            if (i==0) {
                bookViewHolder.headerText.setText("Rak buku terdekat");
            } else {
                bookViewHolder.headerText.setText("Rak buku lainnya");
            }
        }
    }

    @Override
    public void submitList(@Nullable List<DetectedBeacon> list) {
        super.submitList(list);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    class BookViewHolder extends RecyclerView.ViewHolder {
        int viewType;
        View root;
        TextView nameText;
        TextView codeText;

        TextView headerText;
        public BookViewHolder(@NonNull View itemView, int type) {
            super(itemView);
            root = itemView;
            viewType = type;
            if (type == VIEW_TYPE_HEADER) {
                headerText = (TextView) itemView;
            } else if (type == VIEW_TYPE_BOOKSHELF) {
                nameText = itemView.findViewById(R.id.name);
                codeText = itemView.findViewById(R.id.code);
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Bookshelf bookshelf);
    }
}

