package com.yasinmaden.firebaseauthsample.ui.sign_up

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
class SignUpViewModel @Inject constructor(
    private val authRepository: AuthRepository

) : ViewModel() {
    private val _uiState = MutableStateFlow(SignUpContract.UiState())
    val uiState: StateFlow<SignUpContract.UiState> = _uiState.asStateFlow()

    private val _uiEffect by lazy { Channel<SignUpContract.UiEffect>() }
    val uiEffect: Flow<SignUpContract.UiEffect> by lazy { _uiEffect.receiveAsFlow() }


    init {
        isUserLoggedIn()
    }

    fun onAction(uiAction: SignUpContract.UiAction) {
        when (uiAction) {
            is SignUpContract.UiAction.SignUpClick -> signUp()
            is SignUpContract.UiAction.ChangeEmail -> updateUiState { copy(email = uiAction.email) }
            is SignUpContract.UiAction.ChangePassword -> updateUiState { copy(password = uiAction.password) }
            is SignUpContract.UiAction.ChangeConfirmPassword -> updateUiState { copy(confirmPassword = uiAction.confirmPassword) }
            is SignUpContract.UiAction.ChangeCheckbox -> updateUiState { copy(checkboxState = uiAction.checkboxState) }
        }

    }

    private fun isUserLoggedIn() = viewModelScope.launch {
        if (authRepository.isUserLoggedIn()) {
            emitUiEffect(SignUpContract.UiEffect.GoToMainScreen)
        }
    }
    private fun signUp() = viewModelScope.launch {
        when (val result = authRepository.signUp(uiState.value.email, uiState.value.password)) {
            is Resource.Success -> {
                emitUiEffect(SignUpContract.UiEffect.ShowToast(result.data))
                emitUiEffect(SignUpContract.UiEffect.GoToMainScreen)
            }

            is Resource.Error -> {
                emitUiEffect(SignUpContract.UiEffect.ShowToast(result.exception.message.orEmpty()))
            }
        }
    }



    private fun updateUiState(block: SignUpContract.UiState.() -> SignUpContract.UiState) {
        _uiState.update(block)
    }

    private suspend fun emitUiEffect(uiEffect: SignUpContract.UiEffect) {
        _uiEffect.send(uiEffect)
    }
}

object SignUpContract {
    data class UiState(
        val isLoading: Boolean = false,
        val email: String = "",
        val password: String = "",
        val confirmPassword: String = "",
        val checkboxState: Boolean = false,
    )

    sealed class UiAction {
        data object SignUpClick : UiAction()
        data class ChangeEmail(val email: String) : UiAction()
        data class ChangePassword(val password: String) : UiAction()
        data class ChangeConfirmPassword(val confirmPassword: String) : UiAction()
        data class ChangeCheckbox(val checkboxState: Boolean) : UiAction()
    }

    sealed class UiEffect {
        data class ShowToast(val message: String) : UiEffect()
        data object GoToMainScreen : UiEffect()
    }
}