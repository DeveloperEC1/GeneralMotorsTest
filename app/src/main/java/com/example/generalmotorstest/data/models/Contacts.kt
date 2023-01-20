package com.example.generalmotorstest.data.models

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable

data class Contacts(
    var fullName: String?,
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
        parcel.readString(),
        parcel.createTypedArrayList(ContactTypeData.CREATOR),
        parcel.createTypedArrayList(ContactTypeData.CREATOR),
        parcel.readParcelable(Uri::class.java.classLoader)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(fullName)
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
