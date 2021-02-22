package com.pmurck.contacttracer.stays


import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.pmurck.contacttracer.dateFormatter
import com.pmurck.contacttracer.model.Stay
import java.util.concurrent.TimeUnit

@BindingAdapter("stayStartDateFormatted")
fun TextView.setStartDateFormatted(stay: Stay) {
    text = dateFormatter(stay.startTimestamp)
}

@BindingAdapter("stayDurationFormatted")
fun TextView.setContactDurationFormatted(stay: Stay) {
    val millisInAnHour = TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS)
    val millisInAMinute = TimeUnit.MILLISECONDS.convert(1, TimeUnit.MINUTES)
    val timeDiffMillis = stay.endTimestamp!! - stay.startTimestamp //si se llama bien, ese dato no es null
    val hours = TimeUnit.HOURS.convert(timeDiffMillis, TimeUnit.MILLISECONDS)
    val minutes = TimeUnit.MINUTES.convert(timeDiffMillis - (hours*millisInAnHour), TimeUnit.MILLISECONDS)
    val seconds = TimeUnit.SECONDS.convert(timeDiffMillis - (hours*millisInAnHour) - (minutes*millisInAMinute), TimeUnit.MILLISECONDS)
    text = when(hours){
        0L -> when(minutes) {
            0L -> "${seconds} segs"
            else -> "${minutes} mins ${seconds.toString().padStart(2, '0')} segs"
        }
        else -> "${hours} hrs ${minutes.toString().padStart(2, '0')} mins"
    }

}