package com.example.generalmotorstest.presentation.utils

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.example.generalmotorstest.R
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
        var type = MyApplication.application.getString(R.string.custom)

        when (typeNum) {
            "1" -> type = MyApplication.application.getString(R.string.home)
            "2" -> type = MyApplication.application.getString(R.string.mobile)
            "3" -> type = MyApplication.application.getString(R.string.work)
            "4" -> type = MyApplication.application.getString(R.string.work_fax)
            "5" -> type = MyApplication.application.getString(R.string.home_fax)
            "6" -> type = MyApplication.application.getString(R.string.pager)
            "7" -> type = MyApplication.application.getString(R.string.other)
            "8" -> type = MyApplication.application.getString(R.string.callback)
            "9" -> type = MyApplication.application.getString(R.string.custom)
        }

        return type
    }

}
