package ru.mihassu.photos.ui.fragments.interest

import android.app.ActionBar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.marginEnd
import androidx.core.view.setMargins
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.Completable
import ru.mihassu.photos.R


class DatesRvAdapter(val layoutManager: RecyclerView.LayoutManager, val onItemClick: (String) -> Unit) : RecyclerView.Adapter<DatesRvAdapter.DatesViewHolder>() {

    private val SHOW_ALL_STATE = 0x1111
    private val SHOW_ONE_STATE = 0x1000
    private val HIDE_TYPE = 0x01
    private val SHOW_TYPE = 0x11
    private var dataList: List<String> = listOf()
    private var showItemPosition: Int = -1
    private var currentState = SHOW_ALL_STATE

    fun setDataList(data: List<String>) {
        if (dataList.isEmpty()) {
            dataList = data
            notifyDataSetChanged()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position != showItemPosition) {
            HIDE_TYPE
        } else SHOW_TYPE
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DatesViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_date, parent, false)
        return DatesViewHolder(v, viewType)
    }

    override fun onBindViewHolder(holder: DatesViewHolder, position: Int) {
        holder.bind(position)
    }



    override fun getItemCount(): Int = dataList.size


    inner class DatesViewHolder(itemView: View, private val viewType: Int) : RecyclerView.ViewHolder(itemView) {
        private var dateField: TextView = itemView.findViewById(R.id.tv_date)

        fun bind(pos: Int) {
            dateField.text = dataList[pos]

            if (currentState == SHOW_ONE_STATE) {
                if (viewType == HIDE_TYPE) {
                    itemView.apply {
                        visibility = View.GONE
                        val params = layoutParams as RecyclerView.LayoutParams
//                        params.leftMargin = 0
//                        params.rightMargin = 0
                        params.marginEnd = 0
                        params.width = 0
                        layoutParams = params
                    }
                }
            } else if (currentState == SHOW_ALL_STATE) {
                val params = itemView.layoutParams as RecyclerView.LayoutParams
//                params.leftMargin = 10
//                params.rightMargin = 10
                params.marginEnd = 10

                params.width = ViewGroup.LayoutParams.WRAP_CONTENT
                itemView.layoutParams = params
                itemView.visibility = View.VISIBLE
            }

            itemView.setOnClickListener { item ->
                hideAllItemsExcept(pos).subscribe {
                    onItemClick.invoke(dataList[pos])
                }
            }
        }
    }

    private fun hideAllItemsExcept(pos: Int) : Completable {
        return Completable.create { emitter ->
            showItemPosition = pos
            currentState = SHOW_ONE_STATE
            notifyItemRangeChanged(0, dataList.size)
            emitter.onComplete()
        }

    }

    fun showAllItems() {
        currentState = SHOW_ALL_STATE
        notifyItemRangeChanged(0, dataList.size)
    }

}