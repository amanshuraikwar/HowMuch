package io.github.amanshuraikwar.howmuch.bus

import io.github.amanshuraikwar.howmuch.data.local.room.transaction.Transaction
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

/**
 * Created by amanshuraikwar on 06/03/18.
 */
class AppBus {

    var onTransactionUpdated: PublishSubject<Transaction> = PublishSubject.create()
    var onTransactionDeleted: PublishSubject<Transaction> = PublishSubject.create()
    var onTransactionAdded: PublishSubject<Transaction> = PublishSubject.create()
    var onTransactionsChanged: PublishSubject<Transaction> = PublishSubject.create()
    var onSharedPrefsChanged: PublishSubject<String> = PublishSubject.create()
}