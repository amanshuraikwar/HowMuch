package io.github.amanshuraikwar.howmuch.data.network

import io.github.amanshuraikwar.howmuch.model.Photo
import io.github.amanshuraikwar.howmuch.util.Util
import java.io.IOException
import javax.inject.Inject

/**
 * Implementation of Network Data Manager.
 *
 * @author Amanshu Raikwar
 * Created by Amanshu Raikwar on 30/04/18.
 */
class NetworkDataManagerImpl @Inject constructor(
        private val apiInterface: ApiInterface) : NetworkDataManager {

    private val TAG = Util.getTag(this)

    @Throws(IOException::class)
    override fun getAllPhotos(page: Int, orderBy: String, perPage: Int): List<Photo>? {
        return apiInterface.getAllPhotos(page, orderBy, perPage).execute().body()
    }
}