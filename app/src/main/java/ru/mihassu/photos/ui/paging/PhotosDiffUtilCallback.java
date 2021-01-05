package ru.mihassu.photos.ui.paging;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import ru.mihassu.photos.domain.Photo;

public class PhotosDiffUtilCallback extends DiffUtil.ItemCallback<Photo> {

//        public PhotosDiffUtilCallback() {
//            super();
//        }
//
//        @Nullable
//        @Override
//        public Object getChangePayload(@NonNull PhotoConvert oldItem, @NonNull PhotoConvert newItem) {
//            return super.getChangePayload(oldItem, newItem);
//        }

    @Override
    public boolean areItemsTheSame(@NonNull Photo oldItem, @NonNull Photo newItem) {
        return oldItem.getId() == newItem.getId();
    }

    @Override
    public boolean areContentsTheSame(@NonNull Photo oldItem, @NonNull Photo newItem) {
        return oldItem.getUrl().equals(newItem.getUrl());
    }
}

