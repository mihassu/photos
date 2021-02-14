package ru.mihassu.photos.ui.fragments.common

import android.animation.ObjectAnimator
import android.view.View
import androidx.core.animation.doOnEnd
import androidx.fragment.app.Fragment
import io.reactivex.Completable

open class BaseFragment: Fragment(), AnimatedFragment {

    override fun showQuitAnimation(): Completable {
        return Completable.create { emitter ->
            val alphaAnimator = ObjectAnimator.ofFloat(requireView(), View.ALPHA, 1f, 0f).apply {
                duration = 800
                doOnEnd { emitter.onComplete() }
            }
            alphaAnimator.start()
        }
    }

    override fun showEnterAnimation(): Completable {
        return Completable.create { emitter ->
            val alphaAnimator = ObjectAnimator.ofFloat(requireView(), View.ALPHA, 0f, 1f).apply {
                duration = 800
                doOnEnd { emitter.onComplete() }
            }
            alphaAnimator.start()
        }
    }
}