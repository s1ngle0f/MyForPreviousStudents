package com.example.myforpreviousstudents

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class UserLoginReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == ACTION_USER_LOGGED_IN) {
            val userId = intent.getIntExtra("user_id", -1)
            Log.d("UserReceiver", "Пользователь вошёл: $userId")
        }
    }
}