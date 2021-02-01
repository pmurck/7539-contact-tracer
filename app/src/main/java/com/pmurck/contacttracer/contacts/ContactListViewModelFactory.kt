package com.pmurck.contacttracer.contacts

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pmurck.contacttracer.database.ContactDAO

class ContactListViewModelFactory(
    private val contactDAO: ContactDAO,
    private val sharedPreferences: SharedPreferences
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ContactListViewModel::class.java)) {
            return ContactListViewModel(contactDAO, sharedPreferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}