package com.example.generalmotorstest.data.models

import android.net.Uri
import java.io.Serializable

data class ContactTypeData(
    var type: String?,
    var label: String?,
)

data class Contacts(
    var firstName: String?,
    var lastName: String?,
    var phoneNumberList: List<ContactTypeData>?,
    var emailList: List<ContactTypeData>?,
    val profileImage: Uri?,
) : Serializable