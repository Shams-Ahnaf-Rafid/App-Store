package com.example.appstore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            NavHost(navController, startDestination = "home") {
                composable("home") {
                    App(navController)
                }
                composable(
                    "details/{name}/{downloads}/{subject}/{kids}/{packageName}/{imageUrl}",
                    arguments = listOf(
                        navArgument("name") { type = NavType.StringType },
                        navArgument("downloads") { type = NavType.IntType },
                        navArgument("subject") { type = NavType.StringType },
                        navArgument("kids") { type = NavType.BoolType },
                        navArgument("packageName") { type = NavType.StringType },
                    )
                ) { backStackEntry ->
                    val name = backStackEntry.arguments?.getString("name")?.let {
                        URLDecoder.decode(it, StandardCharsets.UTF_8.toString())
                    } ?: ""
                    val subject = backStackEntry.arguments?.getString("subject")?.let {
                        URLDecoder.decode(it, StandardCharsets.UTF_8.toString())
                    } ?: ""
                    val packageName = backStackEntry.arguments?.getString("packageName")?.let {
                        URLDecoder.decode(it, StandardCharsets.UTF_8.toString())
                    } ?: ""
                    val imageUrl = backStackEntry.arguments?.getString("imageUrl")?.let {
                        URLDecoder.decode(it, StandardCharsets.UTF_8.toString())
                    } ?: ""
                    val downloads = backStackEntry.arguments?.getInt("downloads") ?: 0
                    val kids = backStackEntry.arguments?.getBoolean("kids") ?: false

                    DetailScreen(name, downloads, subject, kids, packageName, imageUrl)
                }
            }
        }
    }
}
