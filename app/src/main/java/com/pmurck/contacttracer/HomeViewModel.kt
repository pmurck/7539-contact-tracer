package com.pmurck.contacttracer

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.pmurck.contacttracer.database.StayDAO
import java.util.*

class HomeViewModel(
        stayDAO: StayDAO
) : ViewModel() {

    val stayDAO = stayDAO

    val currentStay = stayDAO.getCurrent()

    val qrStatusText = Transformations.map(currentStay){
        if (it == null) {
            "Iniciar estadía"
        } else {
            "Contabilizando estadía ... \n" +
                    "Código: ${it.qrCode}\n" +
                    "Inicio: ${Date(it.startTimestamp)}"
        }
    }

}