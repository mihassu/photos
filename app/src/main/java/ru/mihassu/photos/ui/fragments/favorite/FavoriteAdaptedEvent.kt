package ru.mihassu.photos.ui.fragments.favorite

import ru.mihassu.photos.domain.Photo

sealed class FavoriteAdaptedEvent {
    class Click(val photo: Photo) : FavoriteAdaptedEvent()
    class Swipe(val photo: Photo) : FavoriteAdaptedEvent()
}