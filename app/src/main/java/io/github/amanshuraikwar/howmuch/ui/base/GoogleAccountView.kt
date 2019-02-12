package io.github.amanshuraikwar.howmuch.ui.base

import android.accounts.Account
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential

interface GoogleAccountView {
    fun getGoogleAccountCredential(googleAccount: Account): GoogleAccountCredential
}