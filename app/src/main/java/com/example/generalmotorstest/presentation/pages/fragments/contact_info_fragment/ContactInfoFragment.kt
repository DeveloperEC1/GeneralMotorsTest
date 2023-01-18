package com.example.generalmotorstest.presentation.pages.fragments.contact_info_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.example.generalmotorstest.ContactInfoGraphArgs
import com.example.generalmotorstest.presentation.pages.fragments.BaseFragment
import com.example.generalmotorstest.presentation.widgets.ProfileImageContact
import com.example.generalmotorstest.presentation.widgets.TextContact

class ContactInfoFragment : BaseFragment() {

    private lateinit var contactInfoViewModel: ContactInfoViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initViewModel()
        setContactDataToParam()

        return ComposeView(requireContext()).apply {
            setContent {
                Body()
            }
        }
    }

    // Logic methods - Start
    private fun initViewModel() {
        contactInfoViewModel =
            ViewModelProvider(requireActivity())[ContactInfoViewModel::class.java]
    }

    private fun setContactDataToParam() {
        contactInfoViewModel.contactInfo =
            ContactInfoGraphArgs.fromBundle(requireArguments()).contactsData
    }
    // Logic methods - End

    // UI methods - Start
    @Composable
    private fun Body() {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
        ) {
            ProfileImageContact(contactInfoViewModel.contactInfo!!)
            FirstNameContact()
            LastNameContact()
            PhoneNumberContact()
            EmailContact()
        }
    }

    @Composable
    private fun FirstNameContact() {
        TextContact("First Name: ${contactInfoViewModel.contactInfo?.firstName!!}")
    }

    @Composable
    private fun LastNameContact() {
        TextContact("Last Name: ${contactInfoViewModel.contactInfo?.lastName!!}")
    }

    @Composable
    private fun PhoneNumberContact() {
        TextContact("Phone Number: ${contactInfoViewModel.contactInfo?.phoneNumber!!}")
    }

    @Composable
    private fun EmailContact() {
        TextContact("Email: ${contactInfoViewModel.contactInfo?.email!!}")
    }
    // UI methods - End

}
