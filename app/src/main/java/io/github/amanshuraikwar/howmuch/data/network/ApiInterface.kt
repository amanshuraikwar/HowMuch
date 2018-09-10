package io.github.amanshuraikwar.howmuch.data.network

import io.github.amanshuraikwar.howmuch.model.Photo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

/**
 * Retrofit api interface.
 *
 * @author Amanshu Raikwar
 * Created by Amanshu Raikwar on 30/04/18.
 */
interface ApiInterface {

    @Suppress("unused")
    companion object {
        const val ORDER_BY_LATEST = "latest"
        const val ORDER_BY_OLDEST = "oldest"
        const val ORDER_BY_POPULAR = "popular"
        const val VERSION = "v1"
        const val ACCESS_KEY = ""
    }


    @Headers(
        value = ["Accept: application/json",
        "Accept-Version: $VERSION",
        "Authorization: Client-ID $ACCESS_KEY"])
    @GET(ApiEndpoints.GET_ALL_PHOTOS)
    fun getAllPhotos(@Query("page") page: Int,
                     @Query("order_by") orderBy: String,
                     @Query("per_page") perPage: Int = ApiEndpoints.PER_PAGE)
            : Call<List<Photo>>
}