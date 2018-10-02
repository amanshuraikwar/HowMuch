package io.github.amanshuraikwar.howmuch.ui.onboarding

interface OnboardingScreen {

    enum class State {
        SIGN_IN_COMPLETE,
        CREATE_SHEET_COMPLETE,
        CREATE_SHEET_INVALID_STATE,
        ONBOARDING_COMPLETE
    }

    fun selected()
}