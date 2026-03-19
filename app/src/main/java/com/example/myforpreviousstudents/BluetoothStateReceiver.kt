package com.example.myforpreviousstudents

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

class BluetoothStateReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == BluetoothAdapter.ACTION_STATE_CHANGED) {
            val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)

            when (state) {
                BluetoothAdapter.STATE_ON -> {
                    Log.d("BluetoothReceiver", "Bluetooth включен")
                    Toast.makeText(context, "Bluetooth включен", Toast.LENGTH_SHORT).show()

                }
                BluetoothAdapter.STATE_OFF -> {
                    Log.d("BluetoothReceiver", "Bluetooth выключен")
                    Toast.makeText(context, "Bluetooth включен", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}