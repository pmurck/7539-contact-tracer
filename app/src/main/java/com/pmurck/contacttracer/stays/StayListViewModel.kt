package com.pmurck.contacttracer.stays

import androidx.lifecycle.ViewModel
import com.pmurck.contacttracer.database.StayDAO

class StayListViewModel(
    stayDAO: StayDAO
) : ViewModel() {

    val stays = stayDAO.getAllMinusCurrent()

}