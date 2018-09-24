package io.github.amanshuraikwar.howmuch.bus

import io.reactivex.subjects.PublishSubject

/**
 * Event Bus of the App - to be implemented using RxJava's PublishSubject.
 *
 * @author Amanshu Raikwar
 * Created by Amanshu Raikwar on 06/03/18.
 */
class AppBus {

    val signInSuccessful: PublishSubject<Any> = PublishSubject.create<Any>()
    val onBoardingScreenProceed: PublishSubject<Any> = PublishSubject.create<Any>()
}