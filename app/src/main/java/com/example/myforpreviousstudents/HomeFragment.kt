package com.example.myforpreviousstudents

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.myforpreviousstudents.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding

    private val REQ_NOTIF = 1001

    private fun ensureNotificationPermission() {
        if (android.os.Build.VERSION.SDK_INT >= 33) {
            val granted = ContextCompat.checkSelfPermission(
                context!!, Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if (!granted) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    REQ_NOTIF
                )
            }
        }
    }

    fun ensureChannel(context: Context) {
        val nm = context.getSystemService(NotificationManager::class.java)

        val channel = NotificationChannel(
            "default",
            "Default notifications",
            NotificationManager.IMPORTANCE_DEFAULT
        )

        nm.createNotificationChannel(channel)
    }

    fun showNotification(context: Context) {
        val notification = NotificationCompat.Builder(context, "default")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Download complete")
            .setContentText("File saved successfully")
            .setAutoCancel(true)
            .build()

        val nm = context.getSystemService(NotificationManager::class.java)

        nm.notify(1, notification)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        ensureNotificationPermission()
        ensureChannel(context = context!!)
        binding.buttonNotify.setOnClickListener {
            showNotification(context!!)
        }
        return binding.root
    }
}