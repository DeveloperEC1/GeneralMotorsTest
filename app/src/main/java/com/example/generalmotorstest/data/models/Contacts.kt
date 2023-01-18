package com.example.generalmotorstest.data.models

import android.net.Uri

data class Contacts(
    var fullName: String?,
    var phoneNumber: String,
    var email: String,
    val image: Uri?,
)