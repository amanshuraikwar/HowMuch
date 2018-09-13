package io.github.amanshuraikwar.howmuch.data.network

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import io.github.amanshuraikwar.howmuch.data.network.sheets.AuthenticationManager
import io.github.amanshuraikwar.howmuch.model.Photo
import io.reactivex.Observable

/**
 * Data Manager for the content fetched from the network.
 * This is the single entry point to fetch data from the network.
 *
 * @author Amanshu Raikwar
 * Created by Amanshu Raikwar on 30/04/18.
 */
interface NetworkDataManager {

    fun getAllPhotos(page: Int, orderBy: String, perPage: Int): List<Photo>?

    fun getAuthenticationManager(): AuthenticationManager

    fun readSpreadSheet(spreadsheetId: String,
                        spreadsheetRange: String): Observable<MutableList<MutableList<Any>>>

    fun readSpreadSheet(spreadsheetId: String,
                        spreadsheetRange: String,
                        googleAccountCredential: GoogleAccountCredential): Observable<MutableList<MutableList<Any>>>
}