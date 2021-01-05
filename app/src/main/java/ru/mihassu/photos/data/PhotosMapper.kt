package ru.mihassu.photos.data

import ru.mihassu.photos.data.entity.ApiPhotosResult
import ru.mihassu.photos.domain.Photo
import ru.mihassu.photos.domain.PhotoPage
import java.util.*

object PhotosMapper {
    @JvmStatic
    fun map(apiResult: ApiPhotosResult): PhotoPage {
        val photoList: MutableList<Photo> = ArrayList()
        for (photo in apiResult.photos.photosList!!) {
            val url = getUrl(photo.farm, photo.server, photo.id, photo.secret)
            photoList.add(Photo(photo.id, photo.title, url))
        }
        return PhotoPage(photoList, apiResult.photos.total, apiResult.photos.page, apiResult.photos.pages)
    }

    private fun getUrl(farm: Long, server: Long, id: Long, secret: String): String {
        val sb = StringBuilder()
        sb
                .append("https://farm")
                .append(farm)
                .append(".staticflickr.com/")
                .append(server)
                .append("/")
                .append(id)
                .append("_")
                .append(secret)
                .append("_w.jpg")
        return sb.toString()
    }
}