package com.example.huleca_mobapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    modifier = Modifier
      .fillMaxSize()
      .padding(16.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    
    Spacer(modifier = Modifier.weight(1f))
    
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .weight(1f),
      contentAlignment = Alignment.TopCenter
    ) {
      Text(
        text = "HULECa",
        fontSize = MaterialTheme.typography.headlineLarge.fontSize,
        color = Color.White
      )
    }
    
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .weight(1f),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
    ) {
      ElevatedButton(
        onClick = { navController.navigate("violations") },
        modifier = Modifier
          .height(48.dp)
          .width(200.dp),
        colors = ButtonDefaults.elevatedButtonColors(
          containerColor = Color.Gray,
          contentColor = Color.Black
        )
      ) {
        Text(text = "Detected Violations")
      }
      
      Spacer(modifier = Modifier.height(16.dp))
      
      ElevatedButton(
        onClick = { navController.navigate("pastViolations") },
        modifier = Modifier
          .height(48.dp)
          .width(200.dp),
        colors = ButtonDefaults.elevatedButtonColors(
          containerColor = Color.Gray,
          contentColor = Color.Black
        )
      ) {
        Text(text = "Past Violations")
      }
    }
    
    Spacer(modifier = Modifier.weight(1f))
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