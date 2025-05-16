package com.rubylearner.ktorclient

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.rubylearner.ktorclient.di.appModule
import com.rubylearner.ktorclient.ui.HomeScreen
import org.koin.compose.KoinApplication

@Composable
fun App() {
    KoinApplication(application = {
        modules(appModule())
    }) {
        MaterialTheme {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {Text("Cryptocurrency App")}
                    )
                }
            ) { padding ->

                Column(Modifier.fillMaxWidth().padding(padding), horizontalAlignment = Alignment.CenterHorizontally) {

                    HomeScreen()
                }
            }
        }
    }

}