package com.dicoding.kotlin.githubuser.fragment

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.dicoding.kotlin.githubuser.R

class SettingPreferencesFragment: PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var REMINDER: String

    private lateinit var isReminder: SwitchPreference

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.setting_preferences)
        init()
        setSummaries()
    }

    private fun init() {
        REMINDER = "reminder"

        isReminder = findPreference<SwitchPreference>(REMINDER) as SwitchPreference
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        if (key == REMINDER) {
            isReminder.isChecked = sharedPreferences.getBoolean(REMINDER, false)
        }
    }

    private fun setSummaries() {
        val sh = preferenceManager.sharedPreferences
        isReminder.isChecked = sh.getBoolean(REMINDER, false)
    }
}