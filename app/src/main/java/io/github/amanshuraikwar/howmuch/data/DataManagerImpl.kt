package io.github.amanshuraikwar.howmuch.data

import android.content.Context
import io.github.amanshuraikwar.howmuch.data.network.NetworkDataManager
import io.github.amanshuraikwar.howmuch.di.ApplicationContext
import javax.inject.Inject

/**
 * Implementation of the Data Manager for the app.
 *
 * @author Amanshu Raikwar
 * Created by Amanshu Raikwar on 07/03/18.
 */
class DataManagerImpl @Inject constructor(
        @ApplicationContext val context: Context,
        private val networkDataManager: NetworkDataManager) : DataManager {

    override fun getAllPhotos(page: Int, orderBy: String, perPage: Int)
            = networkDataManager.getAllPhotos(page, orderBy, perPage)
}