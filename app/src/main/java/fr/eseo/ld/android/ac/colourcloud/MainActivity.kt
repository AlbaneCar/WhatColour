package fr.eseo.ld.android.ac.colourcloud

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import fr.eseo.ld.android.ac.colourcloud.ui.WhatColourApp
import fr.eseo.ld.android.ac.colourcloud.ui.theme.WhatColourTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WhatColourTheme {
                WhatColourApp(context = this)
            }
        }
    }
}
