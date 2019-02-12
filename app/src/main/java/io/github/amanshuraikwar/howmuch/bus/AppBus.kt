package io.github.amanshuraikwar.howmuch.bus

import dagger.Module
import dagger.Provides
import io.github.amanshuraikwar.howmuch.model.Transaction
import io.github.amanshuraikwar.howmuch.ui.onboarding.OnboardingScreen
import io.reactivex.subjects.PublishSubject
import javax.inject.Singleton

/**
 * Event Bus of the App - implemented using RxJava's PublishSubject.
 *
 * @author Amanshu Raikwar
 */
class AppBus {

    val signInSuccessful: PublishSubject<Any> = PublishSubject.create<Any>()
    val onBoardingScreenState: PublishSubject<OnboardingScreen.State> = PublishSubject.create<OnboardingScreen.State>()
    val onLogout: PublishSubject<Any> = PublishSubject.create()
    val onCurrencyChanged: PublishSubject<String> = PublishSubject.create()
    val onExpenseUpdated: PublishSubject<Transaction> = PublishSubject.create()
    val onExpenseDeleted: PublishSubject<Transaction> = PublishSubject.create()

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