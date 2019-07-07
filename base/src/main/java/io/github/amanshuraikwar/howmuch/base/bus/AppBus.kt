package io.github.amanshuraikwar.howmuch.base.bus

import dagger.Module
import dagger.Provides
import io.github.amanshuraikwar.howmuch.protocol.Transaction
import io.reactivex.subjects.PublishSubject
import javax.inject.Singleton

/**
 * Event Bus of the App - implemented using RxJava's PublishSubject.
 *
 * @author Amanshu Raikwar
 */
class AppBus {

    val onSignInSuccessful: PublishSubject<Any> = PublishSubject.create<Any>()
    val onTransactionAdded: PublishSubject<Any> = PublishSubject.create<Any>()
    val onSignOut: PublishSubject<Any> = PublishSubject.create()
    val onAddExpenseInitFailed: PublishSubject<Any> = PublishSubject.create()
    val onAddExpenseProcessCompleted: PublishSubject<Any> = PublishSubject.create()

}

/**
 * Dagger Module to provide Event Bus related instances.
 *
 * @author Amanshu Raikwar
 * Created by Amanshu Raikwar on 07/03/18.
 */
@Module
class AppBusModule {

    @Singleton
    @Provides
    fun appBus(): AppBus = AppBus()
}