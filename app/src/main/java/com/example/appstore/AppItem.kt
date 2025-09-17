package com.example.appstore

data class AppItem(
    val name: String = "",
    val downloads: Int = 0,
    val subject: String = "Unknown",
    val kids: Boolean = false,
    val packageName: String = "abc",
    val imageUrl: String = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRb5LOPUgzjbz_m4aVulC-GU5zu-30HBdYnAg&s"
)
