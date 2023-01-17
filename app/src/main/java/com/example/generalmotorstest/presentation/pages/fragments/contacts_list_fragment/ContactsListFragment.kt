package com.example.generalmotorstest.presentation.pages.fragments.contacts_list_fragment

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.ui.core.Text
import com.example.generalmotorstest.data.models.Contacts
import com.example.generalmotorstest.presentation.pages.fragments.BaseFragment
import kotlinx.coroutines.launch

class ContactsListFragment : BaseFragment() {

    private lateinit var contactsListViewModel: ContactsListViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initViewModel()

        return ComposeView(requireContext()).apply {
            setContent {
                LaunchedEffect(Unit, block = {
                    readContactsPermissionRequest()
                })

                ContactsList()
            }
        }
    }

    private fun initViewModel() {
        contactsListViewModel =
            ViewModelProvider(requireActivity())[ContactsListViewModel::class.java]
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                lifecycleScope.launch {
                    contactsListViewModel.setContactsToContactsList()
                }
            }
        }

    private fun readContactsPermissionRequest() {
        requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
    }

    @Composable
    private fun ContactsList() {
        val contactsList = contactsListViewModel.contactsList

        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            items(
                contactsList
            ) {
                ContactItem(contact = it)
            }
        }
    }

    @Composable
    private fun ContactItem(contact: Contacts) {
        Text(contact.firstName!!)
    }

}