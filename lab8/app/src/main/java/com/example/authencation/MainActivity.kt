package com.example.authencation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.authencation.ui.navigation.NavGraph
import com.example.authencation.ui.navigation.Screen
import com.example.authencation.ui.theme.AuthencationTheme
import com.example.authencation.viewmodel.AuthState
import com.example.authencation.viewmodel.AuthViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AuthencationTheme {
                val authViewModel: AuthViewModel = viewModel()
                val authState = authViewModel.authState.collectAsState()
                
                val startDestination = if (authState.value is AuthState.Success) {
                    Screen.Home.route
                } else {
                    Screen.Login.route
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavGraph(
                        authViewModel = authViewModel,
                        startDestination = startDestination
                    )
                }
            }
        }
    }
}