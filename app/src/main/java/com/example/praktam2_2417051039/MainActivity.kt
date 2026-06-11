package com.example.praktam2_2417051039

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.praktam2_2417051039.ui.navigation.DeadlineRiskApp
import com.example.praktam2_2417051039.ui.theme.PrakTAM2_2417051039Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PrakTAM2_2417051039Theme {
                DeadlineRiskApp()
            }
        }
    }
}
