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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.example.generalmotorstest.ContactInfoGraphArgs
import com.example.generalmotorstest.R
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
            PhoneNumberListContact()
            SpacerHeightWidget(20)
            EmailListContact()
        }
    }

    @Composable
    private fun FirstNameContact() {
        TextContactWidget("${stringResource(R.string.first_name)}: ${contactInfoViewModel.contactInfo?.firstName!!}")
    }

    @Composable
    private fun LastNameContact() {
        TextContactWidget("${stringResource(R.string.last_name)}: ${contactInfoViewModel.contactInfo?.lastName!!}")
    }

    @Composable
    private fun PhoneNumberListContact() {
        TypeDataListContact(
            "${stringResource(R.string.phone_numbers)}:",
            contactInfoViewModel.contactInfo?.phoneNumberList!!
        )
    }

    @Composable
    private fun EmailListContact() {
        TypeDataListContact(
            "${stringResource(R.string.emails)}:",
            contactInfoViewModel.contactInfo?.emailList!!
        )
    }

    @Composable
    private fun TypeDataListContact(title: String, typeDataListContact: List<ContactTypeData>) {
        TextContactWidget(title)
        SpacerHeightWidget(5)
        Column {
            typeDataListContact.forEach { contact ->
                TypeDataContactItem(contact)
            }
        }
    }

    @Composable
    private fun TypeDataContactItem(contactTypeData: ContactTypeData) {
        Row {
            SpacerWidthWidget(20)
            TextContactWidget("${contactTypeData.type}: ${contactTypeData.label}")
        }
    }
    // UI functions - End

}
