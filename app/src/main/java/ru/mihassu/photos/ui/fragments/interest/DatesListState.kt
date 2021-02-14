package ru.mihassu.photos.ui.fragments.interest

import java.text.SimpleDateFormat
import java.util.*

class DatesListState {

    private val ONE_DAY_SECONDS = 86400L
    private val datesList: MutableList<String> = mutableListOf()
    private var currentDatePos: Int = 0

    init {
        initDatesList()
    }

    fun setCurrentDate(datePos: Int) : DatesListState {
        currentDatePos = datePos
        return this
    }

    fun getCurrentDatePos() = currentDatePos

    fun getCurrentDate() = datesList[currentDatePos]

    fun getDatesList() = datesList

    private fun initDatesList() {
        val unixDate = System.currentTimeMillis()
        for (i in 0..4) {
            val date = Date(unixDate - ONE_DAY_SECONDS * 1000 * (i + 1))
            val itemDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date)
            datesList.add(itemDate)
            if (i == 0) {
                currentDatePos = i
            }
        }
    }
}