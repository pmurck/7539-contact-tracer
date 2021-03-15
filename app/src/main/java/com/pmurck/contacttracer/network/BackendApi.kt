package com.pmurck.contacttracer.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class BackendApi {

    companion object {

        fun getRetrofitService(baseUrl: String): BackendApiService {

            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()

            val retrofit = Retrofit.Builder()
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .baseUrl(baseUrl)
                .build()

            return retrofit.create(BackendApiService::class.java)
        }
    }
}