package com.yasinmaden.firebaseauthsample.ui.login


import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.yasinmaden.firebaseauthsample.R
import com.yasinmaden.firebaseauthsample.ui.theme.displayFontFamily
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun LoginScreen(
    uiState: LoginContract.UiState,
    uiEffect: Flow<LoginContract.UiEffect>,
    onAction: (LoginContract.UiAction) -> Unit,
    onNavigateMainScreen: () -> Unit,
    onNavigateSignUpScreen: () -> Unit,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(uiEffect, lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            uiEffect.collect { effect ->
                when (effect) {
                    is LoginContract.UiEffect.ShowToast -> {
                        Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                    }

                    is LoginContract.UiEffect.GoToMainScreen -> {
                        onNavigateMainScreen()
                    }

                    is LoginContract.UiEffect.GoToSignUpScreen -> {
                        onNavigateSignUpScreen()
                    }
                }
            }
        }
    }


    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo), contentDescription = "logo",
            Modifier.size(54.dp)
        )
        Spacer(Modifier.height(40.dp))
        Text(
            text = "Sign in your account",
            fontFamily = displayFontFamily,
            fontSize = 26.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.height(16.dp))
        EmailAndPasswordContent(
            email = uiState.email,
            password = uiState.password,
            onEmailChange = { onAction(LoginContract.UiAction.ChangeEmail(it)) },
            onPasswordChange = { onAction(LoginContract.UiAction.ChangePassword(it)) },
            onSignInClick = { onAction(LoginContract.UiAction.SignInClick) },
        )
        Spacer(Modifier.height(24.dp))
        Text(text = "or sign in with", color = Color(0xFF888888), fontSize = 16.sp)
        Spacer(Modifier.height(24.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 48.dp, end = 48.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { /*TODO*/ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF4F4F4)),
                shape = RoundedCornerShape(10.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.google),
                    contentDescription = "facebook",
                    Modifier.size(27.dp),
                )
            }
            Button(
                onClick = { /*TODO*/ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF4F4F4)),
                shape = RoundedCornerShape(10.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.facebook),
                    contentDescription = "facebook",
                    Modifier.size(27.dp),
                )
            }
            Button(
                onClick = { /*TODO*/ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF4F4F4)),
                shape = RoundedCornerShape(10.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.twitter),
                    contentDescription = "facebook",
                    Modifier.size(27.dp),
                )
            }
        }

        Spacer(Modifier.height(32.dp))
        SignUpContent(onSignUpClick = { onNavigateSignUpScreen() })


    }


}


@Composable
fun SignUpContent(onSignUpClick: () -> Unit) {
    Row {
        Text(text = "Don't have an account?", color = Color(0xFF888888), fontSize = 16.sp)
        Spacer(Modifier.width(5.dp))

        Text(
            text = "SIGN UP",
            modifier = Modifier.clickable { onSignUpClick() },
            color = Color(0xFF00B140),
            fontSize = 16.sp
        )

    }
}

@Composable
fun EmailAndPasswordContent(
    email: String,
    password: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSignInClick: () -> Unit,
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
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 48.dp, end = 48.dp),
        onClick = { onSignInClick() },
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00B140))
    ) {
        Text(text = "SIGN IN", fontSize = 15.sp)
    }

}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(
        uiState = LoginContract.UiState(),
        uiEffect = emptyFlow(),
        onAction = {},
        onNavigateMainScreen = {},
        onNavigateSignUpScreen = {}
    )
}

