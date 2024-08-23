package com.yasinmaden.firebaseauthsample.ui.main

object MainContract {
    data class UiState(
        val isLoading: Boolean = true,
        val userName: String = "",
        val userEmail: String = "",
        val userProfilePic: String = ""
    )

    sealed class UiAction {
        data object SignOutClick : UiAction()
    }

    sealed class UiEffect {
        data object GoToLoginScreen : UiEffect()
    }
}
