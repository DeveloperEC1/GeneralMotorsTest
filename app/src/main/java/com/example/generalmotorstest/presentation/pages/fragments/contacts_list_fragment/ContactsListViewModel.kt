package com.example.generalmotorstest.presentation.pages.fragments.contacts_list_fragment

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.provider.ContactsContract
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.generalmotorstest.core.MyApplication
import com.example.generalmotorstest.data.models.Contacts

class ContactsListViewModel : ViewModel() {

    val contactsList = mutableStateListOf<Contacts>()
//    var isLoading = mutableStateOf(true)

    fun setContactsToContactsList() {
        contactsList.clear()
        contactsList.addAll(getAllContacts())

//        isLoading = true
    }

    @SuppressLint("Range")
    fun getAllContacts(): ArrayList<Contacts> {
        val contactsModelArr = ArrayList<Contacts>()
        val cr: ContentResolver = MyApplication.application.contentResolver
        val cursor = cr.query(
            ContactsContract.Contacts.CONTENT_URI, arrayOf(
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.HAS_PHONE_NUMBER
            ),
            null, null, null
        )

        if (cursor != null && cursor.moveToFirst()) {
            do {
                val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                val name =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                val hasPhone =
                    cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))
                var email: String? = null
                val ce = cr.query(
                    ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", arrayOf(id), null
                )

                if (ce != null && ce.moveToFirst()) {
                    email =
                        ce.getString(ce.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA))
                    ce.close()
                }

                var phone: String? = null

                if (hasPhone > 0) {
                    val cp = cr.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        arrayOf(id),
                        null
                    )
                    if (cp != null && cp.moveToFirst()) {
                        phone =
                            cp.getString(cp.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                        cp.close()
                    }
                }

                if (phone != null && phone != "") {
                    val contact = Contacts(
                        name,
                        name,
                        phone,
                        email ?: "",
                    )
                    contactsModelArr.add(contact)
                }
            } while (cursor.moveToNext())

            cursor.close()
        }

        return contactsModelArr
    }

}