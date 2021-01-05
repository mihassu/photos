package ru.mihassu.photos.ui.fragments.favorite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ru.mihassu.photos.R
import ru.mihassu.photos.domain.Photo
import ru.mihassu.photos.ui.custom.FitWidthTransformation

class FavoriteRvAdapter(private val picasso: Picasso)
    : RecyclerView.Adapter<FavoriteRvAdapter.FavoritesViewHolder>() {

    private var dataList: List<Photo> = listOf()
    private var onPhotoClickListener: OnPhotoClickListener? = null
    private var rvWidth: Int = 100


    fun setDataList(data: List<Photo>) {
        dataList = data
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_favorite_photo, parent, false)
        return FavoritesViewHolder(v)
    }

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = dataList.size


    inner class FavoritesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var photoField: ImageView = itemView.findViewById(R.id.item_photo_field)
//        var titleField: TextView = itemView.findViewById(R.id.item_title_field)

        fun bind(pos: Int) {
//            titleField.setText(dataList.get(pos).getTitle());
            picasso
                    .load(dataList[pos].getLargeSizeUrl())
//                    .transform(FitWidthTransformation(rvWidth))
                    .placeholder(R.drawable.placeholder_favorites)
//                    .error(R.drawable.placeholder_test)
                    .resize(1080, 1296)
                    .centerCrop()
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

    fun setRvWidth(width: Int) {
        this.rvWidth = width
    }

}