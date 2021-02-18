package com.pmurck.contacttracer.nearby

import androidx.lifecycle.ViewModel
import com.pmurck.contacttracer.database.PingDAO

class NearbyDevicesViewModel(
    pingDAO: PingDAO
) : ViewModel() {

    val minutes = 5
    val pings = pingDAO.getLastFromDistinctDNISinceMinutesBeforeNow(minutes)

}