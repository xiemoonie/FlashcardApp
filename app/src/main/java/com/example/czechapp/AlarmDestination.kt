package com.example.czechapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class AlarmDestination : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm_destination)
        Log.d("notificationCALLTWO","notification channel broadcast!!")
    }
}