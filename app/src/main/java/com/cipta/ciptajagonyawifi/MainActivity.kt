package com.cipta.ciptajagonyawifi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.cipta.ciptajagonyawifi.ui.theme.CiptaJagonyaWifiTheme
import com.cipta.ciptajagonyawifi.navigation.NavGraph
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CiptaJagonyaWifiTheme {
                val navController = rememberNavController()
                Scaffold { innerPadding ->
                    NavGraph(navController = navController, modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}
