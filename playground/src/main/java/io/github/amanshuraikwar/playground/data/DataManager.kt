package io.github.amanshuraikwar.playground.data

import io.github.amanshuraikwar.howmuch.protocol.UserDataManager
import io.reactivex.Observable

interface DataManager : UserDataManager {

    fun getAllSongs(): Observable<List<Song>>
}