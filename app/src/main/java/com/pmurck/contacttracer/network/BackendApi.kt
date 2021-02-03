package com.pmurck.contacttracer.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object BackendApi {

    const val BASE_URL = "http://192.168.1.150:8000/" //TODO: tomar de otro lugar

    val retrofitService : BackendApiService by lazy {

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val retrofit = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(BASE_URL)
            .build()

        retrofit.create(BackendApiService::class.java)
    }
}