package com.example.myapplication.lab_1_2

import android.graphics.Typeface.BOLD
import android.os.Bundle
import android.provider.CalendarContract
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.App
import com.example.myapplication.Greeting

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            greeting("LoiKobi")
        }
    }
}
@Composable
fun App() {
    var count by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Số lần bấm: $count",
            fontSize = 24.sp
        )

        Button(onClick = {
            count++
        }) {
            Text("Bấm để tăng")
        }
    }
}


@Preview(
    showBackground = true,
    name = "Light Mode"
)
@Composable
fun GreetingReview() {
    greeting()
}
@Composable
fun greeting(name : String = "Android") {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Hello $name!",
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = Color.Black,
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                )
            )
            Row (

            ){
                Button(onClick = {
                    print("Clicked")
                }) {
                    Text("Button")
                }
                OutlinedButton(onClick = {
                    print("Clicked")
                }) {
                    Text("OutlinedButton")
                }
                TextButton(
                    onClick =
                        {
                            print("Clicked")
                        }) {
                    Text("TextButton")
                }
            }
        }
}