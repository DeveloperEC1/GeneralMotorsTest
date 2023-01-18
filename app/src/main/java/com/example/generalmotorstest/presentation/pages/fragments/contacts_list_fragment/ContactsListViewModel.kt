package com.example.generalmotorstest.presentation.pages.fragments.contacts_list_fragment

import android.annotation.SuppressLint
import android.content.ContentUris
import android.net.Uri
import android.provider.ContactsContract
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.generalmotorstest.core.MyApplication
import com.example.generalmotorstest.data.models.Contacts
import java.util.*
import kotlin.collections.ArrayList

class ContactsListViewModel : ViewModel() {

    var contactsList = mutableStateListOf<Contacts>()
    var searchSrt = mutableStateOf("")
    var isLoading = mutableStateOf(true)

    fun setContactsToContactsList(contactsListVar: ArrayList<Contacts>) {
        isLoading.value = true

        contactsList.clear()
        contactsList.addAll(contactsListVar)

        isLoading.value = false
    }

    fun searchedItems(searchedText: String) {
        if (searchedText.isNotEmpty()) {
            val resultList = ArrayList<Contacts>()

            for (data in contactsList) {
                if (data.fullName?.lowercase(Locale.getDefault())
                        ?.contains(searchedText, ignoreCase = true) == true
                ) {
                    resultList.add(data)
                }
            }

            setContactsToContactsList(resultList)
        } else {
            setContactsToContactsList(getAllContacts())
        }
    }

    @SuppressLint("Range")
    fun getAllContacts(): ArrayList<Contacts> {
        val contactsModelArr = ArrayList<Contacts>()
        val cr = MyApplication.application.contentResolver
        val cursor = cr.query(
            ContactsContract.Contacts.CONTENT_URI, arrayOf(
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.HAS_PHONE_NUMBER,
            ),
            null, null, null
        )

        if (cursor != null && cursor.moveToFirst()) {
            do {
                val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                val hasPhone =
                    cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))

                if (hasPhone > 0) {
                    var fullName = ""
                    var phone = ""
                    var email = ""
                    var image: Uri? = null

                    try {
                        fullName =
                            cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                    } catch (_: Exception) {

                    }

                    try {
                        val cp = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            arrayOf(id),
                            null,
                        )
                        if (cp != null && cp.moveToFirst()) {
                            phone =
                                cp.getString(cp.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                            cp.close()
                        }
                    } catch (_: Exception) {

                    }

                    try {
                        val ce = cr.query(
                            ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                            arrayOf(id),
                            null,
                        )
                        if (ce != null && ce.moveToFirst()) {
                            email =
                                ce.getString(ce.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA))
                            ce.close()
                        }
                    } catch (_: Exception) {

                    }

                    try {
//                    val ci = cr.query(
//                        ContactsContract.Data.CONTENT_URI,
//                        null,
//                        ContactsContract.Data.CONTACT_ID + "=" + id + " AND "
//                                + ContactsContract.Data.MIMETYPE + "='"
//                                + ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE + "'",
//                        null,
//                        null,
//                    )
//
//                    if (ci != null && ci.moveToFirst()) {
//                        phone =
//                            ci.getString(ci.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
//                        ci.close()
//                    }

                        val person = ContentUris.withAppendedId(
                            ContactsContract.Contacts.CONTENT_URI,
                            id.toLong()
                        )
                        image = Uri.withAppendedPath(
                            person,
                            ContactsContract.Contacts.Photo.CONTENT_DIRECTORY
                        )
                    } catch (_: Exception) {

                    }

                    val contact = Contacts(
                        fullName,
                        phone,
                        email,
                        image
                    )
                    contactsModelArr.add(contact)
                }
            } while (cursor.moveToNext())
            cursor.close()
        }

        return contactsModelArr
    }

}