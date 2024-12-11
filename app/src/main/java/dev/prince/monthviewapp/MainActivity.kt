package dev.prince.monthviewapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import dagger.hilt.android.AndroidEntryPoint
import dev.prince.monthviewapp.ui.calendar.CalendarScreen
import dev.prince.monthviewapp.ui.theme.MonthViewAppTheme
import java.time.LocalDate

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MonthViewAppTheme {

                CalendarScreen()
            }
        }
    }
}
