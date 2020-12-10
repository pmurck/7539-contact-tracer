package com.pmurck.contacttracer.scanner

import android.os.CountDownTimer
import androidx.lifecycle.*
import com.pmurck.contacttracer.database.StayDAO
import com.pmurck.contacttracer.model.Stay
import kotlinx.coroutines.launch

class ScannerViewModel(
        stayDAO: StayDAO
) : ViewModel() {

    val stayDAO = stayDAO

    private val currentStay = stayDAO.getCurrent()

    private var detectionState: DetectionState = NoCodes()
    private var stayPolicy: (qrCode: String) -> Unit = {}

    private val _qrScanStatus = MutableLiveData<String>()
    val qrScanStatus: LiveData<String>
        get() = _qrScanStatus

    private val _navigateToHome = MutableLiveData<Boolean>()
    val navigateToHome: LiveData<Boolean>
        get() = _navigateToHome

    init {
        // TODO: cambiar o de alguna forma removeObserver
        currentStay.observeForever{
            if (it == null) {
                stayPolicy = ::saveStay
            } else {
                stayPolicy = ::updateStay
            }
        }
    }

    fun onBarcodesDetected(codes: List<String>) {
        this.detectionState = this.detectionState.onBarcodesDetected(codes)

        if (this.detectionState.statusChanged) {
            _qrScanStatus.value = this.detectionState.status
        }

        if (this.detectionState.isBarcodeConfirmed){
            this.stayPolicy(this.detectionState.confirmedBarcode)
        }
    }

    private fun saveStay(qrCode: String) {
        viewModelScope.launch {
            stayDAO.insert(Stay(qrCode = qrCode, startTimestamp = System.currentTimeMillis()))

            _navigateToHome.value = true
        }
    }

    private fun updateStay(qrCode: String) {
        viewModelScope.launch {
            currentStay.value?.copy(endTimestamp = System.currentTimeMillis())?.let { stayDAO.update(it) }

            _navigateToHome.value = true
        }
    }

}