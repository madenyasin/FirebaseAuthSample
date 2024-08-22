package com.yasinmaden.firebaseauthsample.ui.login

import com.yasinmaden.firebaseauthsample.ui.login.LoginContract.UiEffect


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yasinmaden.firebaseauthsample.common.Resource
import com.yasinmaden.firebaseauthsample.data.repository.AuthRepository

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository

) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginContract.UiState())
    val uiState: StateFlow<LoginContract.UiState> = _uiState.asStateFlow()

    private val _uiEffect by lazy { Channel<UiEffect>() }
    val uiEffect: Flow<UiEffect> by lazy { _uiEffect.receiveAsFlow() }


    init {
        isUserLoggedIn()
    }

    fun onAction(uiAction: LoginContract.UiAction) {
        when (uiAction) {
            is LoginContract.UiAction.SignInClick -> signIn()
            is LoginContract.UiAction.SignUpClick -> signUp()
            is LoginContract.UiAction.ChangeEmail -> updateUiState { copy(email = uiAction.email) }
            is LoginContract.UiAction.ChangePassword -> updateUiState { copy(password = uiAction.password) }
        }

    }

    private fun isUserLoggedIn() = viewModelScope.launch {
        if (authRepository.isUserLoggedIn()) {
            emitUiEffect(UiEffect.GoToMainScreen)
        }
    }
    private fun signUp() = viewModelScope.launch {
        when (val result = authRepository.signUp(uiState.value.email, uiState.value.password)) {
            is Resource.Success -> {
                emitUiEffect(UiEffect.ShowToast(result.data))
                emitUiEffect(UiEffect.GoToMainScreen)
            }

            is Resource.Error -> {
                emitUiEffect(UiEffect.ShowToast(result.exception.message.orEmpty()))
            }
        }
    }

    private fun signIn() = viewModelScope.launch {
        when (val result = authRepository.signIn(uiState.value.email, uiState.value.password)) {
            is Resource.Success -> {
                emitUiEffect(UiEffect.ShowToast(result.data))
                emitUiEffect(UiEffect.GoToMainScreen)
            }

            is Resource.Error -> {
                emitUiEffect(UiEffect.ShowToast(result.exception.message.orEmpty()))
            }
        }
    }


    private fun updateUiState(block: LoginContract.UiState.() -> LoginContract.UiState) {
        _uiState.update(block)
    }

    private suspend fun emitUiEffect(uiEffect: UiEffect) {
        _uiEffect.send(uiEffect)
    }
}

object LoginContract {
    data class UiState(
        val isLoading: Boolean = false,
        val email: String = "",
        val password: String = "",
    )

    sealed class UiAction {
        data object SignInClick : UiAction()
        data object SignUpClick : UiAction()
        data class ChangeEmail(val email: String) : UiAction()
        data class ChangePassword(val password: String) : UiAction()
    }

    sealed class UiEffect {
        data class ShowToast(val message: String) : UiEffect()
        data object GoToMainScreen : UiEffect()
        data object GoToSignUpScreen : UiEffect()
    }
}