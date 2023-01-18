package com.example.generalmotorstest.presentation.pages.fragments.contacts_list_fragment

import android.annotation.SuppressLint
import android.content.ContentUris
import android.net.Uri
import android.provider.ContactsContract
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.generalmotorstest.core.MyApplication
import com.example.generalmotorstest.data.models.ContactTypeData
import com.example.generalmotorstest.data.models.Contacts
import com.example.generalmotorstest.presentation.utils.UtilsApp.getTypeContact
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

    fun filterContactsList() {
        contactsFilteredList.clear()
        val searchedText = searchSrt.value

        if (searchedText.isNotEmpty()) {
            val resultList = ArrayList<Contacts>()

            for (data in contactsList) {
                if (data.firstName?.lowercase(Locale.getDefault())
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
                    var firstName = ""
                    var lastName = ""
                    val phoneNumber: ArrayList<ContactTypeData> = arrayListOf()
                    val email: ArrayList<ContactTypeData> = arrayListOf()
                    var profileImage: Uri? = null

                    // First Name + Last Name - Start
                    val whereName =
                        ContactsContract.Data.MIMETYPE + " = ? AND " + ContactsContract.CommonDataKinds.StructuredName.CONTACT_ID + " = ?"
                    val whereNameParams =
                        arrayOf(
                            ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE,
                            id,
                        )
                    val cn = MyApplication.application.contentResolver.query(
                        ContactsContract.Data.CONTENT_URI,
                        null,
                        whereName,
                        whereNameParams,
                        ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME
                    )

                    while (cn!!.moveToNext()) {
                        // First Name - Start
                        try {
                            firstName =
                                cn.getString(cn.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME))
                        } catch (_: Exception) {

                        }
                        // First Name - End

                        // Last Name - Start
                        try {
                            lastName =
                                cn.getString(cn.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME))
                        } catch (_: Exception) {

                        }
                        // Last Name - End
                    }

                    cn.close()
                    // First Name + Last Name - End

                    // Phone Number - Start
                    try {
                        val cp = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            arrayOf(id),
                            null,
                        )

                        while (cp!!.moveToNext()) {
                            phoneNumber.add(
                                ContactTypeData(
                                    getTypeContact(cp.getString(cp.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE))),
                                    cp.getString(cp.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)),
                                )
                            )
                        }

                        cp.close()
                    } catch (_: Exception) {

                    }
                    // Phone Number - End

                    // Email - Start
                    try {
                        val ce = cr.query(
                            ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                            arrayOf(id),
                            null,
                        )

                        while (ce!!.moveToNext()) {
                            email.add(
                                ContactTypeData(
                                    getTypeContact(ce.getString(ce.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE))),
                                    ce.getString(ce.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA)),
                                )
                            )
                        }

                        ce.close()
                    } catch (_: Exception) {

                    }
                    // Email - End

                    // Profile Image - Start
                    try {
                        val person = ContentUris.withAppendedId(
                            ContactsContract.Contacts.CONTENT_URI,
                            id.toLong()
                        )

                        profileImage = Uri.withAppendedPath(
                            person,
                            ContactsContract.Contacts.Photo.CONTENT_DIRECTORY
                        )
                    } catch (_: Exception) {

                    }
                    // Profile Image - End

                    val contact = Contacts(
                        firstName,
                        lastName,
                        phoneNumber,
                        email,
                        profileImage
                    )
                    contactsModelArr.add(contact)
                }
            } while (cursor.moveToNext())
            cursor.close()
        }

        return contactsModelArr
    }

}
