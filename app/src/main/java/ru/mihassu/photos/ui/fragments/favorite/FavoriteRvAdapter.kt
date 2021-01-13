package ru.mihassu.photos.ui.fragments.favorite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ru.mihassu.photos.R
import ru.mihassu.photos.domain.Photo

class FavoriteRvAdapter(private val picasso: Picasso)
    : RecyclerView.Adapter<FavoriteRvAdapter.FavoritesViewHolder>(), IFavoriteTouchAdapter {

    private var dataList: List<Photo> = listOf()
    private var adapterEventListener: AdapterEventListener? = null
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
                    .error(R.drawable.placeholder_error)
                    .resize(1080, 1296)
                    .centerCrop()
                    .into(photoField)
            itemView.setOnClickListener { v: View? ->
                adapterEventListener?.onEvent(FavoriteAdaptedEvent.Click(dataList[pos])) }
        }
    }

    interface AdapterEventListener {
        fun onEvent(event: FavoriteAdaptedEvent)
    }

    fun setAdapterEventListener(adapterEventListener: AdapterEventListener?) {
        this.adapterEventListener = adapterEventListener
    }

    fun setRvWidth(width: Int) {
        this.rvWidth = width
    }

    //Вызывается при смахивании
    override fun onItemDismiss(position: Int) {
        adapterEventListener?.onEvent(FavoriteAdaptedEvent.Swipe(dataList[position]))
    }
}