package com.example.generalmotorstest.presentation.utils

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.example.generalmotorstest.core.MyApplication

object UtilsApp {

    fun convertUriToBitmap(uri: Uri): Bitmap? {
        val bitmap = try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val source =
                    ImageDecoder.createSource(MyApplication.application.contentResolver, uri)
                ImageDecoder.decodeBitmap(source)
            } else {
                MediaStore.Images.Media.getBitmap(MyApplication.application.contentResolver, uri)
            }
        } catch (error: Throwable) {
            null
        }

        return bitmap
    }

}
