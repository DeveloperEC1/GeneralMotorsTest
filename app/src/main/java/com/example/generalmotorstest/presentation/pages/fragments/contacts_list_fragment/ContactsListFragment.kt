package com.example.generalmotorstest.presentation.pages.fragments.contacts_list_fragment

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import com.example.generalmotorstest.ContactsListGraphDirections
import com.example.generalmotorstest.R
import com.example.generalmotorstest.data.models.Contacts
import com.example.generalmotorstest.presentation.pages.fragments.BaseFragment
import com.example.generalmotorstest.presentation.widgets.ProfileImageContactWidget
import com.example.generalmotorstest.presentation.widgets.ProgressDialogWidget
import com.example.generalmotorstest.presentation.widgets.SpacerHeightWidget
import com.example.generalmotorstest.presentation.widgets.TextContactWidget
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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
                Body()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        readContactsPermissionRequest()
    }

    // Logic functions - Start
    private fun initViewModel() {
        contactsListViewModel =
            ViewModelProvider(requireActivity())[ContactsListViewModel::class.java]
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                CoroutineScope(Dispatchers.IO).launch {
                    contactsListViewModel.setContactsToContactsList()
                    contactsListViewModel.permissionIsGranted.value = true
                }
            } else {
                contactsListViewModel.isLoading.value = false
            }
        }

    private fun readContactsPermissionRequest() {
        requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
    }
    // Logic functions - End

    // UI functions - Start
    @Composable
    private fun Body() {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
        ) {
            if (contactsListViewModel.isLoading.value) {
                ProgressDialogWidget()
            } else {
                if (contactsListViewModel.permissionIsGranted.value) {
                    contactsListViewModel.filterContactsList()

                    SearchViewTextField()
                    SpacerHeightWidget(20)
                    ContactsList()
                } else {
                    Text(
                        stringResource(R.string.read_contacts_permission_not_granted),
                        fontSize = 30.sp,
                        color = Color.Red,
                    )
                }
            }
        }
    }

    @Composable
    private fun SearchViewTextField() {
        Box(
            modifier = Modifier
                .height(40.dp)
                .border(width = 1.dp, color = Color.Gray)
                .fillMaxSize(),
            contentAlignment = Alignment.CenterStart
        ) {
            BasicTextField(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .fillMaxWidth(),
                value = contactsListViewModel.searchSrt.value,
                onValueChange = {
                    contactsListViewModel.searchSrt.value = it
                },
                singleLine = true,
                maxLines = 1,
            )
        }
    }

    @Composable
    private fun ContactsList() {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(
                contactsListViewModel.contactsFilteredList
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
                .padding(bottom = 10.dp)
                .clickable {
                    findNavController(this)
                        .navigate(
                            ContactsListGraphDirections.actionNavigateToContactInfoFragment(
                                contact
                            )
                        )
                },
        ) {
            ProfileImageContactWidget(contact)
            SpacerHeightWidget(5)
            PersonNameContact(contact)
        }
    }

    @Composable
    private fun PersonNameContact(contact: Contacts) {
        TextContactWidget("${stringResource(R.string.person_name)}: ${contact.personName!!}")
    }
    // UI functions - End

}
