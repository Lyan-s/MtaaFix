package com.example.mtaafix

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.mtaafix.ui.navigation.AppNavigation
import com.example.mtaafix.ui.theme.MtaaFixTheme
import com.example.mtaafix.ui.theme.ThemeMode

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            val prefs = remember {
                context.getSharedPreferences("mtaafix_prefs", Context.MODE_PRIVATE)
            }

            // Theme preference persists across app restarts via SharedPreferences,
            // the same store already used for the onboarding-complete flag.
            var themeMode by remember {
                mutableStateOf(
                    ThemeMode.valueOf(
                        prefs.getString("theme_mode", ThemeMode.SYSTEM.name) ?: ThemeMode.SYSTEM.name
                    )
                )
            }

            val useDarkTheme = when (themeMode) {
                ThemeMode.SYSTEM -> isSystemInDarkTheme()
                ThemeMode.LIGHT -> false
                ThemeMode.DARK -> true
            }

            MtaaFixTheme(darkTheme = useDarkTheme) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AppNavigation(
                        themeMode = themeMode,
                        onThemeModeChange = { newMode ->
                            themeMode = newMode
                            prefs.edit().putString("theme_mode", newMode.name).apply()
                        }
                    )
                }
            }
        }
    }
}