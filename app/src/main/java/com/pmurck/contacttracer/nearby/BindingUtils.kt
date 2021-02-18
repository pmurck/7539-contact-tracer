package com.pmurck.contacttracer.nearby


import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.pmurck.contacttracer.database.PingDAO
import com.pmurck.contacttracer.dateFormatter
import com.pmurck.contacttracer.model.Ping
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("pingTimeAndDistance")
fun TextView.setTimeAndDateFormatted(ping: PingDAO.PingWithMinDistance) {
    text = "%s hs a %.2f m".format(SimpleDateFormat("HH:mm:ss").format(Date(ping.unixTimeStamp)), ping.distance)
}
