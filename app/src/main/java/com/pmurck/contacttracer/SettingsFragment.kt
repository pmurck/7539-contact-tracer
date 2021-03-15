package com.pmurck.contacttracer

import android.content.SharedPreferences
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.preference.*
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.pmurck.contacttracer.network.BackendApi
import com.pmurck.contacttracer.network.User
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class SettingsFragment : PreferenceFragmentCompat() {

    val listener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
        when (key) {
            Constants.CONTACT_GEN_TIMESTAMP_PREF_KEY -> setLastContactGen(sharedPreferences.getLong(key,0L))
            Constants.CONTACT_GEN_REPEAT_INTERVAL_HOURS_PREF_KEY -> setupContactGenWorkerReplaceExisting(requireActivity().applicationContext, sharedPreferences.getInt(key, 8))
            Constants.BACKEND_SYNC_TIMESTAMP_PREF_KEY -> setLastUpload(sharedPreferences.getLong(key, 0L))
            Constants.BACKEND_SYNC_REPEAT_INTERVAL_HOURS_PREF_KEY -> setupBackendSyncWorkerReplaceExisting(requireActivity().applicationContext, sharedPreferences.getInt(key, 8))
        }
    }

    override fun onResume() {
        super.onResume()
        preferenceManager.sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
    }

    override fun onPause() {
        super.onPause()
        preferenceManager.sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        val checkConnectionAndAuth = findPreference<Preference>("check_connection_and_auth")!!

        checkConnectionAndAuth.setOnPreferenceClickListener{
            checkConnectionAndAuth.summary = "Comprobando..."

            val baseUrlPref = findPreference<EditTextPreference>("base_url")!!
            val usernamePref = findPreference<EditTextPreference>("username")!!
            val passwordPref = findPreference<EditTextPreference>("password")!!

            val apiService = BackendApi.getRetrofitService(baseUrlPref.text)


            lifecycleScope.launch {
                try {
                    val loginResponse = apiService.login(
                        User(
                            usernamePref.text,
                            passwordPref.text
                        )
                    )
                    if (loginResponse.isSuccessful) {
                        checkConnectionAndAuth.summary = "Conexión y autenticación exitosa"
                    } else {
                        checkConnectionAndAuth.summary = "Falló la autenticación"
                    }
                    // on timeout, que falle
                } catch (e: Exception){
                    checkConnectionAndAuth.summary = "Falló la conexión: ${e.message}"
                }
            }
            true
        }

        val contactGenRepeatIntervalPref = findPreference<SeekBarPreference>("contact_gen_repeat_interval")!!
        val contactGenRunNow = findPreference<Preference>("contact_gen_run_now")!!
        contactGenRunNow.setOnPreferenceClickListener {
            setupContactGenWorkerReplaceExisting(requireActivity().applicationContext, contactGenRepeatIntervalPref.value)
            true
        }

        val uploaderRepeatIntervalPref = findPreference<SeekBarPreference>("uploader_repeat_interval")!!
        val uploaderRunNow = findPreference<Preference>("uploader_run_now")!!
        uploaderRunNow.setOnPreferenceClickListener {
            setupBackendSyncWorkerReplaceExisting(requireActivity().applicationContext, uploaderRepeatIntervalPref.value)
            true
        }

        setLastContactGen(preferenceManager.sharedPreferences.getLong(Constants.CONTACT_GEN_TIMESTAMP_PREF_KEY, 0L))
        setLastUpload(preferenceManager.sharedPreferences.getLong(Constants.BACKEND_SYNC_TIMESTAMP_PREF_KEY, 0L))
    }

    fun setLastContactGen(timestamp: Long) {
        val contactGenLastUpdatePref = findPreference<Preference>("contact_gen_last_update_timestamp")!!
        contactGenLastUpdatePref.summary = when (timestamp) {
            0L -> "Nunca"
            else -> dateFormatter(timestamp)
        }
    }

    fun setLastUpload(timestamp: Long) {
        val lastUploadPref = findPreference<Preference>("uploader_last_update_timestamp")!!
        lastUploadPref.summary = when (timestamp) {
            0L -> "Nunca"
            else -> dateFormatter(timestamp)
        }
    }
}