package com.example.appstore

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage


@Composable
fun DetailScreen(name: String, downloads: Int, subject: String, kids: Boolean, packageName: String, imageUrl: String) {

    val context = LocalContext.current

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize(1f)
    ) {
        Card(
            elevation = CardDefaults.cardElevation(2.dp),
            modifier = Modifier
                .height(250.dp)
                .width(300.dp)
                .padding(0.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {

                AsyncImage(
                    model = imageUrl,
                    contentDescription = "App Image",
                    modifier = Modifier
                        .size(100.dp)
                        .padding(top = 30.dp, start = 2.dp, end = 2.dp, bottom = 6.dp)
                )
                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxSize(1f)
                        .padding(16.dp)
                ) {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(Modifier.height(10.dp))
                    Text(
                        text = "Total Downloads: $downloads",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(Modifier.height(10.dp))
                    Text(
                        text = subject,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(Modifier.height(10.dp))
                    Text(
                        text = "For Kids: ${if (kids) "Yes" else "No"}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(Modifier.height(16.dp))
                    if (packageName != "abc") {
                        Button(
                            onClick = {
                                openPlayStorePage(context, packageName)
                            },
                            //modifier = Modifier.align(Alignment.CenterHorizontally)
                        ) {
                            Text("Install")
                        }
                    }
                }
            }
        }
    }
}

fun openPlayStorePage(context: Context, packageName: String) {
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$packageName"))
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
//        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$packageName"))
//        context.startActivity(intent)
    }
}
