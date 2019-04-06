package io.github.amanshuraikwar.howmuch.protocol

import io.reactivex.Observable

interface WalletDataManager {

    fun getAllWallets(): Observable<Iterable<Wallet>>

    fun getWalletById(id: String): Observable<Wallet>

    fun addWallet(wallet: Wallet): Observable<Wallet>

    fun updateWallet(wallet: Wallet): Observable<Wallet>

    fun deleteWallet(wallet: Wallet): Observable<Wallet>

}