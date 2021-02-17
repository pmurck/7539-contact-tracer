package com.pmurck.contacttracer.pings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pmurck.contacttracer.database.PingDAO

class PingListViewModelFactory(
    private val pingDAO: PingDAO
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(PingListViewModel::class.java)) {
            return PingListViewModel(pingDAO) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}