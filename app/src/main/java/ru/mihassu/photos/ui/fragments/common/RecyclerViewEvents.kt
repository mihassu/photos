package ru.mihassu.photos.ui.fragments.common

import ru.mihassu.photos.domain.Photo

sealed class RecyclerViewEvents {
    class PhotoClickEvent(val photo: Photo) : RecyclerViewEvents()
    class ScrollEvent(val direction: Int) : RecyclerViewEvents()
    class LoadMoreEvent(val lastPosition: Int) : RecyclerViewEvents()
}