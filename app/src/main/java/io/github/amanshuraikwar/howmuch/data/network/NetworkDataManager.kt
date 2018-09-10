package io.github.amanshuraikwar.howmuch.data.network

import io.github.amanshuraikwar.howmuch.model.Photo

/**
 * Data Manager for the content fetched from the network.
 * This is the single entry point to fetch data from the network.
 *
 * @author Amanshu Raikwar
 * Created by Amanshu Raikwar on 30/04/18.
 */
interface NetworkDataManager {

    fun getAllPhotos(page: Int, orderBy: String, perPage: Int): List<Photo>?
}