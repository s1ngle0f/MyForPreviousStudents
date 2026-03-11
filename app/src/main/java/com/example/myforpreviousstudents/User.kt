package com.example.myforpreviousstudents

data class User(
    val name: String,
    val surname: String,
    val email: String? = null,
    val phone: String? = null
)
