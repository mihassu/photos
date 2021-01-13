package ru.mihassu.photos.data

import ru.mihassu.photos.data.entity.ApiPhotosResponse
import ru.mihassu.photos.data.entity.InterestResponse
import ru.mihassu.photos.domain.Photo
import ru.mihassu.photos.domain.PhotoPage
import java.util.*

object PhotosMapper {
    @JvmStatic
    fun map(apiResponse: ApiPhotosResponse): PhotoPage {
        val photoList: MutableList<Photo> = ArrayList()
        for (photo in apiResponse.photos.photosList!!) {
            val url = getUrl(photo.farm, photo.server, photo.id, photo.secret)
            photoList.add(Photo(photo.id, photo.title, url))
        }
        return PhotoPage(photoList, apiResponse.photos.total, apiResponse.photos.page, apiResponse.photos.pages)
    }

    @JvmStatic
    fun mapInterest(interestResponse: InterestResponse): PhotoPage {
        val photoList: MutableList<Photo> = ArrayList()
        for (photo in interestResponse.photos.photosList!!) {
            val url = getUrl(photo.farm, photo.server, photo.id, photo.secret)
            photoList.add(Photo(photo.id, photo.title, url))
        }
        return PhotoPage(photoList, interestResponse.photos.total, interestResponse.photos.page, interestResponse.photos.pages)
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