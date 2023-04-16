package com.example.czechapp


import android.Manifest.permission.POST_NOTIFICATIONS
import android.app.AlarmManager
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("MAIN","hey I am running!")

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.fragmentTranslation,
                R.id.fragmentFlashcard
            )
        )
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setupWithNavController(navController)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_alarm_menu, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        //return navController.navigateUp() || super.onSupportNavigateUp()
        return findNavController(R.id.nav_host_fragment).navigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.alarmIcon) {
            checkNotificationPermissionRequest()
              return true
        } else {
            return item.onNavDestinationSelected(findNavController(R.id.nav_host_fragment))
            //return item.onNavDestinationSelected(findNavController(R.id.nav_host_fragment)) || super.onOptionsItemSelected(item)
        }

    }

    fun checkNotificationPermissionRequest() {
        if (ContextCompat.checkSelfPermission(
                this@MainActivity,
                android.Manifest.permission.POST_NOTIFICATIONS
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            // continue running app
            var fragment = Alarm()
            val fragmentManager: FragmentManager = supportFragmentManager
            fragmentManager.beginTransaction().add(R.id.nav_host_fragment, fragment, "alarmFragment")
                .commit()
        } else if (shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)) {
            showAlertDialog()
        } else {
            makePermissionRequest()
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this@MainActivity, "Notification Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@MainActivity, "Notification Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }


    fun makePermissionRequest() {
        val s = arrayOf(android.Manifest.permission.POST_NOTIFICATIONS)
        ActivityCompat.requestPermissions(this@MainActivity, s, 1)

    }

    fun showAlertDialog(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Alarm permission")
        builder.setMessage("\"This app needs you to allow this \n" +
                "     permission in order to function.Will you allow it\"")

        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            Toast.makeText(applicationContext,
                android.R.string.yes, Toast.LENGTH_SHORT).show()
            makePermissionRequest();
        }

        builder.setNegativeButton(android.R.string.no) { dialog, which ->
            Toast.makeText(applicationContext,
                android.R.string.no, Toast.LENGTH_SHORT).show()
        }

        builder.show()
    }

    fun deleteFragment(){
        var fragment = TranslationFragment()
        val fragmentManager: FragmentManager = supportFragmentManager
        var toRemove = fragmentManager.findFragmentByTag("alarmFragment")
        fragmentManager.beginTransaction().remove(toRemove!!)
            .commit()
    }


}
