package com.example.generalmotorstest.presentation.pages.fragments.contacts_list_fragment

import android.Manifest
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
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

                Body()
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
                    contactsListViewModel.setContactsToContactsList(contactsListViewModel.getAllContacts())
                }
            }
        }

    private fun readContactsPermissionRequest() {
        requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
    }

    @Composable
    private fun Body() {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
        ) {
            SearchViewTextField()

            contactsListViewModel.searchedItems(contactsListViewModel.searchSrt.value)

            if (contactsListViewModel.isLoading.value) {
                EmptyView()
            } else {
                ContactsList()
            }
        }
    }

    @Composable
    private fun SearchViewTextField() {
        Box(
            modifier = Modifier
                .border(width = 1.dp, color = Color.Gray)
                .fillMaxWidth(),
        ) {
            BasicTextField(
                value = contactsListViewModel.searchSrt.value,
                onValueChange = {
                    contactsListViewModel.searchSrt.value = it
                },
                modifier = Modifier
                    .height(38.dp)
                    .fillMaxWidth(),
                singleLine = true,
                maxLines = 1,
            )
        }
    }

    @Composable
    private fun ContactsList() {
        val contactsList = contactsListViewModel.contactsList

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 10.dp),
        ) {
            val uri = contact.image

            if (uri != null) {
                val bitmap = try {
                    MediaStore.Images.Media.getBitmap(requireContext().contentResolver, uri)
                } catch (error: Throwable) {
                    null
                }

                if (bitmap != null) {
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                    )
                } else {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .background(Color.Cyan, shape = CircleShape)
                            .size(50.dp),
                    ) {
                        Text(
                            contact.fullName!![0].toString(),
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }

            Text(
                "Full Name: ${contact.fullName!!}",
                fontSize = 20.sp
            )
        }
    }

    @Composable
    fun EmptyView() {

    }

}
