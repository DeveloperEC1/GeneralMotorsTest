package com.example.generalmotorstest.presentation.pages.fragments.contact_info_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
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
import com.example.generalmotorstest.presentation.widgets.SpacerWidget
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
                .verticalScroll(rememberScrollState())
                .fillMaxWidth(),
        ) {
            ProfileImageContactWidget(contactInfoViewModel.contactInfo!!)
            SpacerWidget(20)
            FirstNameContact()
            SpacerWidget(20)
            LastNameContact()
            SpacerWidget(20)
            PhonesNumberContact()
            SpacerWidget(20)
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
    private fun PhonesNumberContact() {
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
        SpacerWidget(5)
        Column {
            typeDataListContact.forEach { contact ->
                TextContactWidget("${contact.type}: ${contact.label}")
            }
        }
    }
    // UI methods - End

}
