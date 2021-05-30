package ru.mihassu.photos.domain


data class PhotoDates(
        var posted: String? = null,
        val taken: String? = null,
        val takengranularity: Int? = null,
        val takenunknown: Int? = null,
        val lastupdate: String? = null,
) {}