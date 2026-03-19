package com.example.myforpreviousstudents

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.myforpreviousstudents.databinding.FragmentHomeBinding

class HomeFragment : Fragment(), LocationListener {
    lateinit var binding: FragmentHomeBinding

    private val REQ_NOTIF = 1001

    private fun ensureNotificationPermission() {
        if (android.os.Build.VERSION.SDK_INT >= 33) {
            val granted = ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.POST_NOTIFICATIONS
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

    //////////

    private lateinit var locationManager: LocationManager
    private val REQ_LOCATION = 1001

    private fun startLocationUpdates() {
//        if (ActivityCompat.checkSelfPermission(
//                requireContext(),
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            return
//        }
        val granted = ContextCompat.checkSelfPermission(
            requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (!granted) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQ_LOCATION
            )
        }

        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            5000,
            5f,
            this
        )
    }

    //////////

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        ensureNotificationPermission()
        ensureChannel(context = requireContext())
        binding.buttonNotify.setOnClickListener {
            showNotification(requireContext())
        }

        locationManager = requireContext()
            .getSystemService(Context.LOCATION_SERVICE) as LocationManager
        startLocationUpdates()

        return binding.root
    }

    override fun onLocationChanged(location: Location) {
        val lat = location.latitude
        val lon = location.longitude

        val targetMoscow = Location("target").apply {
            latitude = 55.4507
            longitude = 37.3657
        }
        val targetWashington = Location("target").apply {
            latitude = 38.5342
            longitude = -77.0211
        }

        val distanceToMoscow = location.distanceTo(targetMoscow)
        val distanceToWashington = location.distanceTo(targetWashington)

        if(distanceToMoscow > distanceToWashington){
            binding.catImage.setImageResource(R.drawable.usa_cat)
        } else {
            binding.catImage.setImageResource(R.drawable.russia_cat)
        }

        Toast.makeText(
            requireContext(),
            "Lat: $lat\nLon: $lon",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onPause() {
        super.onPause()
        locationManager.removeUpdates(this)
    }
}