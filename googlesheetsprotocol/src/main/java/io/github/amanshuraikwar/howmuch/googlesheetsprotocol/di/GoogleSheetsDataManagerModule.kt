package io.github.amanshuraikwar.howmuch.googlesheetsprotocol.di

import dagger.Binds
import dagger.Module
import io.github.amanshuraikwar.howmuch.googlesheetsprotocol.data.GoogleSheetsUserDataManager
import io.github.amanshuraikwar.howmuch.googlesheetsprotocol.api.SheetsApiDataSource
import io.github.amanshuraikwar.howmuch.googlesheetsprotocol.api.SheetsDataSource
import io.github.amanshuraikwar.howmuch.googlesheetsprotocol.data.GoogleSheetsCategoriesDataManager
import io.github.amanshuraikwar.howmuch.googlesheetsprotocol.data.GoogleSheetsTransactionDataManager
import io.github.amanshuraikwar.howmuch.googlesheetsprotocol.data.GoogleSheetsWalletDataManager
import io.github.amanshuraikwar.howmuch.googlesheetsprotocol.local.LocalDataManager
import io.github.amanshuraikwar.howmuch.googlesheetsprotocol.local.LocalDataManagerImpl
import io.github.amanshuraikwar.howmuch.protocol.CategoriesDataManager
import io.github.amanshuraikwar.howmuch.protocol.TransactionDataManager
import io.github.amanshuraikwar.howmuch.protocol.UserDataManager
import io.github.amanshuraikwar.howmuch.protocol.WalletDataManager

@Module
abstract class GoogleSheetsDataManagerModule {

    @Binds
    abstract fun a(a: SheetsApiDataSource): SheetsDataSource

    @Binds
    abstract fun b(a: LocalDataManagerImpl): LocalDataManager

    @Binds
    abstract fun c(a: GoogleSheetsUserDataManager): UserDataManager

    @Binds
    abstract fun d(a: GoogleSheetsTransactionDataManager): TransactionDataManager

    @Binds
    abstract fun e(a: GoogleSheetsCategoriesDataManager): CategoriesDataManager

    @Binds
    abstract fun f(a: GoogleSheetsWalletDataManager): WalletDataManager
}