package com.example.czechapp

import android.Manifest.permission.POST_NOTIFICATIONS
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.czechapp.databinding.FragmentAlarmBinding
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.*


class Alarm : Fragment() {

    lateinit var _binding: FragmentAlarmBinding
    lateinit var calendar : Calendar
    var newHour = 0
    var newMinute = 0

    lateinit var alarmManager : AlarmManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAlarmBinding.inflate(inflater, container, false)
        createNotificationChannel()
        listeners()
        showTimePicker()


        return _binding.root
    }

    private fun listeners() {

        _binding.saveTimerButton.setOnClickListener(){
            setAlarm()
        }

        _binding.backTimerButton.setOnClickListener() {
            val d = activity as MainActivity
            d.deleteFragment()
        }
    }

    private fun showTimePicker() {
        var hora = ""
        val materialTimePicker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .build()
        materialTimePicker.show(parentFragmentManager, "channel")

        materialTimePicker.addOnPositiveButtonClickListener {
            if(materialTimePicker.hour > 12){
               hora =  String.format("%02d", materialTimePicker.hour - 12) + ":" + String.format(
                    "%02d",
                   materialTimePicker.minute) + "PM"
            }else if(materialTimePicker.hour == 12){
                hora =  String.format("%02d", materialTimePicker.hour ) + ":" + String.format(
                    "%02d",
                    materialTimePicker.minute) + "PM"
            }else{
                hora = String.format("%02d", materialTimePicker.hour) + ":" + String.format(
                    "%02d",
                    materialTimePicker.minute) + "AM"
            }

            _binding.clockText.text = hora
             newHour = materialTimePicker.hour
             newMinute = materialTimePicker.minute
        }

    }

    private fun setAlarm(){
         alarmManager = getActivity()?.getSystemService(ALARM_SERVICE) as AlarmManager
         val intent = Intent(context, AlarmReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, newHour)
        calendar.set(Calendar.MINUTE, newMinute)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)



        //alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent);

        /*alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP, calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY, pendingIntent
        )*/

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)




        Log.d("notificationAlarm","notification channel crates" +calendar.get(Calendar.HOUR_OF_DAY).toString())
        Toast.makeText(context, "Alarm set successfully", Toast.LENGTH_SHORT).show()

    }

        fun createNotificationChannel() {
            Log.d("notificationSDK","notification SDK " +Build.VERSION.SDK_INT.toString())
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            val name : CharSequence = "channel"
            val description = "Channel For Alarm Manager"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("channelId",name,importance)
            channel.description = description
            val notificationManager = context?.getSystemService(NotificationManager::class.java)

            Log.d("notificationCREATE","notification channel crates" +notificationManager.toString())
            notificationManager?.createNotificationChannel(channel)
        }
    }




}