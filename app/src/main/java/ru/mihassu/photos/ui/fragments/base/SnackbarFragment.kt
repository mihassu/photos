package ru.mihassu.photos.ui.fragments.base

import android.view.View
import com.google.android.material.snackbar.Snackbar

abstract class SnackbarFragment {

    private var snackbar: Snackbar? = null
    abstract val snackbarView: View
    abstract val snackbarText: String
    abstract val snackbarActionText: String
    abstract fun snackbarAction()

    fun initSnackbar(snackbarAnchorView: View) {
        snackbar = Snackbar.make(snackbarView, snackbarText, Snackbar.LENGTH_SHORT)
                .setAnchorView(snackbarAnchorView)
//                .apply {
//                    val params = view.layoutParams as CoordinatorLayout.LayoutParams
//                }
                .setAction(snackbarActionText) { snackbarAction() }
    }

    protected fun showSnackbar() {
        snackbar?.show()
    }
}