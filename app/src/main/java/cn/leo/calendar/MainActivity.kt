package cn.leo.calendar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import cn.leo.calendar.ui.theme.Compose_calendarTheme
import cn.leo.calendar.ui.theme.LeoCalendar

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Compose_calendarTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Home(
                        name = "日历测试",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Home(name: String, modifier: Modifier = Modifier) {
    Column {
        Text(
            text = name,
            modifier = modifier
        )
        LeoCalendar()
    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Compose_calendarTheme {
        Home("日历测试")
    }
}