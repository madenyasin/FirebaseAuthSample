package com.yasinmaden.firebaseauthsample.ui.sign_up

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.yasinmaden.firebaseauthsample.ui.theme.displayFontFamily
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun SignUpScreen(
    uiState: SignUpContract.UiState,
    uiEffect: Flow<SignUpContract.UiEffect>,
    onAction: (SignUpContract.UiAction) -> Unit,
    onNavigateMainScreen: () -> Unit,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(uiEffect, lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            uiEffect.collect { effect ->
                when (effect) {
                    is SignUpContract.UiEffect.ShowToast -> {
                        Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                    }

                    is SignUpContract.UiEffect.GoToMainScreen -> {
                        onNavigateMainScreen()
                    }
                }
            }
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Create your account", fontFamily = displayFontFamily,
            fontSize = 26.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.height(16.dp))
        EmailAndPasswordContent(
            email = uiState.email,
            password = uiState.password,
            confirmPassword = uiState.confirmPassword,
            onEmailChange = { onAction(SignUpContract.UiAction.ChangeEmail(it)) },
            onPasswordChange = { onAction(SignUpContract.UiAction.ChangePassword(it)) },
            onConfirmPasswordChange = { onAction(SignUpContract.UiAction.ChangeConfirmPassword(it)) },
            onSignUpClick = { onAction(SignUpContract.UiAction.SignUpClick) },
        )
    }

}

@Composable
fun EmailAndPasswordContent(
    email: String,
    password: String,
    confirmPassword: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onSignUpClick: () -> Unit,
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 48.dp, end = 48.dp),
        value = email,
        maxLines = 1,
        placeholder = { Text("Email") },
        onValueChange = onEmailChange,
    )
    Spacer(Modifier.height(32.dp))

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 48.dp, end = 48.dp),
        value = password,
        maxLines = 1,
        placeholder = { Text("Password") },
        onValueChange = onPasswordChange,
    )
    Spacer(Modifier.height(32.dp))

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 48.dp, end = 48.dp),
        value = confirmPassword,
        maxLines = 1,
        placeholder = { Text("Confirm Password") },
        onValueChange = onConfirmPasswordChange,
    )
    Spacer(Modifier.height(16.dp))
    PolicyContent()
    Spacer(Modifier.height(16.dp))

    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 48.dp, end = 48.dp),
        onClick = {onSignUpClick()},
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00B140))
    ) {
        Text(text = "SIGN UP", fontSize = 15.sp)
    }


}


@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    SignUpScreen(
        uiState = SignUpContract.UiState(),
        uiEffect = emptyFlow(),
        onAction = {},
        onNavigateMainScreen = {},
    )
}

@Composable
fun PolicyContent() {
    Row {
        Text(text = "I understood the", color = Color(0xFF888888), fontSize = 16.sp)
        Spacer(Modifier.width(5.dp))
        Text(
            text = "terms & policy.",
            modifier = Modifier.clickable {  },
            color = Color(0xFF00B140),
            fontSize = 16.sp
        )
    }
}