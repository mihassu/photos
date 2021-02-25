package ru.mihassu.photos.ui.fragments.main

import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import io.reactivex.processors.PublishProcessor
import kotlin.math.max
import kotlin.math.min

class BottomNavigationBehavior<V : View> (context: Context, attrs: AttributeSet) : CoordinatorLayout.Behavior<V>(context, attrs) {

    private var direction: Int = 0
    private var childTranslationY: Float = 0f
    private val scrollEvent: PublishProcessor<Int> = PublishProcessor.create()

    override fun onStartNestedScroll(coordinatorLayout: CoordinatorLayout, child: V, directTargetChild: View, target: View, axes: Int, type: Int): Boolean {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL
    }

    override fun onNestedPreScroll(coordinatorLayout: CoordinatorLayout, child: V, target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)
        child.translationY = max(0f, min(child.height.toFloat(), child.translationY + dy))
                .apply { childTranslationY = this }
        direction = dy
    }

    override fun onNestedScroll(coordinatorLayout: CoordinatorLayout, child: V, target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int, type: Int, consumed: IntArray) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type, consumed)

    }

    override fun onStopNestedScroll(coordinatorLayout: CoordinatorLayout, child: V, target: View, type: Int) {
        super.onStopNestedScroll(coordinatorLayout, child, target, type)
        if (direction < 0) {
            moveChild(child, childTranslationY, 0f)
        } else {
            moveChild(child, childTranslationY, child.height.toFloat())
        }
    }


    private fun moveChild(child: V, from: Float, to: Float) {
        ObjectAnimator.ofFloat(child, View.TRANSLATION_Y, from, to).run {
            duration = 300
            start()
        }
    }
}