package com.yasinmaden.firebaseauthsample.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yasinmaden.firebaseauthsample.R

@Composable
fun SocialMediaButton(onClick: () -> Unit, ImageResId: Int, contentDescription: String){
    Button(
        onClick = { onClick() },
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF4F4F4)),
        shape = RoundedCornerShape(10.dp)
    ) {
        Image(
            painter = painterResource(ImageResId),
            contentDescription = contentDescription,
            Modifier.size(27.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SocialMediaButtonPreview(){
    SocialMediaButton({}, R.drawable.google, "google")
}