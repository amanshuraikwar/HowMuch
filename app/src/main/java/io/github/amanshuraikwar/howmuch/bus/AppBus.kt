package io.github.amanshuraikwar.howmuch.bus

import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

/**
 * Created by amanshuraikwar on 06/03/18.
 */
class AppBus {

    var onTransactionAdded: PublishSubject<Boolean> = PublishSubject.create()
}