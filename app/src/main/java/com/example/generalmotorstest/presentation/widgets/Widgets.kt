package com.example.generalmotorstest.presentation.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.generalmotorstest.data.models.Contacts
import com.example.generalmotorstest.presentation.utils.UtilsApp.convertUriToBitmap

@Composable
fun EmptyViewWidget() {

}

@Composable
fun SpacerWidget(spaceInt: Int) {
    Spacer(modifier = Modifier.height(spaceInt.dp))
}

@Composable
fun TextContactWidget(text: String) {
    Text(
        text,
        fontSize = 20.sp,
    )
}

@Composable
fun ProfileImageContactWidget(contact: Contacts) {
    val uri = contact.profileImage

    if (uri != null) {
        val bitmap = convertUriToBitmap(uri)

        if (bitmap != null) {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
            )
        } else {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .background(Color.Cyan, shape = CircleShape)
                    .size(50.dp),
            ) {
                Text(
                    contact.firstName!![0].toString(),
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Composable
fun ProgressDialogWidget() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(100.dp)
            .background(Color.White, shape = RoundedCornerShape(8.dp))
    ) {
        CircularProgressIndicator()
    }
}
