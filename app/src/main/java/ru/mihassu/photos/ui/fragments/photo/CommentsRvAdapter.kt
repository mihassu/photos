package ru.mihassu.photos.ui.fragments.photo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_comment.view.*
import ru.mihassu.photos.R
import ru.mihassu.photos.domain.PhotoComment

class CommentsRvAdapter (var commentsList: List<PhotoComment> = listOf()) : RecyclerView.Adapter<CommentsRvAdapter.CommentsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)
        return CommentsViewHolder(v)
    }

    override fun onBindViewHolder(holder: CommentsViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = commentsList.size


    inner class CommentsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(pos: Int) {
            with(itemView){
                tv_comment_author.text = commentsList[pos].authorname
                tv_comment_content.text = commentsList[pos].content
            }
        }
    }
}