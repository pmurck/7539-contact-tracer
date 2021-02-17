package com.pmurck.contacttracer.pings


import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.pmurck.contacttracer.dateFormatter
import com.pmurck.contacttracer.model.Ping

@BindingAdapter("pingDateFormatted")
fun TextView.setDateFormatted(ping: Ping) {
    text = dateFormatter(ping.unixTimeStamp)
}
