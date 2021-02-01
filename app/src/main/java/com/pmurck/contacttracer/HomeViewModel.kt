package com.pmurck.contacttracer

import androidx.core.view.isVisible
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.mohsenoid.closetome.CloseToMeState
import com.pmurck.contacttracer.database.StayDAO
import java.util.*

class HomeViewModel(
        stayDAO: StayDAO
) : ViewModel() {

    val stayDAO = stayDAO

    val currentStay = stayDAO.getCurrent()

    val beacon_status = Transformations.map(BeaconService.state){
        when (it) {
            CloseToMeState.STARTED -> "En ejecución"
            else -> "Detenido"
        }
    }

    val qrStatusText = Transformations.map(currentStay){
        if (it == null) {
            "No hay estadía en curso"
        } else {
            "Contabilizando estadía ... \n" +
                    "Código: ${it.qrCode}\n" +
                    "Inicio: ${Date(it.startTimestamp)}"
        }
    }

    val go_to_scanner_help = Transformations.map(currentStay){
        if (it == null) {
            "Para iniciar una estadía"
        } else {
            "Para finalizar la estadía"
        }
    }

}