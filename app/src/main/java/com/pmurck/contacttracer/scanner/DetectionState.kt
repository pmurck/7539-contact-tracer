package com.pmurck.contacttracer.scanner

import android.os.CountDownTimer

abstract class DetectionState(statusChanged: Boolean) {

    var statusChanged: Boolean = statusChanged
        get() {
            val toReturn = field
            field = false
            return toReturn
        }
        protected set

    abstract var status: String
        protected set

    open val isBarcodeConfirmed: Boolean = false

    open lateinit var confirmedBarcode: String
        protected set

    abstract fun onBarcodesDetected(codes: List<String>): DetectionState
}

class NoCodes() : DetectionState(true) {

    override var status: String = "Apunte la cámara a un código QR"

    override fun onBarcodesDetected(codes: List<String>): DetectionState {
        return when (codes.size) {
            0 -> this
            1 -> ScanningCode(codes.first())
            else -> MoreThanOneCode()
        }
    }

}

class ScanningCode(val code: String) : DetectionState(true) {

    override var status: String = "Leyendo..."

    private var codeConfirmed: Boolean = false

    private val scanningTimer: CountDownTimer

    init {
        scanningTimer = object: CountDownTimer(3000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                status = "Leyendo...\nEspere ${millisUntilFinished / 1000} segundo(s)"
                statusChanged = true
            }

            override fun onFinish() {
                codeConfirmed = true
            }

        }.start()
    }

    override fun onBarcodesDetected(codes: List<String>): DetectionState {
        if (codeConfirmed) return FinishedDetecting(code)
        if (codes.size == 1) {
            if (codes.first() == code) {
                return this
            } else {
                scanningTimer.cancel()
                return ScanningCode(codes.first())
            }
        } else {
            scanningTimer.cancel()
            return when (codes.size) {
                0 -> NoCodes()
                else -> MoreThanOneCode()
            }
        }
    }

}

class MoreThanOneCode() : DetectionState(true) {

    override var status: String = "Observando mas de un código, acerquese a uno"

    override fun onBarcodesDetected(codes: List<String>): DetectionState {
        return when (codes.size) {
            0 -> NoCodes()
            1 -> ScanningCode(codes.first())
            else -> this
        }
    }

}

class FinishedDetecting(val confirmedCode: String) : DetectionState(true) {

    override var status: String = "Iniciando estadía..."

    override val isBarcodeConfirmed: Boolean = true

    override var confirmedBarcode = confirmedCode

    override fun onBarcodesDetected(codes: List<String>): DetectionState {
        return this
    }
}