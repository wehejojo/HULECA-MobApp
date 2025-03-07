package com.example.huleca_mobapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.*
import com.example.huleca_mobapp.ui.theme.HULECaMobAppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      HULECaMobAppTheme {
        val navController = rememberNavController()
        AppNavHost(navController = navController)
      }
    }
  }
}

@Composable
fun AppNavHost(navController: NavHostController) {
  NavHost(navController, startDestination = "home"){
    composable("home"){ HomeScreen(navController) }
    composable("violations"){ DetectedViolations() }
    composable("pastViolations"){ PastViolations() }
  }
}

@Composable
fun HomeScreen(navController: NavController) {
  Column(
    modifier = Modifier.fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ){
    Text(text = "HULECa", fontSize = MaterialTheme.typography.headlineLarge.fontSize)
    Spacer(modifier = Modifier.height(16.dp))
    
    Button(onClick = { navController.navigate("violations") }) {
      Text(text = "Detected Violations")
    }
    Button(onClick = { navController.navigate("pastViolations") }) {
      Text(text = "Past Violations")
    }
  }
}

@Composable
fun DetectedViolations(){
  Text(text = "Detected Violations ( Test Change >:) )")
}

@Composable
fun PastViolations(){
  Text(text = "Past Violations")
}

suspend fun fetchViolationData(url: String): String {
  return withContext(Dispatchers.IO){
    try{
      URL(url).readText()
    } catch (err: Exception){
      "Error: ${err.message}"
    }
  }
}