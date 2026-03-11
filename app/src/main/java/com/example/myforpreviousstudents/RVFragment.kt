package com.example.myforpreviousstudents

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myforpreviousstudents.databinding.FragmentRVBinding

class RVFragment : Fragment() {
    lateinit var binding: FragmentRVBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRVBinding.inflate(inflater)
        val users = mutableListOf(
            User("Ivan", "Ivanov"),
            User("Mihail", "Ivanov"),
            User("Vasya", "Ivanov"),
            User("Petya", "Ivanov"),
        )

        val adapter = UserAdapter(users)
        binding.rv.layoutManager = LinearLayoutManager(context)
        binding.rv.adapter = adapter
        adapter.addUser(User("Test", "Testov"))

        return binding.root
    }
}