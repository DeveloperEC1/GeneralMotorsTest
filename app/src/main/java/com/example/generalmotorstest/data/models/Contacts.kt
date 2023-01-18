package com.example.generalmotorstest.data.models

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable

data class ContactTypeData(
    var type: String?,
    var label: String?,
)

data class Contacts(
    var firstName: String?,
    var lastName: String?,
    var phoneNumberList: ArrayList<ContactTypeData>?,
    var emailList: ArrayList<ContactTypeData>?,
    val profileImage: Uri?,
) : Parcelable {
    @Suppress("DEPRECATION")
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        arrayListOf<ContactTypeData>().apply {
            parcel.readList(this, ContactTypeData::class.java.classLoader)
        },
        arrayListOf<ContactTypeData>().apply {
            parcel.readList(this, ContactTypeData::class.java.classLoader)
        },
        parcel.readParcelable(Uri::class.java.classLoader)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(firstName)
        parcel.writeString(lastName)
        parcel.writeList(phoneNumberList)
        parcel.writeList(emailList)
        parcel.writeParcelable(profileImage, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Contacts> {
        override fun createFromParcel(parcel: Parcel): Contacts {
            return Contacts(parcel)
        }

        override fun newArray(size: Int): Array<Contacts?> {
            return arrayOfNulls(size)
        }
    }

}
