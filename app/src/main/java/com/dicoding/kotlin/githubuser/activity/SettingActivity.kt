package com.dicoding.kotlin.githubuser.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.kotlin.githubuser.R
import com.dicoding.kotlin.githubuser.fragment.SettingPreferencesFragment

class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        supportFragmentManager.beginTransaction().add(R.id.setting_holder, SettingPreferencesFragment()).commit()
    }
}