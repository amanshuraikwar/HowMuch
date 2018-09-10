package io.github.amanshuraikwar.howmuch.data.network

/**
 * API endpoints for the unsplash.com api.
 *
 * @author Amanshu Raikwar
 * Created by Amanshu Raikwar on 30/04/18.
 */
@Suppress("unused")
interface ApiEndpoints {

    companion object {

        const val PER_PAGE = 30

        const val BASE_ENDPOINT = "https://api.unsplash.com/"

        const val GET_ALL_PHOTOS = "photos?"

        const val GET_PHOTO = "photos/{id}"
    }
}