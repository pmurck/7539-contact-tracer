package com.pmurck.contacttracer.contacts

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.pmurck.contacttracer.Constants
import com.pmurck.contacttracer.database.ContactDAO
import java.util.*

class ContactListViewModel(
    contactDAO: ContactDAO,
    sharedPrefs: SharedPreferences
) : ViewModel() {

    val contacts = contactDAO.getAll()
    val lastContactGenerationDate = Date(sharedPrefs.getLong(Constants.CONTACT_GEN_TIMESTAMP_PREF_KEY, 0L))

}