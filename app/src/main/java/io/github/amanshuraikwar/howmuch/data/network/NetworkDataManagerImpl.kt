package io.github.amanshuraikwar.howmuch.data.network

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import io.github.amanshuraikwar.howmuch.data.network.sheets.AuthenticationManager
import io.github.amanshuraikwar.howmuch.data.network.sheets.SheetsAPIDataSource
import io.github.amanshuraikwar.howmuch.data.network.sheets.SheetsDataSource
import io.github.amanshuraikwar.howmuch.model.Photo
import io.github.amanshuraikwar.howmuch.util.Util
import io.reactivex.Observable
import java.io.IOException
import javax.inject.Inject

/**
 * Implementation of Network Data Manager.
 *
 * @author Amanshu Raikwar
 * Created by Amanshu Raikwar on 30/04/18.
 */
class NetworkDataManagerImpl @Inject constructor(
        private val apiInterface: ApiInterface,
        private val authenticationManager: AuthenticationManager,
        private val httpTransport: HttpTransport,
        private val jsonFactory: JacksonFactory) : NetworkDataManager {

    private val TAG = Util.getTag(this)

    private var sheetsDataSource: SheetsDataSource? = null

    @Throws(IOException::class)
    override fun getAllPhotos(page: Int, orderBy: String, perPage: Int): List<Photo>? {
        return apiInterface.getAllPhotos(page, orderBy, perPage).execute().body()
    }

    override fun getAuthenticationManager() = authenticationManager

    override fun readSpreadSheet(spreadsheetId: String, spreadsheetRange: String)
            : Observable<MutableList<MutableList<Any>>> {
        return sheetsDataSource!!.readSpreadSheet(spreadsheetId, spreadsheetRange)
    }

    override fun readSpreadSheet(spreadsheetId: String,
                                 spreadsheetRange: String,
                                 googleAccountCredential: GoogleAccountCredential)
            : Observable<MutableList<MutableList<Any>>> {
        sheetsDataSource = SheetsAPIDataSource(googleAccountCredential, httpTransport, jsonFactory)
        return sheetsDataSource!!.readSpreadSheet(spreadsheetId, spreadsheetRange)
    }
}