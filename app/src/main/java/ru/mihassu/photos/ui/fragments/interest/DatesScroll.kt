package ru.mihassu.photos.ui.fragments.interest

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import androidx.core.view.marginEnd
import androidx.lifecycle.ViewModelProvider
import ru.mihassu.photos.R
import ru.mihassu.photos.common.Logi

class DatesScroll @JvmOverloads constructor (context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : LinearLayout(context, attrs, defStyleAttr) {

    private var onDateClick: ((Int) -> Unit)? = null
    private var datesList: List<String> = listOf()


    init {
        orientation = HORIZONTAL
        background = ContextCompat.getDrawable(context, R.drawable.item_date_checked)
    }

    fun setOnDateClick(action: (Int) -> Unit) {
        onDateClick = action
    }


    fun addViewForDates(dates: List<String>, checkedPos: Int) {
        if (childCount == 0) {
            datesList = dates
            datesList.forEachIndexed { index, date ->
                val tv = (TextView(context)).apply {
                    text = date
                    setOnClickListener {
                        hideAllExcept(index)
                        Logi.logIt("translationX: $translationX, x: $x")
//                        onDateClick?.invoke(index)
                    }
                }
                addView(tv)
//                tv.translationX = -tv.x
                val lp = tv.layoutParams as LinearLayout.LayoutParams
//                lp.marginStart = resources.getDimension(R.dimen.date_item_margin).toInt()
                lp.marginEnd = resources.getDimension(R.dimen.date_item_margin).toInt()
//                lp.topMargin = resources.getDimension(R.dimen.date_item_margin).toInt()
//                lp.bottomMargin = resources.getDimension(R.dimen.date_item_margin).toInt()
                tv.layoutParams = lp
                setBackgroundAndTranslation(tv, index == checkedPos)
            }
        }
//        datesList.forEachIndexed { index, _ ->
//            getChildAt(index).run {
//                background = if (index == checkedPos) {
//                    ContextCompat.getDrawable(context, R.drawable.item_date_checked)
//                } else {
//                    ContextCompat.getDrawable(context, R.drawable.item_date_unchecked)
//                }
//                this.translationX = -this.x
//            }
//        }
//        hideAllExcept(checkedPos)
    }

    fun showAllItems() {
        datesList.forEachIndexed { index, _ ->
            getChildAt(index).run {
                val translationAnimator = ObjectAnimator.ofFloat(this, View.TRANSLATION_X, translationX, 0f)
                translationAnimator.duration = 500
                if (alpha == 0f) {
                    val alphaAnimator = ObjectAnimator.ofFloat(this, View.ALPHA, 0f, 1f)
                    alphaAnimator.duration = 500
                    val set = AnimatorSet()
                    set.playTogether(translationAnimator, alphaAnimator)
//                    set.doOnEnd { visibility = View.VISIBLE }
                    set.start()
                } else {
                    translationAnimator.start()
                }
            }
        }

    }

    private fun hideAllExcept(pos: Int) {
        datesList.forEachIndexed { index, _ ->
            getChildAt(index).run {
                val translationAnimator = ObjectAnimator.ofFloat(this, View.TRANSLATION_X, 0f, -x + marginEnd)
                translationAnimator.duration = 500
                if (index != pos) {
                    background = ContextCompat.getDrawable(context, R.drawable.item_date_unchecked)
                    val alphaAnimator = ObjectAnimator.ofFloat(this, View.ALPHA, 1f, 0f)
                    alphaAnimator.duration = 500
                    val set = AnimatorSet()
                    set.playTogether(translationAnimator, alphaAnimator)
//                    set.doOnEnd { visibility = View.GONE }
                    set.start()
                } else {
                    background = ContextCompat.getDrawable(context, R.drawable.item_date_checked)
                    translationAnimator.start()
                }
            }
        }
    }

    private fun setBackgroundAndTranslation(view: View, checked: Boolean) {
        view.background = if (checked) {
            ContextCompat.getDrawable(context, R.drawable.item_date_checked)
        } else {
            ContextCompat.getDrawable(context, R.drawable.item_date_unchecked)
        }
        val translationAnimator = ObjectAnimator.ofFloat(view, View.TRANSLATION_X, 0f, -view.x)
        translationAnimator.duration = 10
        translationAnimator.start()
    }


    private fun Int.toPx() = this * context.resources.displayMetrics.density
    private fun Float.toPx() = this * context.resources.displayMetrics.density
    private fun Float.toDp() = this / context.resources.displayMetrics.density

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredWidth = suggestedMinimumWidth + paddingLeft + paddingRight
        val desiredHeight = suggestedMinimumHeight + paddingTop + paddingBottom

        val specWidthMode = MeasureSpec.getMode(widthMeasureSpec)
        val specWith = MeasureSpec.getSize(widthMeasureSpec)

        val specHeightMode = MeasureSpec.getMode(heightMeasureSpec)
        val specHeight = MeasureSpec.getSize(heightMeasureSpec)

        val resolvedWidth = resolveSize(desiredWidth, widthMeasureSpec)
        val resolvedHeight = resolveSize(desiredHeight, heightMeasureSpec)

//        setMeasuredDimension(specWith, specHeight)
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

}