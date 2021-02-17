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

    abstract fun onBarcodesDetected(codes: List<String>, allowedCodes: Collection<String>): DetectionState
}

class NoCodes() : DetectionState(true) {

    override var status: String = "Apunte la cámara a un código QR"

    override fun onBarcodesDetected(codes: List<String>, allowedCodes: Collection<String>): DetectionState {
        return when (codes.size) {
            0 -> this
            1 -> if (allowedCodes.isNotEmpty() && allowedCodes.contains(codes.first()).not()) {
                    DisallowedCode()
                } else {
                    ScanningCode(codes.first())
                }
            else -> MoreThanOneCode()
        }
    }

}

class DisallowedCode() : DetectionState(true) {

    override var status: String = "Código QR incorrecto"

    override fun onBarcodesDetected(codes: List<String>, allowedCodes: Collection<String>): DetectionState {
        return when (codes.size) {
            0 -> NoCodes()
            1 -> if (allowedCodes.isNotEmpty() && allowedCodes.contains(codes.first()).not()) {
                    this
                } else {
                    ScanningCode(codes.first())
                }
            else -> MoreThanOneCode()
        }
    }

}

class ScanningCode(val code: String) : DetectionState(true) {

    override var status: String = "Leyendo..."

    private var codeConfirmed: Boolean = false

    private val scanningTimer: CountDownTimer

    init {
        scanningTimer = object: CountDownTimer(3000, 100) {
            override fun onTick(millisUntilFinished: Long) {
                status = "Leyendo...\nEspere %.1f segundos".format(millisUntilFinished / 1000.0)
                statusChanged = true
            }

            override fun onFinish() {
                codeConfirmed = true
            }

        }.start()
    }

    override fun onBarcodesDetected(codes: List<String>, allowedCodes: Collection<String>): DetectionState {
        if (codeConfirmed) return FinishedDetecting(code)

        // TODO: darle mini intervalo de gracia sin cambiar nada
        return if (codes.size == 1) {
            if (codes.first() == code) {
                this
            } else {
                scanningTimer.cancel()
                if (allowedCodes.isNotEmpty() && allowedCodes.contains(codes.first()).not()) {
                    DisallowedCode()
                } else {
                    ScanningCode(codes.first())
                }
            }
        } else {
            scanningTimer.cancel()
            when (codes.size) {
                0 -> NoCodes()
                else -> MoreThanOneCode()
            }
        }
    }

}

class MoreThanOneCode() : DetectionState(true) {

    override var status: String = "Observando mas de un código, acerquese a uno"

    override fun onBarcodesDetected(codes: List<String>, allowedCodes: Collection<String>): DetectionState {
        return when (codes.size) {
            0 -> NoCodes()
            1 -> if (allowedCodes.isNotEmpty() && allowedCodes.contains(codes.first()).not()) {
                    DisallowedCode()
                } else {
                    ScanningCode(codes.first())
                }
            else -> this
        }
    }

}

class FinishedDetecting(val confirmedCode: String) : DetectionState(true) {

    override var status: String = "Escaneo finalizado"

    override val isBarcodeConfirmed: Boolean = true

    override var confirmedBarcode = confirmedCode

    override fun onBarcodesDetected(codes: List<String>, allowedCodes: Collection<String>): DetectionState {
        return this
    }
}