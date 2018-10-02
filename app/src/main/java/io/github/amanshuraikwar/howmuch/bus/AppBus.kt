package io.github.amanshuraikwar.howmuch.bus

import io.github.amanshuraikwar.howmuch.ui.onboarding.OnboardingScreen
import io.reactivex.subjects.PublishSubject

/**
 * Event Bus of the App - to be implemented using RxJava's PublishSubject.
 *
 * @author Amanshu Raikwar
 * Created by Amanshu Raikwar on 06/03/18.
 */
class AppBus {

    val signInSuccessful: PublishSubject<Any> = PublishSubject.create<Any>()
    val onBoardingScreenState: PublishSubject<OnboardingScreen.State> = PublishSubject.create<OnboardingScreen.State>()
}