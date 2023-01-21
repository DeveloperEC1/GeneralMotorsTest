package com.example.generalmotorstest.presentation.utils

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.example.generalmotorstest.R
import com.example.generalmotorstest.core.Constants.Companion.CALLBACK
import com.example.generalmotorstest.core.Constants.Companion.CUSTOM
import com.example.generalmotorstest.core.Constants.Companion.HOME
import com.example.generalmotorstest.core.Constants.Companion.HOME_FAX
import com.example.generalmotorstest.core.Constants.Companion.MOBILE
import com.example.generalmotorstest.core.Constants.Companion.OTHER
import com.example.generalmotorstest.core.Constants.Companion.PAGER
import com.example.generalmotorstest.core.Constants.Companion.WORK
import com.example.generalmotorstest.core.Constants.Companion.WORK_FAX
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
        var stringId = R.string.unknown

        when (typeNum) {
            HOME -> stringId = R.string.home
            MOBILE -> stringId = R.string.mobile
            WORK -> stringId = R.string.work
            WORK_FAX -> stringId = R.string.work_fax
            HOME_FAX -> stringId = R.string.home_fax
            PAGER -> stringId = R.string.pager
            OTHER -> stringId = R.string.other
            CALLBACK -> stringId = R.string.callback
            CUSTOM -> stringId = R.string.custom
        }

        return MyApplication.application.getString(stringId)
    }

}
