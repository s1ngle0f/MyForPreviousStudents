package com.example.myforpreviousstudents

import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiManager
import android.os.Bundle
import android.view.Window
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.myforpreviousstudents.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit

const val ACTION_USER_LOGGED_IN = "com.example.myforpreviousstudents.USER_LOGGED_IN"

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var wifiReceiver: WifiStateReceiver
    private lateinit var bluetoothReceiver: BluetoothStateReceiver
    private lateinit var userLoginReceiver: UserLoginReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, true)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.frame_content, HomeFragment())
                .commit()
        }

        setSupportActionBar(binding.toolbar)

        val toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    toast("Home")
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_content, HomeFragment())
                        .commit()
                }
                R.id.rv_page -> {
                    toast("Recycler View")
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_content, RVFragment())
                        .commit()
                }
                R.id.second_activity -> {
                    toast("Second Activity")
                    val intent = Intent(this, SecondActivity::class.java)
                    startActivity(intent)
                }
            }
            binding.drawerLayout.closeDrawers()
            true
        }

        wifiReceiver = WifiStateReceiver()
        val filterWiFi = IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION)
        registerReceiver(wifiReceiver, filterWiFi)

        bluetoothReceiver = BluetoothStateReceiver()
        val filterBluetooth = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
        registerReceiver(bluetoothReceiver, filterBluetooth)

        val userLoginReceiver = UserLoginReceiver()
        val filter = IntentFilter(ACTION_USER_LOGGED_IN)
        registerReceiver(userLoginReceiver, filter, RECEIVER_NOT_EXPORTED)
        val intent = Intent(ACTION_USER_LOGGED_IN).apply {
            setPackage(packageName)
            putExtra("user_id", 123)
        }
        sendBroadcast(intent)

        val inputData = workDataOf("user_id" to 123)
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED) // только Wi-Fi
            .build()
        val workRequest = OneTimeWorkRequestBuilder<SyncWorker>()
            .setInputData(inputData)
//            .setConstraints(constraints)
            .build()
        val workRequest2 = OneTimeWorkRequestBuilder<SyncWorker>()
            .build()
//        val workRequest = PeriodicWorkRequestBuilder<SyncWorker>(
//            15, TimeUnit.MINUTES
//        ).build()
        WorkManager.getInstance(this)
            .enqueue(workRequest)
        WorkManager.getInstance(this)
            .getWorkInfoByIdLiveData(workRequest.id)
            .observe(this) { workInfo ->
                if (workInfo?.state == WorkInfo.State.SUCCEEDED) {
                    Toast.makeText(this@MainActivity, "Ready", Toast.LENGTH_SHORT).show()
                }
            }

//        WorkManager.getInstance(this)
//            .beginWith(workRequest)
//            .then(workRequest2)
//            .enqueue()
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(wifiReceiver)
        unregisterReceiver(bluetoothReceiver)
        unregisterReceiver(userLoginReceiver)
    }
}

fun Context.toast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}