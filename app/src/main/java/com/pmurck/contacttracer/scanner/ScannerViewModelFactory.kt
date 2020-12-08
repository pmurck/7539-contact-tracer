package com.pmurck.contacttracer.scanner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pmurck.contacttracer.database.StayDAO

class ScannerViewModelFactory(
    private val stayDAO: StayDAO
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ScannerViewModel::class.java)) {
            return ScannerViewModel(stayDAO) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}