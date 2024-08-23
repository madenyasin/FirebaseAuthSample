package com.yasinmaden.firebaseauthsample.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yasinmaden.firebaseauthsample.common.Resource
import com.yasinmaden.firebaseauthsample.data.repository.AuthRepository
import com.yasinmaden.firebaseauthsample.ui.main.MainContract.UiEffect
import com.yasinmaden.firebaseauthsample.ui.main.MainContract.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _uiEffect by lazy { Channel<UiEffect>() }
    val uiEffect: Flow<UiEffect> by lazy { _uiEffect.receiveAsFlow() }

    init {
        loadUserData()
    }

    fun onAction(uiAction: MainContract.UiAction) {
        when (uiAction) {
            is MainContract.UiAction.SignOutClick -> signOut()
        }
    }

    private fun loadUserData() = viewModelScope.launch {
        updateUiState { copy(isLoading = true) }
        when (val result = authRepository.getCurrentUserData()) {
            is Resource.Success -> {
                updateUiState {
                    copy(
                        isLoading = false,
                        userName = result.data.name,
                        userEmail = result.data.email,
                        userProfilePic = result.data.profilePicUrl
                    )
                }
            }
            is Resource.Error -> {
                emitUiEffect(UiEffect.GoToLoginScreen)
            }
        }
    }

    private fun signOut() = viewModelScope.launch {
        authRepository.signOut()
        emitUiEffect(UiEffect.GoToLoginScreen)
    }

    private fun updateUiState(block: UiState.() -> UiState) {
        _uiState.update(block)
    }

    private suspend fun emitUiEffect(uiEffect: UiEffect) {
        _uiEffect.send(uiEffect)
    }
}
