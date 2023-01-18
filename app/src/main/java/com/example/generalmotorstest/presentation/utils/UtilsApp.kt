package com.example.generalmotorstest.presentation.utils

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.example.generalmotorstest.core.MyApplication

object UtilsApp {

    fun convertUriToBitmap(uri: Uri): Bitmap? {
        @Suppress("DEPRECATION") val bitmap = try {
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

    fun getTypeContact(typeNum: String): String {
        var type = "Custom"

        when (typeNum) {
            "1" -> type = "Home"
            "2" -> type = "Mobile"
            "3" -> type = "Work"
            "4" -> type = "Work fax"
            "5" -> type = "Home fax"
            "6" -> type = "Pager"
            "7" -> type = "Other"
            "8" -> type = "Callback"
            "9" -> type = "Custom"
        }

        return type
    }

}
