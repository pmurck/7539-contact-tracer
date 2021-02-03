package com.pmurck.contacttracer.network

import android.service.autofill.UserData
import com.squareup.moshi.Json
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

data class User(
    val username: String,
    val password: String
)

data class Token(
    @Json(name = "token") val code: String
)

data class BluetoothContact(
    @Json(name = "id_persona_1") val myDeviceId: String,
    @Json(name = "id_persona_2") val otherDeviceId: String,
    @Json(name = "tiempo_contacto_segundos") val timeInSeconds: Long,
    @Json(name = "distancia_mts") val distanceInMeters: Double,
    @Json(name = "fecha_contacto") val date: String,
    @Json(name = "fuente") val source: String = "BLUETOOTH",
)

data class QrCodeGeneratedStay(
    @Json(name = "id_persona_1") val myDeviceId: String,
    //@Json(name = "id_persona_2") val otherDeviceId: String,
    @Json(name = "tiempo_contacto_segundos") val timeInSeconds: Long,
    //@Json(name = "distancia_mts") val distanceInMeters: Double,
    @Json(name = "fecha_contacto") val date: String,
    @Json(name = "codigo_qr") val qrCode: String,
    @Json(name = "fuente") val source: String = "QR",
)

data class ContactCreated(
    @Json(name = "status") val httpStatusCode: Int,
    val message: String,
    @Json(name = "id") val contactId: Long
)

interface BackendApiService {

    @POST("api/auth/")
    suspend fun login(@Body user: User): Response<Token>

    @POST("api/contact/")
    suspend fun createBluetoothContact(
        @Header("authorization") authToken: String, @Body btContact: BluetoothContact): Response<ContactCreated>

    @POST("api/contact/")
    suspend fun createStayWithQrCode(
        @Header("authorization") authToken: String, @Body qrStay: QrCodeGeneratedStay): Response<ContactCreated>
}