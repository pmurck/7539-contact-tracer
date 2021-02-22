package com.pmurck.contacttracer.stays

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pmurck.contacttracer.database.StayDAO

class StayListViewModelFactory(
    private val stayDAO: StayDAO
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(StayListViewModel::class.java)) {
            return StayListViewModel(stayDAO) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}