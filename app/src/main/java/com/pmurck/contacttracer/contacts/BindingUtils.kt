package com.pmurck.contacttracer.contacts


import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.pmurck.contacttracer.R
import com.pmurck.contacttracer.model.Contact
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

@BindingAdapter("lastContactGenerationDate")
fun TextView.setLastContactGenerationDate(date: Date){
    text = context.resources.getString(R.string.last_update, SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(date).toString())
}

@BindingAdapter("contactStartDateFormatted")
fun TextView.setStartDateFormatted(contact: Contact) {
    text = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        .format(Date(contact.startTimestamp)).toString()
}

@BindingAdapter("contactDurationFormatted")
fun TextView.setContactDurationFormatted(contact: Contact) {
    val millisInAnHour = TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS)
    val timeDiffMillis = contact.endTimestamp - contact.startTimestamp
    val hours = TimeUnit.HOURS.convert(timeDiffMillis, TimeUnit.MILLISECONDS)
    val minutes = TimeUnit.MINUTES.convert(timeDiffMillis - (hours*millisInAnHour), TimeUnit.MILLISECONDS)
    text = when(hours){
        0L -> "${minutes} mins"
        else -> "${hours} hrs ${minutes.toString().padStart(2, '0')} mins"
    }

}