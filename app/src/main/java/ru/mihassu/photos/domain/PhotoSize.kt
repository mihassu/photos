package ru.mihassu.photos.domain

data class PhotoSize(
        val label: String,
        val width: Int,
        val height: Int,
        val source: String,
        val url: String,
        val media: String)