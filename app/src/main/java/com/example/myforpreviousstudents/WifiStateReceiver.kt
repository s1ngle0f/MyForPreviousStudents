package com.example.myforpreviousstudents

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.util.Log
import android.widget.Toast

class WifiStateReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == WifiManager.WIFI_STATE_CHANGED_ACTION) {
            val state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN)

            when (state) {
                WifiManager.WIFI_STATE_ENABLED -> {
                    Log.d("WifiReceiver", "Wi-Fi включен")
                    Toast.makeText(context, "WiFi включен", Toast.LENGTH_SHORT).show()
                }
                WifiManager.WIFI_STATE_DISABLED -> {
                    Log.d("WifiReceiver", "Wi-Fi выключен")
                    Toast.makeText(context, "WiFi выключен", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}