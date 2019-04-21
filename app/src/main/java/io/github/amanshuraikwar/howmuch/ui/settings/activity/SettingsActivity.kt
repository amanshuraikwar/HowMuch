package io.github.amanshuraikwar.howmuch.ui.settings.activity

import android.os.Bundle
import dagger.Binds
import dagger.Module
import io.github.amanshuraikwar.howmuch.R
import io.github.amanshuraikwar.howmuch.base.ui.base.BaseActivity
import io.github.amanshuraikwar.howmuch.ui.settings.SettingsFragment
import javax.inject.Inject


class SettingsActivity
@Inject constructor(): BaseActivity<SettingsActivityContract.View, SettingsActivityContract.Presenter>(), SettingsActivityContract.View {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment_container)
    }

    override fun loadSettings() {

        supportFragmentManager
                .beginTransaction()
                .replace(R.id.containerFl, SettingsFragment())
                .commit()
    }

    @Module
    abstract class DiModule {
        @Binds
        abstract fun presenter(presenter: SettingsActivityContract.PresenterImpl)
                : SettingsActivityContract.Presenter
    }
}