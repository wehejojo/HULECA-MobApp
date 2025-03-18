package com.example.huleca_mobapp

import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import android.util.Log
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationCompat
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.*
import com.example.huleca_mobapp.ui.theme.HULECaMobAppTheme
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.net.URL

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      HULECaMobAppTheme {
        Box(
          modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
        ) {
          val navController = rememberNavController()
          AppNavHost(navController = navController)
          
          FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
              Log.w("FCM", "Fetching FCM token failed", task.exception)
              return@addOnCompleteListener
            }
            val token = task.result
            Log.d("FCM Token", token)
          }
        }
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
        color = Color.Black
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
fun PastViolations() {
  val response = remember { mutableStateOf<String?>(null) }
  val url = "http://192.168.100.6:3001/logs"
  
  LaunchedEffect(Unit) {
    response.value = fetchViolationData(url)
  }
  
  val violations = remember(response.value) {
    response.value?.let { jsonData ->
      try {
        val jsonArray = JSONArray(jsonData)
        List(jsonArray.length()) { index ->
          val obj = jsonArray.getJSONObject(index)
          Violation(
            location = obj.optString("logLocation", "Unknown Location"),
            time = obj.optString("logTime", "Unknown Time"),
            imagePath = obj.optString("logImagePath", "")
          )
        }
      } catch (e: Exception) {
        emptyList()
      }
    } ?: emptyList()
  }
  
  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(16.dp)
      .background(Color.White),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Spacer(modifier = Modifier.height(16.dp))
    
    Text(
      text = "Past Violations",
      fontSize = MaterialTheme.typography.headlineLarge.fontSize,
      color = Color.Black
    )
    
    when {
      response.value == null -> Text("Loading...")
      violations.isEmpty() -> Text("No Violations Found")
      else -> {
        LazyColumn {
          items(violations.size) { index ->
            val violation = violations[index]
            Column(
              modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(Color.Gray)
            ) {
//              Text("Location: ${violation.location}")
              Text(
                text = "Location: University of the Cordilleras",
                color = Color.Black
              )
              Text(
                text = "Time: ${violation.time}",
                color = Color.Black
              )
              Text(
                text = "Image Path: ${violation.imagePath}",
                color = Color.Black
              )
//              if (violation.imagePath.isNotBlank()) {
//                AsyncImage(
//                  model = violation.imagePath,
//                  contentDescription = "Violation Image",
//                  modifier = Modifier.size(200.dp)
//                )
//              }
            }
          }
        }
      }
    }
  }
}

data class Violation(
  val location: String,
  val time: String,
  val imagePath: String
)

suspend fun fetchViolationData(url: String): String {
  return withContext(Dispatchers.IO){
    try{
      URL(url).readText()
    } catch (err: Exception){
      "Error: ${err.message}"
    }
  }
}

class MyFirebaseMessagingService : FirebaseMessagingService() {
  override fun onMessageReceived(remoteMessage: RemoteMessage) {
    remoteMessage.notification?.let {
      sendNotification(it.body ?: "Violation Detected!")
    }
  }
  
  private fun sendNotification(messageBody: String) {
    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val notificationBuilder = NotificationCompat.Builder(this, "channel_id")
      .setSmallIcon(R.drawable.ic_launcher_foreground)
      .setContentTitle("Violation Alert")
      .setContentText(messageBody)
      .setPriority(NotificationCompat.PRIORITY_HIGH)
    
    notificationManager.notify(0, notificationBuilder.build())
  }
}