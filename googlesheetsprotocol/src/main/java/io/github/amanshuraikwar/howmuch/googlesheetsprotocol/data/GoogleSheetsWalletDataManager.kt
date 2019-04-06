package io.github.amanshuraikwar.howmuch.googlesheetsprotocol.data

import io.github.amanshuraikwar.howmuch.protocol.Wallet
import io.github.amanshuraikwar.howmuch.protocol.WalletDataManager
import io.reactivex.Observable
import javax.inject.Inject

class GoogleSheetsWalletDataManager
@Inject constructor()
    : WalletDataManager {

    override fun getAllWallets(): Observable<Iterable<Wallet>> {
        // todo
        return Observable.fromCallable { listOf(Wallet("", "", 0.0)) }
    }

    override fun getWalletById(id: String): Observable<Wallet> {
        // todo
        return Observable.fromCallable { Wallet("", "", 0.0) }
    }

    override fun addWallet(wallet: Wallet): Observable<Wallet> {
        // todo
        return Observable.fromCallable { Wallet("", "", 0.0) }
    }

    override fun updateWallet(wallet: Wallet): Observable<Wallet> {
        // todo
        return Observable.fromCallable { Wallet("", "", 0.0) }
    }

    override fun deleteWallet(wallet: Wallet): Observable<Wallet> {
        // todo
        return Observable.fromCallable { Wallet("", "", 0.0) }
    }
}