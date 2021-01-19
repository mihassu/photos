package ru.mihassu.photos.ui.fragments.common

import androidx.recyclerview.widget.DiffUtil
import ru.mihassu.photos.domain.Photo

class PhotosDiffUtil(private val oldList: List<Photo>, private val newList: List<Photo>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition].id == newList[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition].url == newList[newItemPosition].url
}