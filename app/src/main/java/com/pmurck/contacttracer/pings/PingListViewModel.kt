package com.pmurck.contacttracer.pings

import androidx.lifecycle.ViewModel
import com.pmurck.contacttracer.database.PingDAO

class PingListViewModel(
        pingDAO: PingDAO
) : ViewModel() {

    val pings = pingDAO.getAll()

}