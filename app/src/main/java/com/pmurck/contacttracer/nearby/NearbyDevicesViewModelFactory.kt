package com.pmurck.contacttracer.nearby

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pmurck.contacttracer.database.PingDAO

class NearbyDevicesViewModelFactory(
    private val pingDAO: PingDAO
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(NearbyDevicesViewModel::class.java)) {
            return NearbyDevicesViewModel(pingDAO) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}