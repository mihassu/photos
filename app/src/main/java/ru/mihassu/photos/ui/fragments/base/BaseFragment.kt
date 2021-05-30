package ru.mihassu.photos.ui.fragments.base

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import androidx.core.animation.doOnEnd
import androidx.fragment.app.Fragment
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable
import ru.mihassu.photos.common.Logi
import ru.mihassu.photos.ui.fragments.common.AnimatedFragment

open class BaseFragment: Fragment(), AnimatedFragment {

    private val disposables: CompositeDisposable = CompositeDisposable()

    override fun showQuitAnimation(): Completable {
        return Completable.create { emitter ->
            val alphaAnimator = ObjectAnimator.ofFloat(requireView(), View.ALPHA, 1f, 0f).apply {
                duration = 400
                doOnEnd { emitter.onComplete() }
            }
            alphaAnimator.start()
        }
    }

    override fun showEnterAnimation(): Completable {
        return Completable.create { emitter ->
            val alphaAnimator = ObjectAnimator.ofFloat(requireView(), View.ALPHA, 0f, 1f).apply {
                duration = 400
                doOnEnd { emitter.onComplete() }
            }
            alphaAnimator.start()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showEnterAnimation().subscribe( {}, { throwable ->
            Logi.logIt("No enter animation. Error: ${throwable.message}")
        }).apply { disposables.add(this) }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }
}