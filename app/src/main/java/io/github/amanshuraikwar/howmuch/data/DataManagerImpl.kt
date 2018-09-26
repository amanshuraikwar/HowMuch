package io.github.amanshuraikwar.howmuch.data

import android.content.Context
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import io.github.amanshuraikwar.howmuch.data.network.NetworkDataManager
import io.github.amanshuraikwar.howmuch.data.network.sheets.AuthenticationManager
import io.github.amanshuraikwar.howmuch.di.ApplicationContext
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Implementation of the Data Manager for the app.
 *
 * @author Amanshu Raikwar
 * Created by Amanshu Raikwar on 07/03/18.
 */
class DataManagerImpl @Inject constructor(
        @ApplicationContext private val context: Context,
        private val networkDataManager: NetworkDataManager) : DataManager {

    override fun getAllPhotos(page: Int, orderBy: String, perPage: Int)
            = networkDataManager.getAllPhotos(page, orderBy, perPage)

    override fun getAuthenticationManager()
            = networkDataManager.getAuthenticationManager()

    override fun readSpreadSheet(spreadsheetId: String,
                                 spreadsheetRange: String)
            : Observable<MutableList<MutableList<Any>>>
            = networkDataManager.readSpreadSheet(spreadsheetId, spreadsheetRange)

    override fun readSpreadSheet(spreadsheetId: String,
                                 spreadsheetRange: String,
                                 googleAccountCredential: GoogleAccountCredential)
            : Observable<MutableList<MutableList<Any>>>
            = networkDataManager.readSpreadSheet(spreadsheetId, spreadsheetRange, googleAccountCredential)

    override fun appendToSpreadSheet(spreadsheetId: String,
                                     spreadsheetRange: String,
                                     valueInputOption: String,
                                     values: List<List<Any>>)
            : Observable<Boolean>
            = networkDataManager.appendToSpreadSheet(spreadsheetId, spreadsheetRange, valueInputOption, values)


    override fun appendToSpreadSheet(spreadsheetId: String,
                                     spreadsheetRange: String,
                                     valueInputOption: String,
                                     values: List<List<Any>>,
                                     googleAccountCredential: GoogleAccountCredential)
            : Observable<Boolean>
            = networkDataManager.appendToSpreadSheet(spreadsheetId, spreadsheetRange, valueInputOption, values, googleAccountCredential)

    override fun createSpeadSheet() = networkDataManager.createSpeadSheet()

    override fun createSpeadSheet(googleAccountCredential: GoogleAccountCredential)
            = networkDataManager.createSpeadSheet(googleAccountCredential)
}