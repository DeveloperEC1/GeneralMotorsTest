package com.example.generalmotorstest.presentation.pages.fragments.contact_info_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.example.generalmotorstest.ContactInfoGraphArgs
import com.example.generalmotorstest.data.models.ContactTypeData
import com.example.generalmotorstest.presentation.pages.fragments.BaseFragment
import com.example.generalmotorstest.presentation.widgets.ProfileImageContactWidget
import com.example.generalmotorstest.presentation.widgets.SpacerHeightWidget
import com.example.generalmotorstest.presentation.widgets.SpacerWidthWidget
import com.example.generalmotorstest.presentation.widgets.TextContactWidget

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

    // Logic functions - Start
    private fun initViewModel() {
        contactInfoViewModel =
            ViewModelProvider(requireActivity())[ContactInfoViewModel::class.java]
    }

    private fun setContactDataToParam() {
        contactInfoViewModel.contactInfo =
            ContactInfoGraphArgs.fromBundle(requireArguments()).contactsData
    }
    // Logic functions - End

    // UI functions - Start
    @Composable
    private fun Body() {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth(),
        ) {
            ProfileImageContactWidget(contactInfoViewModel.contactInfo!!)
            SpacerHeightWidget(20)
            FirstNameContact()
            SpacerHeightWidget(20)
            LastNameContact()
            SpacerHeightWidget(20)
            PhoneNumbersContact()
            SpacerHeightWidget(20)
            EmailsContact()
        }
    }

    @Composable
    private fun FirstNameContact() {
        TextContactWidget("First Name: ${contactInfoViewModel.contactInfo?.firstName!!}")
    }

    @Composable
    private fun LastNameContact() {
        TextContactWidget("Last Name: ${contactInfoViewModel.contactInfo?.lastName!!}")
    }

    @Composable
    private fun PhoneNumbersContact() {
        val phoneNumberList = contactInfoViewModel.contactInfo?.phoneNumberList!!

        TypeDataListContact("Phone Numbers:", phoneNumberList)
    }

    @Composable
    private fun EmailsContact() {
        val emailList = contactInfoViewModel.contactInfo?.emailList!!

        TypeDataListContact("Emails:", emailList)
    }

    @Composable
    private fun TypeDataListContact(title: String, typeDataListContact: List<ContactTypeData>) {
        TextContactWidget(title)
        SpacerHeightWidget(5)
        Column {
            typeDataListContact.forEach { contact ->
                Row {
                    SpacerWidthWidget(20)
                    TextContactWidget("${contact.type}: ${contact.label}")
                }
            }
        }
    }
    // UI functions - End

}
