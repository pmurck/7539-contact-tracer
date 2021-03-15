package com.pmurck.contacttracer

object Constants {
    const val SHARED_PREFS_CONFIG_NAME = "CONFIG"
    const val DEVICE_ID_PREF_KEY = "device_id"
    const val CONTACT_GEN_TIMESTAMP_PREF_KEY = "last_contacts_update_timestamp"
    const val CONTACT_GEN_REPEAT_INTERVAL_HOURS_PREF_KEY ="contact_gen_repeat_interval"
    const val CONTACT_GEN_MIN_TIME_IN_MINUTES_PREF_KEY = "contact_gen_min_contact_time_in_minutes"
    const val CONTACT_GEN_MAX_TIME_DIFF_BETWEEN_PINGS_IN_SECONDS_PREF_KEY = "contact_gen_max_time_diff_between_pings_in_seconds"
    const val BACKEND_SYNC_TIMESTAMP_PREF_KEY = "backend_sync_last_update_timestamp"
    const val BACKEND_SYNC_REPEAT_INTERVAL_HOURS_PREF_KEY = "uploader_repeat_interval"
}