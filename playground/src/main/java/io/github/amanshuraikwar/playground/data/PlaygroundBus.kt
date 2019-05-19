package io.github.amanshuraikwar.playground.data

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

class PlaygroundBus {

}

@Module
class PlaygroundBusModule {

    @Singleton
    @Provides
    fun a(): PlaygroundBus = PlaygroundBus()
}