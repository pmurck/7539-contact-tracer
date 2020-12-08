package com.pmurck.contacttracer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pmurck.contacttracer.database.StayDAO

class HomeViewModelFactory(
        private val stayDAO: StayDAO) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(stayDAO) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}