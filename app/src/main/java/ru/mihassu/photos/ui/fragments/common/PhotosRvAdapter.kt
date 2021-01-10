package ru.mihassu.photos.ui.fragments.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ru.mihassu.photos.R
import ru.mihassu.photos.domain.Photo

class PhotosRvAdapter(private val picasso: Picasso, private val loadMoreRequest: () -> Unit)
    : RecyclerView.Adapter<PhotosRvAdapter.PhotosListViewHolder>() {

    private var dataList: MutableList<Photo> = mutableListOf()
    private var onPhotoClickListener: OnPhotoClickListener? = null
    var recyclerWidth: Int = 200
    val rvSpanCount: Int = 3


    fun setDataList(data: List<Photo>) {
//        for (i in data) {
//            dataList.add(i)
//        }
//        notifyItemRangeInserted(lastItemPosition, data.size)
        dataList = data.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotosListViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_photo, parent, false)
        return PhotosListViewHolder(v)
    }

    override fun onBindViewHolder(holder: PhotosListViewHolder, position: Int) {
        holder.bind(position)
        if (position >= dataList.size - 1) {
            loadMoreRequest.invoke()
        }
    }



    override fun getItemCount(): Int = dataList.size

    inner class PhotosListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var photoField: ImageView = itemView.findViewById(R.id.item_photo_field)
        val width = recyclerWidth.div(rvSpanCount)

        fun bind(pos: Int) {
            val lp = itemView.layoutParams
            val height = lp.height
            picasso.load(dataList[pos].url)
                    .placeholder(R.drawable.placeholder)
                    .resize(width, height)
                    .centerCrop()
//                    .transform(CropSquareTransformation())
                    .into(photoField)

            itemView.setOnClickListener { v: View? -> onPhotoClickListener!!.onPhotoClick(dataList[pos]) }
        }
    }

    interface OnPhotoClickListener {
        fun onPhotoClick(photo: Photo)
    }

    fun setOnPhotoClickListener(onPhotoClickListener: OnPhotoClickListener?) {
        this.onPhotoClickListener = onPhotoClickListener
    }


}