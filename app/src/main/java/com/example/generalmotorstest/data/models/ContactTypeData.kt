package com.example.generalmotorstest.data.models

import android.os.Parcel
import android.os.Parcelable

data class ContactTypeData(
    var type: String?,
    var label: String?,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(type)
        parcel.writeString(label)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ContactTypeData> {
        override fun createFromParcel(parcel: Parcel): ContactTypeData {
            return ContactTypeData(parcel)
        }

        override fun newArray(size: Int): Array<ContactTypeData?> {
            return arrayOfNulls(size)
        }
    }

}
