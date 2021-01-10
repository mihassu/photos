package ru.mihassu.photos.util

import android.content.Context
import android.content.res.Configuration
import android.os.IBinder
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import java.util.regex.Pattern

fun EditText.showKeyboard(context: Context) {
    requestFocus()
    if (isHardwareKeyboardAvailable(context)) {
        postDelayed({
            val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showSoftInput(this, InputMethodManager.SHOW_FORCED)
        }, 300L)
    }
}

fun hideKeyboard(context: Context, windowToken: IBinder) {
//    if (isHardwareKeyboardAvailable(context)) {
//        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//        inputMethodManager.hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_IMPLICIT_ONLY)
//    }

    val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
}

fun isHardwareKeyboardAvailable(context: Context) = context.resources.configuration.keyboard != Configuration.KEYBOARD_NOKEYS

fun EditText.setInputValidation(pattern: Pattern, error: String) {
    setOnFocusChangeListener { v, hasFocus ->
        if (hasFocus) return@setOnFocusChangeListener
        v as TextView
        val text = v.text.toString()
        if (pattern.matcher(text).matches()) {
            v.error = null
        } else v.error = error
    }
}