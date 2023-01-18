package com.example.generalmotorstest.presentation.pages.fragments.contacts_list_fragment

import android.annotation.SuppressLint
import android.content.ContentUris
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.ContactsContract
import android.provider.MediaStore
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.generalmotorstest.core.MyApplication
import com.example.generalmotorstest.data.models.Contacts
import java.util.*

class ContactsListViewModel : ViewModel() {

    private val contactsList = mutableStateListOf<Contacts>()
    val contactsFilteredList = mutableStateListOf<Contacts>()
    val searchSrt = mutableStateOf("")
    val isLoading = mutableStateOf(true)

    fun setContactsToContactsList() {
        isLoading.value = true

        contactsList.clear()
        contactsList.addAll(getAllContacts())

        isLoading.value = false
    }

    fun searchedItems(searchedText: String) {
        contactsFilteredList.clear()

        if (searchedText.isNotEmpty()) {
            val resultList = ArrayList<Contacts>()

            for (data in contactsList) {
                if (data.fullName?.lowercase(Locale.getDefault())
                        ?.contains(searchedText, ignoreCase = true) == true
                ) {
                    resultList.add(data)
                }
            }

            contactsFilteredList.addAll(resultList)
        } else {
            contactsFilteredList.addAll(contactsList)
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

    fun convertUriToBitmap(uri: Uri): Bitmap? {
        val bitmap = try {
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

}
