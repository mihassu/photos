package ru.mihassu.photos.ui.fragments.interest

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.transition.*
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import androidx.core.view.marginEnd
import androidx.lifecycle.ViewModelProvider
import ru.mihassu.photos.R
import ru.mihassu.photos.common.Logi

class DatesScrollConstraint @JvmOverloads constructor (context: Context, val attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : ConstraintLayout(context, attrs, defStyleAttr) {

    private var onDateClick: ((Int) -> Unit)? = null
    private var datesList: List<String> = listOf()
    private val set: ConstraintSet = ConstraintSet()
    private val viewsList = mutableListOf<TextView>()
    private val transitionSet: TransitionSet

    init {
        background = ContextCompat.getDrawable(context, R.drawable.item_date_unchecked)
        transitionSet = TransitionSet().apply {
            addTransition(Fade())
            addTransition(ChangeBounds())
            ordering = TransitionSet.ORDERING_TOGETHER
            duration = 500
        }
    }

    fun setOnDateClick(action: (Int) -> Unit) {
        onDateClick = action
    }


    fun addViewForDates(dates: List<String>, checkedPos: Int) {
        if (childCount == 0) {
            //создать для каждой даты textView и добавить их в массив
            datesList = dates
            datesList.forEachIndexed { index, date ->
                val tv = (TextView(context)).apply {
                    text = date
                    setOnClickListener {
//                        hideAllExcept(index)
                        Logi.logIt("translationX: $translationX, x: $x")
                        onDateClick?.invoke(index)
                    }
                    id = index
                }
                viewsList.add(tv)
            }

            viewsList.forEachIndexed { index, textView ->

                if (index == 0) { // первую textView привязать к родителю
                    set.connect(textView.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
                    set.connect(textView.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)

                } else { //остальные привязать к первой
                    set.connect(textView.id, ConstraintSet.START, viewsList[0].id, ConstraintSet.START)
                    set.connect(textView.id, ConstraintSet.TOP, viewsList[0].id, ConstraintSet.TOP)
                }

                // установить фон и видимость для выбранной позиции
                if (index == checkedPos) {
                    textView.background = ContextCompat.getDrawable(context, R.drawable.item_date_checked)
                } else {
                    textView.background = ContextCompat.getDrawable(context, R.drawable.item_date_unchecked)
//                    textView.visibility = View.GONE
//                    textView.alpha = 0f
//                    set.setAlpha(textView.id, 0f)
                    set.setVisibility(textView.id, View.INVISIBLE)
                }

                set.constrainWidth(textView.id, ConstraintSet.WRAP_CONTENT)
                addView(textView)
            }
            set.applyTo(this)

        } else {
            hideAllExcept(checkedPos)
        }
    }

    fun showAllItems() {
        set.clone(this)
        viewsList.forEachIndexed { index, textView ->
            set.run {
                if (index > 0) {
                    clear(textView.id, ConstraintSet.START)
                    connect(textView.id, ConstraintSet.START, viewsList[index - 1].id, ConstraintSet.END)
                }
//                textView.visibility = View.VISIBLE
//                textView.alpha = 1f
//                setAlpha(textView.id, 1f)
                setVisibility(textView.id, View.VISIBLE)
            }
        }
        TransitionManager.beginDelayedTransition(this, transitionSet)
        set.applyTo(this)
    }

    private fun hideAllExcept(pos: Int) {
        set.clone(this)
        viewsList.forEachIndexed { index, textView ->
            if (index != pos) {
                set.run {
                    if (index > 0) {
                        clear(textView.id, ConstraintSet.START)
                        connect(textView.id, ConstraintSet.START, viewsList[0].id, ConstraintSet.START)
                    }
                    textView.background = ContextCompat.getDrawable(context, R.drawable.item_date_unchecked)
//                    textView.visibility = View.GONE
//                    textView.alpha = 0f
//                    setAlpha(textView.id, 0f)
                    setVisibility(textView.id, View.INVISIBLE)
                }
            } else {
                set.run {
                    if (index > 0) {
                        clear(textView.id, ConstraintSet.START)
                        connect(textView.id, ConstraintSet.START, viewsList[0].id, ConstraintSet.START)
                    }
                    textView.background = ContextCompat.getDrawable(context, R.drawable.item_date_checked)
//                    textView.visibility = View.VISIBLE
//                    textView.alpha = 1f
//                    setAlpha(textView.id, 1f)
                    setVisibility(textView.id, View.VISIBLE)
                }
            }

        }
        TransitionManager.beginDelayedTransition(this, transitionSet)
        set.applyTo(this)
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