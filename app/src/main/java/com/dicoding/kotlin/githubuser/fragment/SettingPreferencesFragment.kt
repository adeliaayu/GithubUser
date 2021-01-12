package com.dicoding.kotlin.githubuser.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.dicoding.kotlin.githubuser.R
import com.dicoding.kotlin.githubuser.utils.ReminderReciever

class SettingPreferencesFragment: PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var REMINDER: String

    private lateinit var isReminder: SwitchPreference

    private lateinit var reminderReceiver: ReminderReciever

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        reminderReceiver = ReminderReciever()
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

            if (isReminder.isChecked){
                Toast.makeText(context, "Reminder set up", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Reminder dibatalkan", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setSummaries() {
        val sh = preferenceManager.sharedPreferences
        isReminder.isChecked = sh.getBoolean(REMINDER, false)

        if(isReminder.isChecked){
            context?.let {
                reminderReceiver.setReminderAlarm(it)
            }
        } else {
            context?.let { reminderReceiver.cancelReminder(it) }
        }
    }
}