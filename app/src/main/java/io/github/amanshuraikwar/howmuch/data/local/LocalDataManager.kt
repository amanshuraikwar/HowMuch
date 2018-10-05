package io.github.amanshuraikwar.howmuch.data.local

import io.github.amanshuraikwar.howmuch.data.local.prefs.PrefsDataManager
import io.github.amanshuraikwar.howmuch.data.local.sqlite.SqliteDataManager

interface LocalDataManager : PrefsDataManager, SqliteDataManager