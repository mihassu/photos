package ru.mihassu.photos.ui.animation

import android.animation.*
import android.content.Context
import android.view.View
import androidx.core.animation.doOnEnd
import io.reactivex.Completable
import ru.mihassu.photos.R

class MyAnimator(context: Context) {

    private val hideAnimator: Animator by lazy { AnimatorInflater.loadAnimator(context, R.animator.hide_anim ) }
    private val showAnimator: Animator by lazy { AnimatorInflater.loadAnimator(context, R.animator.show_anim ) }
    private val touchAnimator: Animator by lazy { AnimatorInflater.loadAnimator(context, R.animator.touch_anim ) }


    fun scaleDownAnimation(view: View) : Completable = Completable.create { emitter ->

//        ObjectAnimator.ofFloat(view, "translationY", 0f, -100f).apply {
//            duration = 500
//            doOnEnd { emitter.onComplete() }
//            start()
//        }

        ObjectAnimator.ofPropertyValuesHolder(view,
            PropertyValuesHolder.ofFloat("scaleY", 1f, 0f),
            PropertyValuesHolder.ofFloat("scaleX", 1f, 0f))
            .apply {
                doOnEnd { emitter.onComplete() }
                duration = 300
                start()
            }
    }

    fun scaleUpAnimation(view: View) : Completable = Completable.create { emitter ->

        ObjectAnimator.ofPropertyValuesHolder(view,
                PropertyValuesHolder.ofFloat("scaleY", 0f, 1f),
                PropertyValuesHolder.ofFloat("scaleX", 0f, 1f))
                .apply {
                    doOnEnd { emitter.onComplete() }
                    duration = 300
                    start()
                }
    }

    fun showHideAnimation(view: View) : Completable = Completable.create {emitter ->
        hideAnimator.doOnEnd { emitter.onComplete() }
        hideAnimator.setTarget(view)
        hideAnimator.start()
    }

    fun showShowAnimation(view: View) : Completable = Completable.create {emitter ->
        showAnimator.doOnEnd { emitter.onComplete() }
        showAnimator.setTarget(view)
        showAnimator.start()
    }

    fun showTouchAnimation(view: View) : Completable = Completable.create {emitter ->
        touchAnimator.doOnEnd { emitter.onComplete() }
        touchAnimator.setTarget(view)
        touchAnimator.start()
    }

}