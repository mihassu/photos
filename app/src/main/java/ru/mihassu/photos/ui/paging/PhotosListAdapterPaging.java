package ru.mihassu.photos.ui.paging;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import ru.mihassu.photos.R;
import ru.mihassu.photos.common.Logi;
import ru.mihassu.photos.domain.Photo;
//import androidx.paging.PositionalDataSource;

public class PhotosListAdapterPaging extends PagedListAdapter<Photo, PhotosListAdapterPaging.PhotosListViewHolder> {

//    private List<Photo> dataList;
    private Picasso picasso;
    private OnPhotoClickListener onPhotoClickListener;

    public PhotosListAdapterPaging(Picasso picasso){
        super(new PhotosDiffUtilCallback());
        this.picasso = picasso;
    }

//    public void setDataList(List<Photo> dataList) {
//        this.dataList = dataList;
//    }

    @NonNull
    @Override
    public PhotosListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo, parent, false);
        return new PhotosListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotosListViewHolder holder, int position) {
        Photo photoItem = getItem(position);
        if (photoItem == null) {
            holder.bindPlaceholder();
        } else {
            holder.bind(photoItem);
        }
    }

    class PhotosListViewHolder extends RecyclerView.ViewHolder {

        ImageView photoField;
        TextView titleField;

        PhotosListViewHolder(@NonNull View itemView) {
            super(itemView);
            photoField = itemView.findViewById(R.id.item_photo_field);
//            titleField = itemView.findViewById(R.id.item_title_field);
        }

        void bind(Photo item){
//            titleField.setText(dataList.get(pos).getTitle());
//            Logi.logIt("Adapter - bind(), photo: " + item.getTitle());
            picasso.load(item.getUrl()).into(photoField);
            itemView.setOnClickListener(v -> onPhotoClickListener.onPhotoClick(item.getId()));
        }

        void bindPlaceholder() {
            picasso.load("https://www.talhonline.com/wp-content/uploads/2018/01/icon-no-image-2.jpg")
            .into(photoField);
        }
    }

    public interface OnPhotoClickListener {
        void onPhotoClick(long id);
    }

    public void setOnPhotoClickListener(OnPhotoClickListener onPhotoClickListener) {
        this.onPhotoClickListener = onPhotoClickListener;
    }
}
