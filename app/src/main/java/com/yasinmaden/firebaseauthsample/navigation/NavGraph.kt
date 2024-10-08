package com.yasinmaden.firebaseauthsample.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.yasinmaden.firebaseauthsample.ui.main.MainScreen
import com.yasinmaden.firebaseauthsample.ui.login.LoginScreen
import com.yasinmaden.firebaseauthsample.ui.login.LoginViewModel
import com.yasinmaden.firebaseauthsample.ui.main.MainViewModel
import com.yasinmaden.firebaseauthsample.ui.sign_up.SignUpScreen
import com.yasinmaden.firebaseauthsample.ui.sign_up.SignUpViewModel

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "login") {

        composable("login") {
            val viewModel: LoginViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val uiEffect = viewModel.uiEffect
            LoginScreen(
                uiState = uiState,
                uiEffect = uiEffect,
                onAction = viewModel::onAction,
                onNavigateMainScreen = { navController.navigate("main") },
                onNavigateSignUpScreen = {navController.navigate("sign_up")}
            )
        }

        composable("main") {
            val viewModel: MainViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val uiEffect = viewModel.uiEffect
            MainScreen(
                uiState = uiState,
                uiEffect = uiEffect,
                onAction = viewModel::onAction,
                onNavigateLoginScreen = { navController.navigate("login") }
            )
        }

        composable("sign_up") {
            val viewModel: SignUpViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val uiEffect = viewModel.uiEffect
            SignUpScreen(
                uiState = uiState,
                uiEffect = uiEffect,
                onAction = viewModel::onAction,
                onNavigateMainScreen = { navController.navigate("main") },
                onNavigateLoginScreen = { navController.navigate("login") },
                onBackButtonClick = { navController.popBackStack() }
            )
        }


    }
}