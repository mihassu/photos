package ru.mihassu.photos.domain


data class PhotoOwner(
    val nsid: String? = null,
    val username: String? = null,
    val realname: String? = null,
    val location: String? = null,
    val iconserver: String? = null,
    val iconfarm: Int? = null,
    val pathAlias: String? = null
) {}