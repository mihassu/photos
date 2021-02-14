package ru.mihassu.photos.ui.fragments.common

import io.reactivex.Completable

interface AnimatedFragment {
    fun showQuitAnimation() : Completable
    fun showEnterAnimation() : Completable
}