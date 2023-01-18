package com.example.generalmotorstest.data.models

import android.net.Uri

data class Contacts(
    var firstName: String?,
    var lastName: String?,
    var phoneNumber: String,
    var email: String,
    val profileImage: Uri?,
)